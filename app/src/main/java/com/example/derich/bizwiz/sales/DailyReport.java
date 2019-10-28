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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_OPENING_CASH_SALES;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_PRODUCT_BUYING_PRICE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_PRODUCT_NAME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_QUANTITY;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_DATE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_TIME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_USER;
import static com.example.derich.bizwiz.sql.DatabaseHelper.REPORT_ADDED_ITEMS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.REPORT_DATE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.REPORT_PRODUCT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.REPORT_RETAIL_SALES;
import static com.example.derich.bizwiz.sql.DatabaseHelper.REPORT_SOLD_ITEMS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.REPORT_USER;
import static com.example.derich.bizwiz.sql.DatabaseHelper.REPORT_WHOLESALE_SALES;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_PRODUCTS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_REPORT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_SALES;

public class DailyReport extends AppCompatActivity {
  //  TextView wholesaleSales,retailSales, expectedCashe,addedItems,remainingItems,debtSales, soldItems;
    Spinner  userSpinner, dateSpinner;
    TextView totalCash, openingCash;
    DatabaseHelper database;
    private List<ReportModel> reportModel;
    ReportAdapter recycler;
    private RecyclerView recyclerView;
    int saleItems = 0;
    int addItems = 0;
    float expectCash =0;
    int expectWholesaleCash =0;
    int expectRetailCash =0;
    int remainingItems = 0;
    float buyingPrice = 0;
    float expectedTotalCash = 0;
    int mOpeningSales =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        userSpinner= findViewById(R.id.spinner_daily_report_user);
        dateSpinner= findViewById(R.id.spinner_daily_report_date);
        totalCash = findViewById(R.id.daily_report_total_cash);
        openingCash = findViewById(R.id.daily_report_opening_cash);
        recyclerView = findViewById(R.id.recyclerReport);
        database = new DatabaseHelper(this);
        reportModel =new ArrayList<>();
        populateDateSpinner();
        populateUserSpinner();
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void populateUserSpinner(){
      if (PreferenceHelper.getUsername().equals("Admin"))
      {
          ArrayList<String> listPro = database.getAllReportsUsers();
          ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
          userSpinner.setAdapter(adapter);
      }
      else {  ArrayList<String> listPro = new ArrayList<>();
        listPro.add(PreferenceHelper.getUsername());
          ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
          userSpinner.setAdapter(adapter);
      }

    }
    public void populateDateSpinner(){
        ArrayList<String> listPro = database.getAllReportsDates();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        dateSpinner.setAdapter(adapter);
    }

    public void populateRecyclerView(){
        String date = dateSpinner.getSelectedItem().toString();
        String user = userSpinner.getSelectedItem().toString();
        reportModel = getdata(date,user);
        recycler =new ReportAdapter(reportModel);
        RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);
    }
    public List<ReportModel> getdata(String date, String user){
        // DataModel dataModel = new DataModel();
        List<ReportModel> data=new ArrayList<>();
        SQLiteDatabase db = database.getWritableDatabase();
        String[] params = new String[] {date, user};
        String sql = "SELECT DISTINCT report_product FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " +" AND " + REPORT_USER + " = ? " + " ORDER BY " + REPORT_DATE + " DESC;";
        Cursor cursor = db.rawQuery(sql,params);
        String sqlOpeningTime = "SELECT DISTINCT sales_time FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " +" AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_DATE + " DESC;";
        Cursor cursorOpeningTime = db.rawQuery(sqlOpeningTime,params);
        StringBuffer stringBuffer = new StringBuffer();
        ReportModel reportModel;
        if (cursorOpeningTime.moveToFirst()){
            do{
            String oCash = cursorOpeningTime.getString(cursorOpeningTime.getColumnIndexOrThrow(COLUMN_SALES_TIME));
            String[] parameter = new String[] {date, user,oCash};
            String sqlOpening = "SELECT DISTINCT opening_cash_sales FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " +" AND " + COLUMN_SALES_USER + " = ? "+" AND " + COLUMN_SALES_TIME + " = ? " + " ORDER BY " + COLUMN_SALES_DATE + " DESC;";
            Cursor cursorOpening = db.rawQuery(sqlOpening,parameter);
            if (cursorOpening.moveToFirst()){
                int openingSales = Integer.valueOf(cursorOpening.getString(cursorOpening.getColumnIndexOrThrow(COLUMN_OPENING_CASH_SALES)));
                mOpeningSales = mOpeningSales + openingSales;

            }
        }while (cursorOpeningTime.moveToNext());

        }
        if (cursor.moveToFirst()){
            do{
            String productName = cursor.getString(cursor.getColumnIndexOrThrow(REPORT_PRODUCT));
            reportModel= new ReportModel();
            String[] param = new String[] {date,user,productName};
            String sqli = "SELECT report_sold_items FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_USER + " = ? "+ " AND " + REPORT_PRODUCT + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
            Cursor cursorQUery = db.rawQuery(sqli,param);
            while(cursorQUery.moveToNext()){
                String soldItems = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(REPORT_SOLD_ITEMS));
                saleItems = Integer.valueOf(soldItems) + saleItems;
            }

            String sqliAdded = "SELECT report_added_items FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_USER + " = ? "+ " AND " + REPORT_PRODUCT + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
            Cursor cursorAdded = db.rawQuery(sqliAdded,param);
            while(cursorAdded.moveToNext()){
                String addedItems = cursorAdded.getString(cursorAdded.getColumnIndexOrThrow(REPORT_ADDED_ITEMS));
                addItems = Integer.valueOf(addedItems) + addItems;
            }

            String sqliExpectedWholesale = "SELECT report_wholesale_sales FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_USER + " = ? "+ " AND " + REPORT_PRODUCT + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
            Cursor cursorExpectedWholesale = db.rawQuery(sqliExpectedWholesale,param);
            while(cursorExpectedWholesale.moveToNext()){
                String expectedWholesaleCash = cursorExpectedWholesale.getString(cursorExpectedWholesale.getColumnIndexOrThrow(REPORT_WHOLESALE_SALES));
                expectWholesaleCash = Integer.valueOf(expectedWholesaleCash) + expectWholesaleCash;
            }
            String sqliExpectedRetail = "SELECT report_retail_sales FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_USER + " = ? "+ " AND " + REPORT_PRODUCT + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
            Cursor cursorExpectedRetail = db.rawQuery(sqliExpectedRetail,param);
            while(cursorExpectedRetail.moveToNext()){
                String expectedRetailCash = cursorExpectedRetail.getString(cursorExpectedRetail.getColumnIndexOrThrow(REPORT_RETAIL_SALES));
                expectRetailCash = Integer.valueOf(expectedRetailCash) + expectRetailCash;
            }
                String[] para = new String[] {productName};
                String sqliBuyingPrice = "SELECT product_buying_price FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_NAME + " = ? " + " ORDER BY " + COLUMN_PRODUCT_NAME + " ASC;";
                Cursor cursorBP = db.rawQuery(sqliBuyingPrice,para);
                while(cursorBP.moveToNext()){
                    String remainItems = cursorBP.getString(cursorBP.getColumnIndexOrThrow(COLUMN_PRODUCT_BUYING_PRICE));
                    buyingPrice = Float.valueOf(remainItems);
                }
                float expense = addItems * buyingPrice;

            expectCash = expectRetailCash + expectWholesaleCash - expense;

            String sqliRemainingItems = "SELECT product_quantity FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_NAME + " = ? " + " ORDER BY " + COLUMN_PRODUCT_NAME + " ASC;";
            Cursor cursorRemainingItems = db.rawQuery(sqliRemainingItems,para);
            while(cursorRemainingItems.moveToNext()){
                String remainItems = cursorRemainingItems.getString(cursorRemainingItems.getColumnIndexOrThrow(COLUMN_QUANTITY));
                remainingItems = Integer.valueOf(remainItems) + remainingItems;
            }

            reportModel.setProductName(productName);
            reportModel.setSoldItems(saleItems);
            reportModel.setAddedItems(addItems);
            reportModel.setExpectedCash(expectCash);
            reportModel.setRemainingItems(Integer.valueOf(remainingItems));
            stringBuffer.append(reportModel);
            // stringBuffer.append(dataModel);
            data.add(reportModel);
            expectedTotalCash = (expectWholesaleCash + expectRetailCash - expense) + expectedTotalCash;
            saleItems = 0;
            addItems = 0;
            expectCash =0;
            expectWholesaleCash =0;
            expectRetailCash =0;
            remainingItems=0;
            buyingPrice = 0;
        }
            while (cursor.moveToNext());
        }


        //
        openingCash.setText(String.valueOf(mOpeningSales));
        totalCash.setText(String.valueOf(expectedTotalCash + mOpeningSales));
        expectedTotalCash = 0;
        mOpeningSales = 0;
        return data;

    }

}
