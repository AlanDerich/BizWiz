package com.example.derich.bizwiz.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_DAILY_EXPENSE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_DEBTS_PAID;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_OPENING_CASH_SALES;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_RETAIL_SALES;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_DATE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_ID;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_TIME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_USER;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_WHOLESALE_SALES;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_SALES;

public class SalesTransactions extends AppCompatActivity {
    ListView mListView;
    DatabaseHelper myDb;
    Spinner spinnerDate,spinnerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_transactions);
        mListView = findViewById(R.id.listSalesTrans);
        spinnerDate = findViewById(R.id.spinnerDate);
        spinnerUser = findViewById(R.id.spinnerUser);
        myDb = new DatabaseHelper(this);
        populateSpinner();
        populateUserSpinner();

        //populate an ArrayList<String> from the database and then view it

       // long timeMillis = System.currentTimeMillis();

        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String date = spinnerDate.getSelectedItem().toString();
                String user = spinnerUser.getSelectedItem().toString();
                getData(date,user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String date = spinnerDate.getSelectedItem().toString();
                String user = spinnerUser.getSelectedItem().toString();
                getData(date,user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    public void getData(String date, String user){
        String[] param = new String[] {date,user};
        ArrayList<String> theList = new ArrayList<>();
        String sqli = "SELECT DISTINCT sales_time FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        SQLiteDatabase db = myDb.getWritableDatabase();
        Cursor cursorQUery = db.rawQuery(sqli,param);
        //  Cursor transact = myDb.getAllTransactions();
        if(cursorQUery.getCount() == 0){
            Toast.makeText(this, "There are no transactions in this list!", Toast.LENGTH_LONG).show();
        }else{
            while(cursorQUery.moveToNext()){
                String time =cursorQUery.getString(cursorQUery.getColumnIndex(COLUMN_SALES_TIME));
                String[] params = {date,user,time};
                String sql = "SELECT  DISTINCT opening_cash_sales FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " +" AND " + COLUMN_SALES_TIME + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
                Cursor cursor = db.rawQuery(sql,params);
                while (cursor.moveToNext()){
                    String id =cursor.getString(cursor.getColumnIndex(COLUMN_OPENING_CASH_SALES));
                    String[] para = {date,user,time,id};
                    String sqlite = "SELECT DISTINCT opening_cash_sales,wholesale_sales,retail_sales,daily_expense,debts_paid,daily_debt FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? "+ " AND " + COLUMN_SALES_TIME + " = ? " +" AND " + COLUMN_OPENING_CASH_SALES+ " = ? " + " ORDER BY " + COLUMN_SALES_ID + " ASC;";
                    Cursor cur = db.rawQuery(sqlite,para);
                    while (cur.moveToNext()){
                    theList.add("sales id :" + id);
                    theList.add("Opening cash sales :" + cur.getString(cur.getColumnIndex(COLUMN_OPENING_CASH_SALES)));
                    theList.add("Retail Sales :" + cur.getString(cur.getColumnIndex(COLUMN_RETAIL_SALES)));
                    theList.add("Wholesale sales : " + cur.getString(cur.getColumnIndex(COLUMN_WHOLESALE_SALES)));
                    theList.add("Transaction time :" + time);
                    theList.add("Paid debts :" + cur.getString(cur.getColumnIndex(COLUMN_DEBTS_PAID)));
                    theList.add("Expenses :" + cur.getString(cur.getColumnIndex(COLUMN_DAILY_EXPENSE)) + "\n\n");
                ListAdapter list1Adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                mListView.setAdapter(list1Adapter);
                    }
                    cur.close();
                }
                cursor.close();
            }

        }

        cursorQUery.close();
    }
    public static String getDate(long milliseconds){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar vCalendar = Calendar.getInstance();
        vCalendar.setTimeInMillis(milliseconds);
        return sdf.format(vCalendar.getTime());
    }
    public void populateSpinner(){
        ArrayList<String> listPro = myDb.getAllSalesDates();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        spinnerDate.setAdapter(adapter);
    }
    public void populateUserSpinner(){
        ArrayList<String> listPro = myDb.getAllSalesUsers();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        spinnerUser.setAdapter(adapter);
    }
}
