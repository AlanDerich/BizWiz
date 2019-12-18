package com.example.derich.bizwiz.sales;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;

import static com.example.derich.bizwiz.PreferenceHelper.getUsername;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_DAILY_DEBT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_DAILY_EXPENSE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_DEBTS_PAID;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_OPENING_CASH_SALES;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_RETAIL_SALES;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_DATE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_TIME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_USER;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_WHOLESALE_SALES;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_SALES;

public class Statistics extends AppCompatActivity {
    Spinner mSpinnerDate,mSpinnerUser;
    TextView textView_stats_Ocash,textView_stats_Wsale,textView_stats_Rsale,textView_stats_expenses,textView_expected_closing_cash,textView_stats_given_debts,textView_stats_debts_paid;
    DatabaseHelper database;
    float openingCash = 0;
    float wholesaleSales = 0;
    float retail_sales =0;
    float daily_expense =0;
    float debts_paid =0;
    float daily_debt =0;
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
        populateUserSpinner();
        String Administrator = getUsername();
       if (mSpinnerDate.getSelectedItem() != null && mSpinnerUser.getSelectedItem() != null){
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
       }
       if (mSpinnerUser.getSelectedItem() != null && mSpinnerDate.getSelectedItem() != null) {
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
    }



    public void populateSpinner(){
        ArrayList<String> listPro = database.getAllSalesDates();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        mSpinnerDate.setAdapter(adapter);
    }
    public void populateUserSpinner(){

        if (PreferenceHelper.getUsername().equals("Admin"))
        {
            String mSelectQuery = "SELECT DISTINCT " + COLUMN_SALES_USER + " FROM " + TABLE_SALES;
            ArrayList<String> listPro = database.getAllSalesUsers(mSelectQuery);
            ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
            mSpinnerUser.setAdapter(adapter);
        }
        else {  ArrayList<String> listPro = new ArrayList<>();
            listPro.add(PreferenceHelper.getUsername());
            ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
            mSpinnerUser.setAdapter(adapter);
        }
    }




    public void getdata(String date,String user){
        // DataModel dataModel = new DataModel();
        SQLiteDatabase db = database.getWritableDatabase();
        String[] params = new String[] {date,user};
        String sql = "SELECT opening_cash_sales FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursor = db.rawQuery(sql,params);
        String sql1 = "SELECT wholesale_sales FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursorWholesale = db.rawQuery(sql1,params);
        String sql2 = "SELECT retail_sales FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursorRetail = db.rawQuery(sql2,params);
        String sql3 = "SELECT daily_expense FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursorExpense = db.rawQuery(sql3,params);
        String sql4 = "SELECT debts_paid FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursorPaidDebts = db.rawQuery(sql4,params);
        String sql5 = "SELECT daily_debt FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_TIME + " ASC;";
        Cursor cursorGivenDebts = db.rawQuery(sql5,params);

        int wholesale_sales;

        if (cursor.moveToFirst()) {
           // opening_cash_sales = cursor.getInt(0);
            String[] param = new String[] {date,user};
            String sqli = "SELECT DISTINCT sales_time,opening_cash_sales FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_OPENING_CASH_SALES + " ASC;";
            Cursor cursorQUery = db.rawQuery(sqli,param);
            while(cursorQUery.moveToNext()){
                String openCash = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_OPENING_CASH_SALES));
                openingCash = Float.valueOf(openCash) + openingCash;
            }
            cursorQUery.close();
        } else {
            openingCash = openingCash;
        }
        if (cursorWholesale.moveToFirst()) {
            //wholesale_sales = cursorWholesale.getInt(0);
            String[] param = new String[] {date,user};
            String sqli = "SELECT DISTINCT sales_time,wholesale_sales FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_WHOLESALE_SALES + " ASC;";
            Cursor cursorQUery = db.rawQuery(sqli,param);
            while(cursorQUery.moveToNext()){
                String wholesale = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_WHOLESALE_SALES));
                wholesaleSales = Float.valueOf(wholesale) + wholesaleSales;
            }
            cursorQUery.close();
        } else {
           wholesaleSales = wholesaleSales;
        }
        if (cursorRetail.moveToFirst()) {
           // retail_sales = cursorRetail.getInt(0);
            String[] param = new String[] {date,user};
            String sqli = "SELECT DISTINCT sales_time,retail_sales FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_RETAIL_SALES + " ASC;";
            Cursor cursorQUery = db.rawQuery(sqli,param);
            while(cursorQUery.moveToNext()){
                String retail = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_RETAIL_SALES));
                retail_sales = Float.valueOf(retail) + retail_sales;
            }
            cursorQUery.close();
        } else {
            retail_sales = retail_sales;
        }
        if (cursorExpense.moveToFirst()) {
            String[] param = new String[] {date,user};
            String sqli = "SELECT DISTINCT sales_time,daily_expense FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_DAILY_EXPENSE + " ASC;";
            Cursor cursorQUery = db.rawQuery(sqli,param);
            while(cursorQUery.moveToNext()){
                String expense = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_DAILY_EXPENSE));
                daily_expense = Float.valueOf(expense) + daily_expense;
            }
            cursorQUery.close();
           // daily_expense = cursorExpense.getFloat(0);
        } else {
            daily_expense = daily_expense;
        }
        if (cursorPaidDebts.moveToFirst()) {
            String[] param = new String[] {date,user};
            String sqli = "SELECT DISTINCT sales_time,debts_paid FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_DEBTS_PAID + " ASC;";
            Cursor cursorQUery = db.rawQuery(sqli,param);
            while(cursorQUery.moveToNext()){
                String paidDebts = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_DEBTS_PAID));
                debts_paid = Float.valueOf(paidDebts) + debts_paid;
            }
            cursorQUery.close();
          //  debts_paid = cursorPaidDebts.getInt(0);
        } else {
            debts_paid = debts_paid;
        }
        if (cursorGivenDebts.moveToFirst()) {
            String[] param = new String[] {date,user};
            String sqli = "SELECT DISTINCT sales_time,daily_debt FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " + " AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_DAILY_DEBT+ " ASC;";
            Cursor cursorQUery = db.rawQuery(sqli,param);
            while(cursorQUery.moveToNext()){
                String debtsGiven = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_DAILY_DEBT));
                daily_debt = Float.valueOf(debtsGiven) + daily_debt;
            }
            cursorQUery.close();
           // daily_debt = cursorGivenDebts.getInt(0);
        } else {
            daily_debt = daily_debt;
        }
            // stringBuffer.append(dataModel);
           // textView_stats_Ocash.setText(String.valueOf(opening_cash_sales));
            textView_stats_Ocash.setText(String.valueOf(openingCash));
            textView_stats_Wsale.setText(String.valueOf(wholesaleSales));
            textView_stats_Rsale.setText(String.valueOf(retail_sales));
            textView_stats_debts_paid.setText(String.valueOf(debts_paid));
            textView_stats_expenses.setText(String.valueOf(daily_expense));
            textView_stats_given_debts.setText(String.valueOf(daily_debt));
            Float closingCash = openingCash + wholesaleSales + retail_sales + debts_paid - daily_expense;
                textView_expected_closing_cash.setText(String.valueOf(closingCash));
        openingCash = 0;
        wholesaleSales = 0;
        retail_sales = 0;
        debts_paid = 0;
        daily_expense = 0;
        daily_debt = 0;
    cursor.close();
    cursorExpense.close();
    cursorGivenDebts.close();
    cursorPaidDebts.close();
    cursorRetail.close();
    cursorWholesale.close();
    }
}
