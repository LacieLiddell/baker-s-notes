package com.example.lacie.bakernotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowItemActivity extends Activity implements OnClickListener {
    Button changeButtom, deleteButtom;
    TextView nameText, ingredientsText, programText;
    SQLiteOpenHelper helper;
    SQLiteDatabase db;
    int id;
    static final String TAG = "TAG";
    static final int DIALOG_DELETE = 1;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        nameText = (TextView) findViewById(R.id.nameText);
        ingredientsText = (TextView) findViewById(R.id.ingredientsText);
        programText = (TextView) findViewById(R.id.programText);
        changeButtom = (Button) findViewById(R.id.changeButton);
        deleteButtom = (Button) findViewById(R.id.deleteButton);
        deleteButtom.setOnClickListener(this);
        changeButtom.setOnClickListener(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("itemID", -1);
        fillParams();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeButton:
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("itemID", id);
                startActivityForResult(intent, 1);
                //fillEditFields();
               /*AlertDialog.Builder editBuilder = new AlertDialog.Builder(ShowItemActivity.this);
               editBuilder.setView(R.layout.activity_edit);
               editBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                   }
               });

               editBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });*/


                break;
            case R.id.deleteButton:
              /* Intent intentDelete = new Intent (this, DeleteActivity.class);
               intentDelete.putExtra("itemID", id);
               startActivityForResult(intentDelete, 2);*/

                AlertDialog.Builder builder = new AlertDialog.Builder(ShowItemActivity.this);
                builder.setMessage(R.string.sure);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRecipe();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }
    }

   /* public  void fillEditFields(){
        EditText nameForEdit = (EditText)findViewById(R.id.nameForEdit);
        EditText ingredientsForEdit = (EditText)findViewById(R.id.ingredientsForEdit);
        EditText programForEdit = (EditText) findViewById(R.id.prorgamForEdit);
        try{
            helper = new DBHelper(this);
            db = helper.getWritableDatabase();
            Log.i(TAG, "id = "+Integer.toString(id));
            Cursor cursor = db.query("RECIPES", new String[]{"NAME", "INGREDIENTS", "PROGRAM"}, "_id = ?",
                    new String[]{Integer.toString(id)}, null, null, null);
            if (cursor.moveToFirst()){
                Log.i(TAG, "cursor");
                Log.i(TAG, cursor.getString(0));
                nameForEdit.setText(cursor.getString(0), EDITABLE);
                ingredientsForEdit.setText(cursor.getString(1), EDITABLE);
                programForEdit.setText(cursor.getString(2), EDITABLE);
            }
            cursor.close();

        }catch (SQLException ex){
            Toast errorToast = Toast.makeText(getApplicationContext(), R.string.dbError, Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }*/

    public void deleteRecipe() {
        try {
            helper = new DBHelper(this);
            db = helper.getWritableDatabase();
            db.delete("RECIPES", "_id = ?", new String[]{Integer.toString(id)});
            // Intent success = new Intent(this, ShowAllActivity.class);
            Toast toast = Toast.makeText(this, R.string.successDelete, Toast.LENGTH_LONG);
            toast.show();
            Intent intentDel = new Intent(getApplicationContext(), ShowAllActivity.class);
            startActivity(intentDel);
        } catch (SQLException ex) {
            Toast errorToast = Toast.makeText(getApplicationContext(), R.string.dbError, Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Intent retIntent = getIntent();
                    id = retIntent.getIntExtra("itemID", -1);
                    fillParams();
                    break;
                case 2:
                    break;
            }
        } else {
            return;
        }
    }

    public void fillParams() {
        helper = new DBHelper(this);
        db = helper.getWritableDatabase();
        cursor = db.query("RECIPES", new String[]{"NAME", "INGREDIENTS", "PROGRAM"}, "_id = ?",
                new String[]{Integer.toString(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            nameText.setText(cursor.getString(0));
            ingredientsText.setText(cursor.getString(1));
            programText.setText(cursor.getString(2));
        }
        // cursor.close();

    }

    public void onRestart() {
        super.onRestart();
        cursor.requery();

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, ShowAllActivity.class);
        startActivity(i);
    }

    public void onPause() {
        super.onPause();
        db.close();
        helper.close();
    }

}
