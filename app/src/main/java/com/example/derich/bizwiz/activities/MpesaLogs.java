package com.example.derich.bizwiz.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.mpesa.DataModel;
import com.example.derich.bizwiz.mpesa.MpesaAdapter;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_ADDED_CASH;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_ADDED_FLOAT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_CLOSING_CASH;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_COMMENT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_MPESA_STATUS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_OPENING_CASH;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_OPENING_FLOAT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_REDUCTED_CASH;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_REDUCTED_FLOAT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.DATE_MILLIS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_MPESA;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TIME_OF_TRANSACTION;

public class MpesaLogs extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int LOADER_TRANSACTION_DATES = 0;
    public static final int LOADER_TRANSACTIONS = 1;
    DatabaseHelper database;
    SQLiteDatabase db ;
    RecyclerView recyclerView;
    MpesaAdapter recycler;
    Spinner spinnerLogs;
    List<DataModel> datamodel;
    private Cursor mDates,mTransactions;
    private String mStatus;
    private String[] mParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa_logs);
        Intent intent = getIntent();
        mStatus = intent.getStringExtra("status");
        datamodel =new ArrayList<>();
        recyclerView =  findViewById(R.id.recyclerView_log);
        database = new DatabaseHelper(MpesaLogs.this);
        db = database.getWritableDatabase();
        spinnerLogs = findViewById(R.id.spinner_log);
        populateSpinner();
        if (spinnerLogs.getSelectedItem() != null){
        populateRecyclerView();}
        else{
            Toast.makeText(this,"Please select a date.",Toast.LENGTH_SHORT).show();
        }
        spinnerLogs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void populateSpinner(){
       getLoaderManager().initLoader(LOADER_TRANSACTION_DATES,null,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOADER_TRANSACTION_DATES, null, this);
            populateSpinner();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getLoaderManager().restartLoader(LOADER_TRANSACTION_DATES, null, this);
        populateSpinner();
    }

    public void populateRecyclerView(){
        getLoaderManager().restartLoader(LOADER_TRANSACTIONS, null, this);
        getLoaderManager().initLoader(LOADER_TRANSACTIONS,null,this);



    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (id == LOADER_TRANSACTION_DATES)
            if (mStatus == null){
                loader = getMpesaDates();
            }
            else {
                loader = getUnsyncedMpesaDates();
            }

        else if (id == LOADER_TRANSACTIONS){
            if (mStatus == null){
                loader = getTransactions();
            }
            else {
                loader = getUnsyncedTransactions();
            }
        }
        return loader;
    }

    private CursorLoader getTransactions() {
        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {
                String date = spinnerLogs.getSelectedItem().toString();
                String[] params = new String[] {date};
                String sql = "SELECT DISTINCT time_of_transaction FROM " + TABLE_MPESA + " WHERE " + DATE_MILLIS + " = ? " + " ORDER BY " + TIME_OF_TRANSACTION + " ASC;";
                return db.rawQuery(sql,params);
            }
        };
    }
    private CursorLoader getUnsyncedTransactions() {
        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {
                String date = spinnerLogs.getSelectedItem().toString();
                String[] params = new String[] {date, "0"};
                String sql = "SELECT DISTINCT time_of_transaction FROM " + TABLE_MPESA + " WHERE " + DATE_MILLIS + " = ? " + " AND " + COLUMN_MPESA_STATUS + " = ? " + " ORDER BY " + TIME_OF_TRANSACTION + " ASC;";
                return db.rawQuery(sql,params);
            }
        };
    }

    private CursorLoader getMpesaDates() {
        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {

                SQLiteDatabase db = database.getReadableDatabase();
                String selectQuery = "SELECT DISTINCT " + DATE_MILLIS + " FROM " + TABLE_MPESA + " ORDER BY " + DATE_MILLIS + " ASC;";
                return db.rawQuery(selectQuery, null);
            }
        };

    }
    private CursorLoader getUnsyncedMpesaDates() {
        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {

                SQLiteDatabase db = database.getReadableDatabase();
                mParam = new String[]{"0"};
                String selectQuery = "SELECT DISTINCT " + DATE_MILLIS + " FROM " + TABLE_MPESA + " WHERE " + COLUMN_MPESA_STATUS + " = ? "+ " ORDER BY " + DATE_MILLIS + " ASC;";
                return db.rawQuery(selectQuery, mParam);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_TRANSACTION_DATES)
            loadFinishedDates(data);
        else if (loader.getId() == LOADER_TRANSACTIONS)
            loadTransactions(data);
    }

    private void loadTransactions(Cursor data) {
        mTransactions = data;
        List<DataModel> datas=new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        DataModel dataModel;
        String date = spinnerLogs.getSelectedItem().toString();
        if (mTransactions.moveToFirst()) {
            do {
                String time = mTransactions.getString(mTransactions.getColumnIndexOrThrow(TIME_OF_TRANSACTION));
                String[] parameter = new String[]{date, time};
                String sqlite = "SELECT DISTINCT date_in_millis,time_of_transaction,opening_float,opening_cash,added_float,added_cash,reducted_float,reducted_cash,closing_cash,comment,status FROM " + TABLE_MPESA + " WHERE " + DATE_MILLIS + " = ? " + " AND " + TIME_OF_TRANSACTION + " = ? " + " ORDER BY " + TIME_OF_TRANSACTION + " ASC;";
                Cursor cur = db.rawQuery(sqlite, parameter);
                if (cur.moveToFirst()) {
                    do {
                        dataModel = new DataModel();
                        String openingFloat = cur.getString(cur.getColumnIndexOrThrow(COLUMN_OPENING_FLOAT));
                        String openingCash = cur.getString(cur.getColumnIndexOrThrow(COLUMN_OPENING_CASH));
                        // String dateMillis = cursor.getString(cursor.getColumnIndexOrThrow(DATE_MILLIS));
                        String addedFloat = cur.getString(cur.getColumnIndexOrThrow(COLUMN_ADDED_FLOAT));
                        String addedCash = cur.getString(cur.getColumnIndexOrThrow(COLUMN_ADDED_CASH));
                        String reductedFloat = cur.getString(cur.getColumnIndexOrThrow(COLUMN_REDUCTED_FLOAT));
                        String reductedCash = cur.getString(cur.getColumnIndexOrThrow(COLUMN_REDUCTED_CASH));
                        String closingCash = cur.getString(cur.getColumnIndexOrThrow(COLUMN_CLOSING_CASH));
                        String comment = cur.getString(cur.getColumnIndexOrThrow(COLUMN_COMMENT));
                        String timeOfTransaction = cur.getString(cur.getColumnIndexOrThrow(TIME_OF_TRANSACTION));
                        String syncStatus = cur.getString(cur.getColumnIndexOrThrow(COLUMN_MPESA_STATUS));
                        if (!openingFloat.equals("0")) {
                            dataModel.setAmount(openingFloat);
                            dataModel.setTypeOfTransaction("Added opening float");

                        } else if (!openingCash.equals("0")) {
                            dataModel.setAmount(openingCash);
                            dataModel.setTypeOfTransaction("Added opening cash");
                        } else if (!addedFloat.equals("0")) {
                            dataModel.setAmount(addedFloat);
                            dataModel.setTypeOfTransaction("Added float");
                        } else if (!addedCash.equals("0")) {
                            dataModel.setAmount(addedCash);
                            dataModel.setTypeOfTransaction("Added cash");
                        } else if (!reductedFloat.equals("0")) {
                            dataModel.setAmount(reductedFloat);
                            dataModel.setTypeOfTransaction("Reducted float");
                        } else if (!reductedCash.equals("0")) {
                            dataModel.setAmount(reductedCash);
                            dataModel.setTypeOfTransaction("Reducted cash");
                        } else if (!closingCash.equals("0")) {
                            dataModel.setAmount(closingCash);
                            dataModel.setTypeOfTransaction("Closing cash");
                        } else {
                            dataModel.setAmount("none");
                        }

                        dataModel.setComment(comment);
                        dataModel.setTimeOfTransaction(timeOfTransaction);
                        dataModel.setSyncStatus(syncStatus);
                        stringBuffer.append(dataModel);
                        // stringBuffer.append(dataModel);
                        datas.add(dataModel);
                    }
                    while (cur.moveToNext());
                }
                cur.close();
            }
            while (mTransactions.moveToNext());
        }
        datamodel= datas;
        recycler =new MpesaAdapter(datamodel);
        RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);

    }

    private void loadFinishedDates(Cursor data) {
        mDates = data;
        ArrayList<String> list = new ArrayList<>();
        if (mDates.getCount() > 0) {
            while (mDates.moveToNext()) {
                String dateOfTransaction = mDates.getString(mDates.getColumnIndex("date_in_millis"));
                list.add(dateOfTransaction);
            }
        }
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, list);
        spinnerLogs.setAdapter(adapter);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == LOADER_TRANSACTION_DATES){
            if (mDates != null){
                mDates.close();
            }
        }
        else if (loader.getId() == LOADER_TRANSACTIONS){
            if (mTransactions != null){
                mTransactions.close();
            }
        }

    }
}
