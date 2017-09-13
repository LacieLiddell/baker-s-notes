package com.example.lacie.bakernotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener {
    //Button showButton, addButton;
    ListView favoriteList, menuList;
    Intent intent;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuList = (ListView) findViewById(R.id.menuList);
        favoriteList = (ListView) findViewById(R.id.favoriteList);
        adapter = ArrayAdapter.createFromResource(this, R.array.mainMenu, android.R.layout.simple_list_item_1);
        menuList.setAdapter(adapter);
        menuList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), ShowAllActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), AddActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), ListNetActivity.class));
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

    }
}
