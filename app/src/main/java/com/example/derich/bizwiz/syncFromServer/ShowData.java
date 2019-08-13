package com.example.derich.bizwiz.syncFromServer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;

public class ShowData extends AppCompatActivity {

    DatabaseHelper sqLiteHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    com.example.derich.bizwiz.syncFromServer.ListAdapter listAdapter ;
    ListView LISTVIEW;
    ArrayList<String> ID_Array;
    ArrayList<String> client_NAME_Array;
    ArrayList<String> client_Debt_Array;
    ArrayList<String> client_Number_Array;
    ArrayList<String> client_Email_Array;
    ArrayList<String> ListViewClickItemArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        LISTVIEW = findViewById(R.id.listView1);

        ID_Array = new ArrayList<>();

        client_NAME_Array = new ArrayList<>();
        client_Debt_Array = new ArrayList<>();
        client_Number_Array = new ArrayList<>();
        client_Email_Array = new ArrayList<>();

        sqLiteHelper = new DatabaseHelper(this);

        LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                Toast.makeText(ShowData.this, ListViewClickItemArray.get(position), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onResume() {

        ShowSQLiteDBdata() ;

        super.onResume();
    }

    private void ShowSQLiteDBdata() {

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ DatabaseHelper.TABLE_CLIENT+"", null);

        ID_Array.clear();
        client_NAME_Array.clear();
        client_Debt_Array.clear();
        client_Number_Array.clear();
        client_Email_Array.clear();

        if (cursor.moveToFirst()) {
            do {

                ID_Array.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_ID)));

                //Inserting Column Name into Array to Use at ListView Click Listener Method.
                ListViewClickItemArray.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_FULLNAME)));

                client_NAME_Array.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_FULLNAME)));
                client_Debt_Array.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_DEBT)));
                client_Number_Array.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER)));
                client_Email_Array.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_EMAIL)));


            } while (cursor.moveToNext());
        }

        listAdapter = new ListAdapter(this,

                ID_Array,
                client_NAME_Array,
                client_Debt_Array,
                client_Number_Array,
                client_Email_Array
        );

        LISTVIEW.setAdapter(listAdapter);

        cursor.close();
    }
}
