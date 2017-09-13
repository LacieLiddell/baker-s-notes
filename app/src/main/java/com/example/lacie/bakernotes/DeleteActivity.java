package com.example.lacie.bakernotes;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DeleteActivity extends Activity implements OnClickListener {
    Button confirmButton, cancelButton;
    DBHelper helper;
    SQLiteDatabase db;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        Intent getIntent = getIntent();
        id = getIntent.getIntExtra("itemID", -1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton:
                Intent cancelIntent = new Intent(this, ShowItemActivity.class);
                cancelIntent.putExtra("itemID", id);
                startActivity(cancelIntent);
                break;
            case R.id.confirmButton:
                try {
                    helper = new DBHelper(this);
                    db = helper.getWritableDatabase();
                    db.delete("RECIPES", "_id = ?", new String[]{Integer.toString(id)});
                    Intent success = new Intent(this, ShowAllActivity.class);
                    Toast toast = Toast.makeText(this, R.string.successDelete, Toast.LENGTH_LONG);
                    toast.show();
                    startActivity(success);
                } catch (SQLException ex) {
                    Toast errorToast = Toast.makeText(getApplicationContext(), R.string.dbError, Toast.LENGTH_SHORT);
                    errorToast.show();
                }
                break;
        }
    }
}
