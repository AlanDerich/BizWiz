package com.example.derich.bizwiz.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_TRANSACTION_STATUS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_TRANSACTIONS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TRANSACTION_DATE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TRANSACTION_ID;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TRANSACTION_TYPE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TRANSACTION_USER;

public class Transactions extends AppCompatActivity {
    DatabaseHelper myDB;
    ListView listView;
    private String mStatus;
    private Cursor mTransact;
    private String mSql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        Intent intent = getIntent();
        mStatus = intent.getStringExtra("status");
        listView = findViewById(R.id.transactions);
        myDB = new DatabaseHelper(this);
        String Admin = PreferenceHelper.getUsername();

        //populate an ArrayList<String> from the database and then view it
      if (Admin.equals("Admin")) {
          ArrayList<String> theList = new ArrayList<>();
          if (mStatus== null){
              mSql = "select * from " + TABLE_TRANSACTIONS+ " ORDER BY " + TRANSACTION_DATE + " DESC;";
              mTransact = myDB.getAllTransactions(mSql);
          }
          else {
              mSql = "select * from " + TABLE_TRANSACTIONS+ " WHERE "+ COLUMN_TRANSACTION_STATUS + " =0 " + " ORDER BY " + TRANSACTION_DATE + " DESC;";
              mTransact = myDB.getAllTransactions(mSql);
          }

          if (mTransact.getCount() == 0) {
              Toast.makeText(this, "There are no transactions in this list!", Toast.LENGTH_LONG).show();
          } else {
              while (mTransact.moveToNext()) {
                  theList.add("transaction id :" + mTransact.getString(mTransact.getColumnIndex(TRANSACTION_ID)));
                  theList.add("Transaction type :" + mTransact.getString(mTransact.getColumnIndex(TRANSACTION_TYPE)));
                  theList.add("Transaction User : " + mTransact.getString(mTransact.getColumnIndex(TRANSACTION_USER)));
                  theList.add("Transaction date :" + mTransact.getString(mTransact.getColumnIndex(TRANSACTION_DATE)));
                  if (Integer.valueOf(mTransact.getString(mTransact.getColumnIndex(COLUMN_TRANSACTION_STATUS)))>0){
                      theList.add("Transaction Status :" + "Synced" + "\n\n");
                  }
                  else {
                      theList.add("Transaction Status :" + "Not Synced" + "\n\n");
                  }

                  ListAdapter list1Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                  listView.setAdapter(list1Adapter);
              }
          }
      }
      else {
          ArrayList<String> theList = new ArrayList<>();
          long timeMillis = System.currentTimeMillis();
      //    String date = AddedFloat.getDate(timeMillis);
          if (mStatus== null){
              String sql = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + TRANSACTION_USER + " = ? "  + " ORDER BY " + TRANSACTION_DATE + " DESC;";
              mTransact = myDB.getAllTransactionsUser(sql,Admin);
          }
          else {
              String sql = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TRANSACTION_STATUS + " =0 "  +" AND " + TRANSACTION_USER + " = ? "  + " ORDER BY " + TRANSACTION_DATE + " DESC;";
              mTransact = myDB.getAllTransactionsUser(sql,Admin);
          }
          if (this.mTransact.getCount() == 0) {
              Toast.makeText(this, "There are no transactions in this list!", Toast.LENGTH_LONG).show();
          } else {
              while (this.mTransact.moveToNext()) {
                  theList.add("transaction id :" + this.mTransact.getString(this.mTransact.getColumnIndex(TRANSACTION_ID)));
                  theList.add("Transaction type :" + this.mTransact.getString(this.mTransact.getColumnIndex(TRANSACTION_TYPE)));
                  theList.add("Transaction User : " + this.mTransact.getString(this.mTransact.getColumnIndex(TRANSACTION_USER)));
                  theList.add("Transaction date :" + this.mTransact.getString(this.mTransact.getColumnIndex(TRANSACTION_DATE)));
                  if (Integer.valueOf(mTransact.getString(mTransact.getColumnIndex(COLUMN_TRANSACTION_STATUS)))>0){
                      theList.add("Transaction Status :" + "Synced" + "\n\n");
                  }
                  else {
                      theList.add("Transaction Status :" + "Not Synced" + "\n\n");
                  }
                  ListAdapter list1Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                  listView.setAdapter(list1Adapter);
              }
          }
      }
        mTransact.close();
    }


}

