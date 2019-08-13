package com.example.derich.bizwiz.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;

public class Transactions extends AppCompatActivity {
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ListView listView = findViewById(R.id.transactions);
        myDB = new DatabaseHelper(this);

        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        Cursor transact = myDB.getAllTransactions();
        if(transact.getCount() == 0){
            Toast.makeText(this, "There are no transactions in this list!", Toast.LENGTH_LONG).show();
        }else{
            StringBuffer bufferr = new StringBuffer();
            while(transact.moveToNext()){
                theList.add("transaction id :" + transact.getString(0));
                theList.add("Transaction type :" + transact.getString(1));
                theList.add("Transaction date :" + transact.getString(2) + "\n\n");
                ListAdapter list1Adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(list1Adapter);
            }
        }


    }


}

