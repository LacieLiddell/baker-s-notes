package com.example.lacie.bakernotes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity implements OnClickListener {

    Button saveButton;
    EditText ingredientsEdit, programEdit, nameEdit;
    Intent intent;
    DBHelper helper;
    SQLiteDatabase db;
    ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        ingredientsEdit = (EditText) findViewById(R.id.ingredientsEdit);
        programEdit = (EditText) findViewById(R.id.prorgamEdit);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
    }

    @Override
    public void onClick(View v) {
        if (ingredientsEdit.getText().toString().equals("") || programEdit.getText().toString().equals("")
                || nameEdit.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.empty, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            try {
                cv = new ContentValues();
                helper = new DBHelper(this);
                db = helper.getWritableDatabase();
                cv.put("NAME", nameEdit.getText().toString());
                cv.put("INGREDIENTS", ingredientsEdit.getText().toString());
                cv.put("PROGRAM", programEdit.getText().toString());
                cv.put("FAVORITE", false);
                db.insert("RECIPES", null, cv);
                Cursor cursor = db.query("RECIPES", new String[]{"_id", "NAME"}, "NAME = ?", new String[]{nameEdit.getText().toString()},
                        null, null, "_id DESC");
                if (cursor.moveToFirst()) {
                    //прочитать из базы запись со схожим именем, если их несколько, Расположить по убыванию id, взять первую, получить id
                    int id = Integer.parseInt(cursor.getString(0));
                    intent = new Intent(this, ShowItemActivity.class);
                    intent.putExtra("itemID", id);
                    cursor.close();
                    db.close();
                    helper.close();
                }
                startActivity(intent);
            } catch (SQLException ex) {
                Toast errorToast = Toast.makeText(getApplicationContext(), R.string.dbError, Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();

    }
}
