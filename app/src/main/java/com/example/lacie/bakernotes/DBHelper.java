package com.example.lacie.bakernotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lacie on 23.08.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "NOTES";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DB_NAME, "DB onCreate");
        db.execSQL("CREATE TABLE RECIPES( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, INGREDIENTS TEXT, PROGRAM TEXT, FAVORITE BOOLEAN);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
