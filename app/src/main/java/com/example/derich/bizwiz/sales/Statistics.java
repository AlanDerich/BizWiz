package com.example.derich.bizwiz.sales;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;

import static com.example.derich.bizwiz.PreferenceHelper.getUsername;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_DATE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_TIME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_USER;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_SALES;

public class Statistics extends AppCompatActivity {
    Spinner mSpinnerDate,mSpinnerUser;
    TextView textView_stats_Ocash,textView_stats_Wsale,textView_stats_Rsale,textView_stats_expenses,textView_expected_closing_cash,textView_stats_given_debts,textView_stats_debts_paid;
    DatabaseHelper database;
 //   private String Admin = "Admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        mSpinnerDate = findViewById(R.id.spinner_stats_date);
        mSpinnerUser = findViewById(R.id.spinner_stats_user);
        database = new DatabaseHelper(this);
        textView_stats_Ocash = findViewById(R.id.textView_stats_Ocash);
        textView_stats_Wsale = findViewById(R.id.textView_stats_Wsale);
        textView_stats_Rsale = findViewById(R.id.textView_stats_Rsale);
        textView_stats_expenses = findViewById(R.id.textView_stats_expenses);
        textView_stats_given_debts = findViewById(R.id.textView_stats_given_debts);
        textView_stats_debts_paid = findViewById(R.id.textView_stats_debts_paid);
        textView_expected_closing_cash = findViewById(R.id.textView_expected_closing_cash);
        populateSpinner();
        String Administrator = getUsername();
        mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String date = mSpinnerDate.getSelectedItem().toString();
                String user = mSpinnerUser.getSelectedItem().toString();
                getdata(date,user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        populateUserSpinner();
        mSpinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String date = mSpinnerDate.getSelectedItem().toString();
                String user = mSpinnerUser.getSelectedItem().toString();
                getdata(date,user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    public void populateSpinner(){
        ArrayList<String> listPro = database.getAllSalesDates();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        mSpinnerDate.setAdapter(adapter);
    }
    public void populateUserSpinner(){
        ArrayList<String> listPro = database.getAllSalesUsers();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        mSpinnerUser.setAdapter(adapter);
    }




    public void getdata(String date,String user){
        // DataModel dataModel = new DataModel();
        SQLiteDatabase db = database.getWritableDatabase();
        String[] params = new String[] {date,user};
        String sql = "SELECT SUM(opening_cash_sales) FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursor = db.rawQuery(sql,params);
        int opening_cash_sales;
        String sql1 = "SELECT SUM(wholesale_sales) FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursorWholesale = db.rawQuery(sql1,params);
        String sql2 = "SELECT SUM(retail_sales) FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursorRetail = db.rawQuery(sql2,params);
        String sql3 = "SELECT SUM(daily_expense) FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursorExpense = db.rawQuery(sql3,params);
        String sql4 = "SELECT SUM(debts_paid) FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursorPaidDebts = db.rawQuery(sql4,params);
        String sql5 = "SELECT SUM(daily_debt) FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursorGivenDebts = db.rawQuery(sql5,params);

        int wholesale_sales;
        int retail_sales;
        float daily_expense;
        int debts_paid;
        int daily_debt;

        if (cursor.moveToFirst()) {
            opening_cash_sales = cursor.getInt(0);
        } else {
            opening_cash_sales = 0;
        }
        if (cursorWholesale.moveToFirst()) {
            wholesale_sales = cursorWholesale.getInt(0);
        } else {
            wholesale_sales = 0;
        }
        if (cursorRetail.moveToFirst()) {
            retail_sales = cursorRetail.getInt(0);
        } else {
            retail_sales = 0;
        }
        if (cursorExpense.moveToFirst()) {
            daily_expense = cursorExpense.getFloat(0);
        } else {
            daily_expense = 0;
        }
        if (cursorPaidDebts.moveToFirst()) {
            debts_paid = cursorPaidDebts.getInt(0);
        } else {
            debts_paid = 0;
        }
        if (cursorGivenDebts.moveToFirst()) {
            daily_debt = cursorGivenDebts.getInt(0);
        } else {
            daily_debt = 0;
        }
            // stringBuffer.append(dataModel);
            textView_stats_Ocash.setText(String.valueOf(opening_cash_sales));
            textView_stats_Wsale.setText(String.valueOf(wholesale_sales));
            textView_stats_Rsale.setText(String.valueOf(retail_sales));
            textView_stats_debts_paid.setText(String.valueOf(debts_paid));
            textView_stats_expenses.setText(String.valueOf(daily_expense));
            textView_stats_given_debts.setText(String.valueOf(daily_debt));
            Float closingCash = opening_cash_sales +  Integer.valueOf(wholesale_sales) + Integer.valueOf(retail_sales) + Integer.valueOf(debts_paid) - Float.valueOf(daily_expense);
                textView_expected_closing_cash.setText(String.valueOf(closingCash));

    cursor.close();
    }



    }

