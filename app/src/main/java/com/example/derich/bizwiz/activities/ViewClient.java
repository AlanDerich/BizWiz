package com.example.derich.bizwiz.activities;

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
                theList.add("client_Id :" + data.getString(0));
                theList.add("Full Name :" + data.getString(1));
                theList.add("Debt :" + data.getString(2));
                theList.add("Number :" + data.getString(3));
                theList.add("Email :" + data.getString(4) + "\n\n");
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

