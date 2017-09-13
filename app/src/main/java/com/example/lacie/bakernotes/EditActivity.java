package com.example.lacie.bakernotes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.TextView.BufferType.EDITABLE;

public class EditActivity extends Activity implements OnClickListener {
    Button saveChanges;
    EditText nameForEdit, ingredientsForEdit, programForEdit;
    DBHelper helper;
    SQLiteDatabase db;
    Cursor cursor;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        saveChanges = (Button) findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(this);
        nameForEdit = (EditText) findViewById(R.id.nameForEdit);
        ingredientsForEdit = (EditText) findViewById(R.id.ingredientsForEdit);
        programForEdit = (EditText) findViewById(R.id.prorgamForEdit);
        Intent intent = getIntent();
        id = intent.getIntExtra("itemID", -1);
        Log.i("TAG", String.valueOf(id));
        try {
            helper = new DBHelper(this);
            db = helper.getWritableDatabase();
            cursor = db.query("RECIPES", new String[]{"NAME", "INGREDIENTS", "PROGRAM"}, "_id = ?",
                    new String[]{Integer.toString(id)}, null, null, null);
            if (cursor.moveToFirst()) {
                nameForEdit.setText(cursor.getString(0), EDITABLE);
                ingredientsForEdit.setText(cursor.getString(1), EDITABLE);
                programForEdit.setText(cursor.getString(2), EDITABLE);
            }
            cursor.close();
        } catch (SQLException ex) {
            Toast errorToast = Toast.makeText(getApplicationContext(), R.string.dbError, Toast.LENGTH_SHORT);
            errorToast.show();
        }

    }

    @Override
    public void onClick(View v) {
        if (ingredientsForEdit.getText().toString().equals("") || programForEdit.getText().toString().equals("")
                || nameForEdit.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.empty, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            try {
                ContentValues cv = new ContentValues();
                cv.put("NAME", nameForEdit.getText().toString());
                cv.put("INGREDIENTS", ingredientsForEdit.getText().toString());
                cv.put("PROGRAM", programForEdit.getText().toString());
                db.update("RECIPES", cv, "_id = ?", new String[]{Integer.toString(id)});
                Intent intent = new Intent(this, ShowItemActivity.class);
                intent.putExtra("itemID", id);
                setResult(RESULT_OK, intent);

                finish();
            } catch (SQLException ex) {
                Toast errorToast = Toast.makeText(getApplicationContext(), R.string.dbError, Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }

    }

    public void onPause() {
        super.onPause();
        db.close();
        helper.close();

    }

}
