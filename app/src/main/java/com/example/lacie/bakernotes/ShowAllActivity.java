package com.example.lacie.bakernotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ShowAllActivity extends Activity {
    final static int MENU_DELETE = 0;
    final static int MENU_EDIT = 1;
    CursorAdapter listAdapter;
    ListView listAll;
    Cursor cursor;
    DBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        listAll = (ListView) findViewById(R.id.listAll);
        registerForContextMenu(listAll);
        fillList();
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_EDIT, 0, R.string.change);
        menu.add(0, MENU_DELETE, 0, R.string.delete);
    }

    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo adapter = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case MENU_EDIT:
                startActivity(new Intent(getApplicationContext(), EditActivity.class).putExtra("itemID", (int) adapter.id));
                break;
            case MENU_DELETE:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ShowAllActivity.this);
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
                        deleteRecipe((int) adapter.id);

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                break;

        }

        return super.onContextItemSelected(item);

    }

    public void registerForContextMenu(View view) {
        view.setOnCreateContextMenuListener(this);
    }

    public void deleteRecipe(int id) {
        try {
            db.delete("RECIPES", "_id = ?", new String[]{Integer.toString(id)});
            Toast toast = Toast.makeText(this, R.string.successDelete, Toast.LENGTH_LONG);

            listAdapter.notifyDataSetChanged();
            onResume();
            toast.show();
        } catch (SQLException ex) {
            Toast errorToast = Toast.makeText(getApplicationContext(), R.string.dbError, Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }

    private void fillList() {
        try {
            helper = new DBHelper(this);
            db = helper.getReadableDatabase();
            cursor = db.query("RECIPES", new String[]{"_id", "NAME"}, null, null, null, null, null);
            listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                    cursor, new String[]{"NAME"}, new int[]{android.R.id.text1}, 0);
            listAll.setAdapter(listAdapter);
            listAll.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), ShowItemActivity.class);
                    intent.putExtra("itemID", (int) id);
                    startActivity(intent);
                }
            });
        } catch (SQLException ex) {
            Toast errorToast = Toast.makeText(getApplicationContext(), R.string.dbError, Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void fabClick(View v) {
        startActivity(new Intent(this, AddActivity.class));
    }

    public void onResume() {
        super.onResume();
        cursor.requery();
    }


    public void onDestroy() {
        super.onDestroy();
        db.close();
        helper.close();
    }
}
