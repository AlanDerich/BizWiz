package com.example.derich.bizwiz.credentials;

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

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_USER_EMAIL;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_USER_ID;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_USER_NAME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_USER_QUESTION;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_USER_STATUS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_USER;

public class ViewUsers extends AppCompatActivity {
    ListView mListView;
    private DatabaseHelper myDB;
    private String mStatus;
    private String mSql;
    private Cursor mTransact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);
        mListView = findViewById(R.id.listUsers);
        Intent intent = getIntent();
        mStatus = intent.getStringExtra("status");
        myDB = new DatabaseHelper(this);
        String Admin = PreferenceHelper.getUsername();
        if (Admin.equals("Admin")) {
            ArrayList<String> theList = new ArrayList<>();
            if (mStatus== null){
                mSql = "select * from " + TABLE_USER + " ORDER BY " + COLUMN_USER_NAME + " DESC;";
                mTransact = myDB.getAllUsersAdmin(mSql);
            }
            else {
                mSql = "select * from " + TABLE_USER+ " WHERE "+ COLUMN_USER_STATUS + " =0 " + " ORDER BY " + COLUMN_USER_NAME + " DESC;";
                mTransact = myDB.getAllUsersAdmin(mSql);
            }

            if (mTransact.getCount() == 0) {
                Toast.makeText(this, "There are no Users in this list!", Toast.LENGTH_LONG).show();
            } else {
                while (mTransact.moveToNext()) {
                    theList.add("User id :" + mTransact.getString(mTransact.getColumnIndex(COLUMN_USER_ID)));
                    theList.add("Username :" + mTransact.getString(mTransact.getColumnIndex(COLUMN_USER_NAME)));
                    theList.add("User email : " + mTransact.getString(mTransact.getColumnIndex(COLUMN_USER_EMAIL)));
                    theList.add("Security question :" + mTransact.getString(mTransact.getColumnIndex(COLUMN_USER_QUESTION)));
                    if (Integer.valueOf(mTransact.getString(mTransact.getColumnIndex(COLUMN_USER_STATUS))) > 0){
                        theList.add("Status :" + " Synced" + "\n\n");
                    }
                    else{
                        theList.add("Status :" + " Not Synced" + "\n\n");
                    }
                    ListAdapter list1Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                    mListView.setAdapter(list1Adapter);
                }
            }
        }
        else {
            ArrayList<String> theList = new ArrayList<>();
            long timeMillis = System.currentTimeMillis();
            //    String date = AddedFloat.getDate(timeMillis);
            if (mStatus== null){
                String[] name = {"Admin"};
                String sql = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_NAME + " = ? "  + " ORDER BY " + COLUMN_USER_NAME + " DESC;";
                mTransact = myDB.getAllUsers(sql,Admin);
            }
            else {
                String sql = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_STATUS + " =0 "  +" AND " + COLUMN_USER_NAME + " = ? "  + " ORDER BY " + COLUMN_USER_NAME + " DESC;";
                mTransact = myDB.getAllUsers(sql,Admin);
            }
            if (this.mTransact.getCount() == 0) {
                Toast.makeText(this, "There are no transactions in this list!", Toast.LENGTH_LONG).show();
            } else {
                while (this.mTransact.moveToNext()) {
                    theList.add("User id :" + mTransact.getString(mTransact.getColumnIndex(COLUMN_USER_ID)));
                    theList.add("Username :" + mTransact.getString(mTransact.getColumnIndex(COLUMN_USER_NAME)));
                    theList.add("User email : " + mTransact.getString(mTransact.getColumnIndex(COLUMN_USER_EMAIL)));
                    theList.add("Security question :" + mTransact.getString(mTransact.getColumnIndex(COLUMN_USER_QUESTION)));
                    if (Integer.valueOf(mTransact.getString(mTransact.getColumnIndex(COLUMN_USER_STATUS))) > 0){
                        theList.add("Status :" + " Synced" + "\n\n");
                    }
                    else{
                        theList.add("Status :" + " Not Synced" + "\n\n");
                    }

                    ListAdapter list1Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                    mListView.setAdapter(list1Adapter);
                }
            }
        }
        mTransact.close();
    }


}