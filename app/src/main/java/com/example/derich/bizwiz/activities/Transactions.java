package com.example.derich.bizwiz.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.mpesa.AddedFloat;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_TRANSACTION_STATUS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TRANSACTION_DATE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TRANSACTION_ID;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TRANSACTION_TYPE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TRANSACTION_USER;

public class Transactions extends AppCompatActivity {
    DatabaseHelper myDB;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        listView = findViewById(R.id.transactions);
        myDB = new DatabaseHelper(this);
        String Admin = PreferenceHelper.getUsername();

        //populate an ArrayList<String> from the database and then view it
      if (Admin.equals("Admin")) {
          ArrayList<String> theList = new ArrayList<>();
          Cursor transact = myDB.getAllTransactions();
          if (transact.getCount() == 0) {
              Toast.makeText(this, "There are no transactions in this list!", Toast.LENGTH_LONG).show();
          } else {
              while (transact.moveToNext()) {
                  theList.add("transaction id :" + transact.getString(transact.getColumnIndex(TRANSACTION_ID)));
                  theList.add("Transaction type :" + transact.getString(transact.getColumnIndex(TRANSACTION_TYPE)));
                  theList.add("Transaction User : " + transact.getString(transact.getColumnIndex(TRANSACTION_USER)));
                  theList.add("Transaction date :" + transact.getString(transact.getColumnIndex(TRANSACTION_DATE)));
                  theList.add("Transaction Status :" + transact.getString(transact.getColumnIndex(COLUMN_TRANSACTION_STATUS)) + "\n\n");
                  ListAdapter list1Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                  listView.setAdapter(list1Adapter);
              }
          }
      }
      else {
          ArrayList<String> theList = new ArrayList<>();
          long timeMillis = System.currentTimeMillis();
          String date = AddedFloat.getDate(timeMillis);
          Cursor transact = myDB.getAllTransactionsUser(Admin);
          if (transact.getCount() == 0) {
              Toast.makeText(this, "There are no transactions in this list!", Toast.LENGTH_LONG).show();
          } else {
              StringBuffer bufferr = new StringBuffer();
              while (transact.moveToNext()) {
                  theList.add("transaction id :" + transact.getString(transact.getColumnIndex(TRANSACTION_ID)));
                  theList.add("Transaction type :" + transact.getString(transact.getColumnIndex(TRANSACTION_TYPE)));
                  theList.add("Transaction User : " + transact.getString(transact.getColumnIndex(TRANSACTION_USER)));
                  theList.add("Transaction date :" + transact.getString(transact.getColumnIndex(TRANSACTION_DATE)));
                  theList.add("Transaction Status :" + transact.getString(transact.getColumnIndex(COLUMN_TRANSACTION_STATUS)) + "\n\n");
                  ListAdapter list1Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                  listView.setAdapter(list1Adapter);
              }
          }
      }

    }


}

