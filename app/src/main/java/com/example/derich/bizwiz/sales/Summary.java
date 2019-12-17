package com.example.derich.bizwiz.sales;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.HandlerThread;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_PRODUCT_BUYING_PRICE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_PRODUCT_NAME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_QUANTITY;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_SALES_DATE;
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

public class Summary extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_USERS = 0;
    private static final int LOADER_SALES = 1;
    RecyclerView recyclerSummary;
    SummaryAdapter recycler;
    static Spinner spinnerPeriod,spinnerUser;
    static DatabaseHelper database;
    TextView totalCash,dates;
    String[] period =  {"This month","This Week","Last 1 week","Last Month","This Year","Last Year"};
    private Cursor mUsers;
    private SimpleDateFormat mSdf;
    private Calendar mC;
    public static String startDate;
    public static String mToday;
    private SQLiteDatabase mDb;
    private Cursor mSales;
    static int saleItems = 0;
    static int addItems = 0;
    static float expectCash =0;
    static int expectWholesaleCash =0;
    static int expectRetailCash =0;
    static int remainingItems = 0;
    static float buyingPrice = 0;
    static float expectedTotalCash = 0;
    static int mOpeningSales =0;
    private List<SummaryModel> summaryModel;
    private SummaryModel mSummaryModel;
    private HandlerThread mHandlerThread;

    @Override
    protected void onStart() {
        super.onStart();
        mHandlerThread = new HandlerThread("Database query");
        mHandlerThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandlerThread.quit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        recyclerSummary = findViewById(R.id.recyclerViewSummary);
        spinnerPeriod = findViewById(R.id.spinnerSummary);
        spinnerUser = findViewById(R.id.spinnerSummaryUser);
        totalCash = findViewById(R.id.textView21);
        dates = findViewById(R.id.textView23);
        database = new DatabaseHelper(this);
        mDb = database.getReadableDatabase();
        summaryModel = new ArrayList<>();
        mSdf = new SimpleDateFormat("yyyy/MM/dd");
        mC = Calendar.getInstance();

        CustomAdapter customAdapter = new CustomAdapter(this,period);
        spinnerPeriod.setAdapter(customAdapter);
        populateUsers();
        populateDates();
        populateRecyclerView();
        spinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateDates();
              populateRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Summary.this,"No records for the user are available during this period",Toast.LENGTH_LONG).show();

            }
        });
        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateDates();
                populateRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Summary.this,"No user available",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateDates() {
        if (spinnerPeriod.getSelectedItem().toString().equals("This month")){
            mC = Calendar.getInstance();
            mC.set(Calendar.DAY_OF_MONTH,1);
            startDate = mSdf.format(mC.getTime());
            mC = Calendar.getInstance();
            mToday = mSdf.format(mC.getTime());
            dates.setText("Showing sales from " + startDate + " to " + mToday);
        }
        else if (spinnerPeriod.getSelectedItem().toString().equals("This Week")){
            mC = Calendar.getInstance();
            mC.set(Calendar.DAY_OF_WEEK,1);
            startDate = mSdf.format(mC.getTime());
            mC = Calendar.getInstance();
            mToday = mSdf.format(mC.getTime());
            dates.setText("Showing sales from " + startDate + " to " + mToday);
        }
        else if (spinnerPeriod.getSelectedItem().toString().equals("This Year")){
            mC = Calendar.getInstance();
            mC.set(Calendar.DAY_OF_YEAR,1);
            startDate = mSdf.format(mC.getTime());
            mC = Calendar.getInstance();
            mToday = mSdf.format(mC.getTime());
            dates.setText("Showing sales from " + startDate + " to " + mToday);
        }
        else if (spinnerPeriod.getSelectedItem().toString().equals("Last 1 week")){
            mC = Calendar.getInstance();
            mC.add(Calendar.WEEK_OF_YEAR,-1);
            mC.set(Calendar.DAY_OF_WEEK,1);
            startDate = mSdf.format(mC.getTime());
            mC = Calendar.getInstance();
            mC.add(Calendar.WEEK_OF_YEAR,-1);
            mC.set(Calendar.DAY_OF_WEEK,mC.getActualMaximum(Calendar.DAY_OF_WEEK));
            mToday = mSdf.format(mC.getTime());
            dates.setText("Showing sales from " + startDate + " to " + mToday);
        }
        else if (spinnerPeriod.getSelectedItem().toString().equals("Last Month")){
            mC = Calendar.getInstance();
            mC.add(Calendar.MONTH,-1);
            mC.set(Calendar.DAY_OF_MONTH,1);
            startDate = mSdf.format(mC.getTime());
            mC = Calendar.getInstance();
            mC.add(Calendar.MONTH,-1);
            mC.set(Calendar.DAY_OF_MONTH,mC.getActualMaximum(Calendar.DAY_OF_MONTH));
            mToday = mSdf.format(mC.getTime());
            dates.setText("Showing sales from " + startDate + " to " + mToday);
        }
        else if (spinnerPeriod.getSelectedItem().toString().equals("Last Year")){
            mC = Calendar.getInstance();
            mC.add(Calendar.YEAR,-1);
            mC.set(Calendar.DAY_OF_YEAR,1);
            startDate = mSdf.format(mC.getTime());
            mC = Calendar.getInstance();
            mC.add(Calendar.YEAR,-1);
            mC.set(Calendar.DAY_OF_YEAR,mC.getActualMaximum(Calendar.DAY_OF_YEAR));
            mToday = mSdf.format(mC.getTime());
            dates.setText("Showing sales from " + startDate + " to " + mToday);
        }
        else {
            dates.setText("No valid date detected ");
        }
    }

    private void populateRecyclerView() {
        if (spinnerUser.getSelectedItem() != null){
            getLoaderManager().restartLoader(LOADER_SALES,null, this);
          //  getLoaderManager().initLoader(LOADER_SALES,null,this);
        }
        else {
            Toast.makeText(this,"No user selected!",Toast.LENGTH_LONG).show();
        }
    }

    private void populateUsers() {
        getLoaderManager().restartLoader(LOADER_USERS,null, this);
        getLoaderManager().initLoader(LOADER_USERS,null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Loader<Cursor> loader = null;
        if (id == LOADER_USERS){
            loader = getUsers();
        }
        else if (id == LOADER_SALES){
            loader = getSales();
        }
        return loader;
    }

    private CursorLoader getSales() {
        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {
                String[] arrgs = {startDate, mToday};
                String selectQuery = "SELECT DISTINCT " + REPORT_DATE + " FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " BETWEEN " + " ? " + " AND " + " ? " + " ORDER BY " + REPORT_DATE + " ASC;";
                Cursor data = mDb.rawQuery(selectQuery, arrgs);
                List<SummaryModel> listSummary=new ArrayList<>();
                StringBuffer stringBuffer = new StringBuffer();
                if (data.getCount() > 0) {
                    while (data.moveToNext()) {
                        String transactionUser = spinnerUser.getSelectedItem().toString();
                        String dateOfTransaction = data.getString(data.getColumnIndex("report_date"));
                        String[] params = {dateOfTransaction, transactionUser};
                        String sqlProduct = "SELECT DISTINCT report_product FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_USER + " = ? " + " ORDER BY " + REPORT_DATE + " DESC;";
                        Cursor cursorProduct = mDb.rawQuery(sqlProduct, params);
                       /* if (cursorProduct.moveToFirst()) {
                            do {
                                String sqlOpeningTime = "SELECT DISTINCT sales_time FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " +" AND " + COLUMN_SALES_USER + " = ? " + " ORDER BY " + COLUMN_SALES_DATE + " DESC;";
                                Cursor cursorOpeningTime = mDb.rawQuery(sqlOpeningTime,params);
                                StringBuffer stringBuffer = new StringBuffer();
                                if (cursorOpeningTime.moveToFirst()){
                                    do{
                                        String oCash = cursorOpeningTime.getString(cursorOpeningTime.getColumnIndexOrThrow(COLUMN_SALES_TIME));
                                        String[] parameter = new String[] {dateOfTransaction, transactionUser,oCash};
                                        String sqlOpening = "SELECT DISTINCT opening_cash_sales FROM " + TABLE_SALES + " WHERE " + COLUMN_SALES_DATE + " = ? " +" AND " + COLUMN_SALES_USER + " = ? "+" AND " + COLUMN_SALES_TIME + " = ? " + " ORDER BY " + COLUMN_SALES_DATE + " DESC;";
                                        Cursor cursorOpening = mDb.rawQuery(sqlOpening,parameter);
                                        if (cursorOpening.moveToFirst()){
                                            if (dateOfTransaction == startDate) {
                                                int openingSales = Integer.valueOf(cursorOpening.getString(cursorOpening.getColumnIndexOrThrow(COLUMN_OPENING_CASH_SALES)));
                                                mOpeningSales = mOpeningSales + openingSales;
                                            }
                                        }
                                    }while (cursorOpeningTime.moveToNext());

                                }*/
                        if (cursorProduct.moveToFirst()){
                            do{
                                String productName = cursorProduct.getString(cursorProduct.getColumnIndexOrThrow(REPORT_PRODUCT));
                                mSummaryModel = new SummaryModel();
                                String[] param = new String[] {dateOfTransaction,transactionUser,productName};
                                String sqli = "SELECT report_sold_items FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_USER + " = ? "+ " AND " + REPORT_PRODUCT + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
                                Cursor cursorQUery = mDb.rawQuery(sqli,param);
                                while(cursorQUery.moveToNext()){
                                    String soldItems = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(REPORT_SOLD_ITEMS));
                                    saleItems = Integer.valueOf(soldItems) + saleItems;
                                }

                                String sqliAdded = "SELECT report_added_items FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_USER + " = ? "+ " AND " + REPORT_PRODUCT + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
                                Cursor cursorAdded = mDb.rawQuery(sqliAdded,param);
                                while(cursorAdded.moveToNext()){
                                    String addedItems = cursorAdded.getString(cursorAdded.getColumnIndexOrThrow(REPORT_ADDED_ITEMS));
                                    addItems = Integer.valueOf(addedItems) + addItems;
                                }

                                String sqliExpectedWholesale = "SELECT report_wholesale_sales FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_USER + " = ? "+ " AND " + REPORT_PRODUCT + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
                                Cursor cursorExpectedWholesale = mDb.rawQuery(sqliExpectedWholesale,param);
                                while(cursorExpectedWholesale.moveToNext()){
                                    String expectedWholesaleCash = cursorExpectedWholesale.getString(cursorExpectedWholesale.getColumnIndexOrThrow(REPORT_WHOLESALE_SALES));
                                    expectWholesaleCash = Integer.valueOf(expectedWholesaleCash) + expectWholesaleCash;
                                }
                                String sqliExpectedRetail = "SELECT report_retail_sales FROM " + TABLE_REPORT + " WHERE " + REPORT_DATE + " = ? " + " AND " + REPORT_USER + " = ? "+ " AND " + REPORT_PRODUCT + " = ? " + " ORDER BY " + REPORT_DATE + " ASC;";
                                Cursor cursorExpectedRetail = mDb.rawQuery(sqliExpectedRetail,param);
                                while(cursorExpectedRetail.moveToNext()){
                                    String expectedRetailCash = cursorExpectedRetail.getString(cursorExpectedRetail.getColumnIndexOrThrow(REPORT_RETAIL_SALES));
                                    expectRetailCash = Integer.valueOf(expectedRetailCash) + expectRetailCash;
                                }
                                String[] para = new String[] {productName};
                                String sqliBuyingPrice = "SELECT product_buying_price FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_NAME + " = ? " + " ORDER BY " + COLUMN_PRODUCT_NAME + " ASC;";
                                Cursor cursorBP = mDb.rawQuery(sqliBuyingPrice,para);
                                while(cursorBP.moveToNext()){
                                    String itemPrice = cursorBP.getString(cursorBP.getColumnIndexOrThrow(COLUMN_PRODUCT_BUYING_PRICE));
                                    buyingPrice = Float.valueOf(itemPrice);
                                }
                                float expense = addItems * buyingPrice;

                                expectCash = expectRetailCash + expectWholesaleCash - expense;

                                String sqliRemainingItems = "SELECT product_quantity FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_NAME + " = ? " + " ORDER BY " + COLUMN_PRODUCT_NAME + " ASC;";
                                Cursor cursorRemainingItems = mDb.rawQuery(sqliRemainingItems,para);
                                while(cursorRemainingItems.moveToNext()){
                                    String remainItems = cursorRemainingItems.getString(cursorRemainingItems.getColumnIndexOrThrow(COLUMN_QUANTITY));
                                    remainingItems = Integer.valueOf(remainItems) + remainingItems;
                                }
                                mSummaryModel.setProductName(productName);
                                mSummaryModel.setQuantitySold(saleItems);
                                mSummaryModel.setRetailSales(expectRetailCash);
                                mSummaryModel.setWholesaleSales(expectWholesaleCash);
                                mSummaryModel.setAdded(addItems);
                                mSummaryModel.setExpectedCash(expectCash);
                                stringBuffer.append(mSummaryModel);
                                listSummary.add(mSummaryModel);
                                expectedTotalCash = (expectWholesaleCash + expectRetailCash - expense) + expectedTotalCash;
                                saleItems = 0;
                                addItems = 0;
                                expectCash =0;
                                expectWholesaleCash =0;
                                expectRetailCash =0;
                                remainingItems=0;
                                buyingPrice = 0;
                                cursorAdded.close();
                                cursorBP.close();
                                cursorExpectedRetail.close();
                                cursorExpectedWholesale.close();
                                cursorRemainingItems.close();

                            }
                            while (cursorProduct.moveToNext());
                        }
                        cursorProduct.close();
                    }
                }
                summaryModel= listSummary;
                return data;
            }
        };
    }

    private CursorLoader getUsers() {
        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {
                String sql = "SELECT DISTINCT " + COLUMN_SALES_USER + " FROM "+ TABLE_SALES + " ORDER BY " + COLUMN_SALES_DATE + " ASC;";
                return mDb.rawQuery(sql, null);
            }
        };

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_USERS)
            loadFinishedUsers(data);
        else if (loader.getId()==LOADER_SALES)
            loadFinishedSales(data);

    }

    private void loadFinishedSales(Cursor data) {
        recycler =new SummaryAdapter(summaryModel);
        RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerSummary.setLayoutManager(reLayoutManager);
        recyclerSummary.setItemAnimator(new DefaultItemAnimator());
        recyclerSummary.setAdapter(recycler);
        totalCash.setText(String.valueOf(expectedTotalCash + mOpeningSales));
        expectedTotalCash = 0;
        mOpeningSales = 0;
    }

    private void loadFinishedUsers(Cursor data) {
        mUsers = data;
        ArrayList<String> list = new ArrayList<>();
        if (mUsers.getCount() > 0) {
            while (mUsers.moveToNext()) {
                String dateOfTransaction = mUsers.getString(mUsers.getColumnIndex("sales_user"));
                list.add(dateOfTransaction);
            }
        }
        ArrayList<String> listPro = list;
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        spinnerUser.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == LOADER_USERS){
            if (mUsers != null){
                mUsers.close();
            }
        }
        if (loader.getId() == LOADER_SALES){
            if (mSales != null){
                mSales.close();
            }
        }
    }
}
