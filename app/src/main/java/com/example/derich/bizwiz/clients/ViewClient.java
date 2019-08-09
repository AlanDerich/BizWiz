package com.example.derich.bizwiz.clients;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_CLIENT_DEBT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_CLIENT_EMAIL;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_CLIENT_FULLNAME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_NUMBER;

public class ViewClient  extends AppCompatActivity {
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_clientss);

        ListView listView = findViewById(R.id.listview);
        myDB = new DatabaseHelper(this);

        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getAllData();
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        }else{
            StringBuffer buffer = new StringBuffer();
            while(data.moveToNext()){
                theList.add("Full Name :" + data.getString(data.getColumnIndex(COLUMN_CLIENT_FULLNAME)));
                theList.add("Debt :" + data.getString(data.getColumnIndex(COLUMN_CLIENT_DEBT)));
                theList.add("Number :" + data.getString(data.getColumnIndex(COLUMN_NUMBER)));
                theList.add("Email :" + data.getString(data.getColumnIndex(COLUMN_CLIENT_EMAIL)) + "\n");
                theList.add("\n");
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ViewClient.this, ClientsDetails.class);
                        startActivity(intent);
                    }
                });
            }
        }


    }
}

