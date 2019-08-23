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

import static com.example.derich.bizwiz.sql.DatabaseHelper.REPORT_DATE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.REPORT_PRODUCT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.REPORT_USER;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_REPORT;

public class DailyReport extends AppCompatActivity {
    TextView wholesaleSales,retailSales, expectedCashe,addedItems,remainingItems,debtSales, soldItems;
    Spinner productSpinner, userSpinner, dateSpinner;
    DatabaseHelper database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        wholesaleSales = findViewById(R.id.textView_daily_report_wholesale);
        retailSales = findViewById(R.id.textView_daily_report_retail);
        expectedCashe = findViewById(R.id.textView_daily_report_expected_cash);
        addedItems = findViewById(R.id.textView_daily_report_added_items);
        remainingItems = findViewById(R.id.textView_daily_report_remaining_items);
        soldItems = findViewById(R.id.textView_report_sold_items);
        debtSales = findViewById(R.id.textView_debt_sales);
        productSpinner= findViewById(R.id.spinner_daily_report_product);
        userSpinner= findViewById(R.id.spinner_daily_report_user);
        dateSpinner= findViewById(R.id.spinner_daily_report_date);
        database = new DatabaseHelper(this);
        populateDateSpinner();
        populateProductSpinner();
        populateUserSpinner();
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String date = dateSpinner.getSelectedItem().toString();
                String product = productSpinner.getSelectedItem().toString();
                String user = userSpinner.getSelectedItem().toString();
                getdata(date,product,user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String date = dateSpinner.getSelectedItem().toString();
                String product = productSpinner.getSelectedItem().toString();
                String user = userSpinner.getSelectedItem().toString();
                getdata(date,product,user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String date = dateSpinner.getSelectedItem().toString();
                String product = productSpinner.getSelectedItem().toString();
                String user = userSpinner.getSelectedItem().toString();
                getdata(date,product,user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void populateProductSpinner(){
        ArrayList<String> listPro = database.getAllReportProducts();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        productSpinner.setAdapter(adapter);
    }
    public void populateUserSpinner(){
        ArrayList<String> listPro = database.getAllSalesUsers();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        userSpinner.setAdapter(adapter);
    }
    public void populateDateSpinner(){
        ArrayList<String> listPro = database.getAllSalesDates();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        dateSpinner.setAdapter(adapter);
    }
    public void getdata(String date,String product,String user){
        SQLiteDatabase db = database.getWritableDatabase();
        String[] params = new String[] {date,product,user};
        String sql = "SELECT SUM(report_wholesale_sales) FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_PRODUCT + " = ? " + " AND " + REPORT_USER + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
        Cursor cursorWholesale = db.rawQuery(sql,params);
        String sql1 = "SELECT SUM(report_retail_sales) FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_PRODUCT + " = ? " + " AND " + REPORT_USER + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
        Cursor cursorRetail = db.rawQuery(sql1,params);
        String sql2 = "SELECT SUM(report_debt_sales) FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_PRODUCT + " = ? " + " AND " + REPORT_USER + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
        Cursor cursorDebt = db.rawQuery(sql2,params);
        String sql3 = "SELECT SUM(report_added_items) FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_PRODUCT + " = ? " + " AND " + REPORT_USER + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
        Cursor cursorAdded = db.rawQuery(sql3,params);
        String sql4 = "SELECT SUM(report_sold_items) FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_PRODUCT + " = ? " + " AND " + REPORT_USER + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
        Cursor cursorSoldItems = db.rawQuery(sql4,params);
        int previousBal=database.previousBal(product);
/*
*
    public static final String REPORT_WHOLESALE_SALES = "report_wholesale_sales";
    public static final String REPORT_RETAIL_SALES = "report_retail_sales";
    public static final String REPORT_DEBT_SALES = "report_debt_sales";
    public static final String REPORT_ADDED_ITEMS = "report_added_items";
    public static final String REPORT_STATUS = "report_status";
* */
        int wholesale_sales;
        int retail_sales;
        int debt_sales;
        int added_products;
        int sold_items;

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
        if (cursorDebt.moveToFirst()) {
            debt_sales = cursorDebt.getInt(0);
        } else {
            debt_sales = 0;
        }
        if (cursorAdded.moveToFirst()) {
            added_products = cursorAdded.getInt(0);
        } else {
            added_products = 0;
        }
        if (cursorSoldItems.moveToFirst()) {
            sold_items = cursorSoldItems.getInt(0);
        } else {
            sold_items = 0;
        }
        // stringBuffer.append(dataModel);
        wholesaleSales.setText(String.valueOf(wholesale_sales));
        retailSales.setText(String.valueOf(retail_sales));
        debtSales.setText(String.valueOf(debt_sales));
        addedItems.setText(String.valueOf(added_products));
        soldItems.setText(String.valueOf(sold_items));
        int expectedCash = wholesale_sales +  retail_sales;
        expectedCashe.setText(String.valueOf(expectedCash));
        remainingItems.setText(String.valueOf(previousBal));

        cursorWholesale.close();
        cursorAdded.close();
        cursorDebt.close();
        cursorRetail.close();
        cursorSoldItems.close();
    }
}
