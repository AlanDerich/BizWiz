package com.example.derich.bizwiz.syncFromServer;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_PRODUCT_ID;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_STATUSS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_PRODUCTS;

public class UnsyncedData extends AppCompatActivity {
    public DatabaseHelper db;
    RecyclerView unsyncedRecycler;
    UnsyncedDataAdapter recycler;
    List<UnsyncedDataModel> datamodel;
    StringBuffer stringBuffer;
    UnsyncedDataModel unsyncedDataModel;
    private Cursor mRem;
    private String mSql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsynced_data);
        db = new DatabaseHelper(this);
        unsyncedRecycler = findViewById(R.id.unsynced_recycler);
        datamodel=new ArrayList<>();
        stringBuffer = new StringBuffer();
        unsyncedDataModel = new UnsyncedDataModel();
        unsyncedList();
    }

    public void unsyncedList(){

        datamodel = getUnsynced();
        recycler =new UnsyncedDataAdapter(this,datamodel);
        RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
        unsyncedRecycler.setLayoutManager(reLayoutManager);
        unsyncedRecycler.setItemAnimator(new DefaultItemAnimator());
        unsyncedRecycler.setAdapter(recycler);
    }

    private List<UnsyncedDataModel> getUnsynced(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        List<UnsyncedDataModel> data1 = remainingClients();
        List<UnsyncedDataModel> data2 =remainingProducts();
        List<UnsyncedDataModel> data3 =remainingMpesa();
        List<UnsyncedDataModel> data4 = remainingSales();
        List<UnsyncedDataModel> data5 = remainingTransactions();
        List<UnsyncedDataModel> data6 = remainingUsers();
        List<UnsyncedDataModel> data7 = remainingReports();
        data.addAll(data1);
        data.addAll(data2);
        data.addAll(data3);
        data.addAll(data4);
        data.addAll(data5);
        data.addAll(data6);
        data.addAll(data7);
        return data;
    }
    private List<UnsyncedDataModel> remainingClients(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        String type = "Clients";
        mRem = db.getUnsyncedClients();
        int remainingClients = mRem.getCount();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingClients));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        mRem.close();
        return data;
    }
    private List<UnsyncedDataModel> remainingProducts(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        String type = "Products";
        mSql = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUSS + " =0 "+ " ORDER BY " + COLUMN_PRODUCT_ID + " ASC;";
        mRem = db.getProducts(mSql);
        int remainingProducts = mRem.getCount();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingProducts));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        mRem.close();
        return data;
    }
    private List<UnsyncedDataModel> remainingMpesa(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        String type = "Mpesa Transactions";
        mRem = db.getUnsyncedMpesa();
        int remainingMpesas = mRem.getCount();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingMpesas));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        mRem.close();
        return data;
    }
    private List<UnsyncedDataModel> remainingSales(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        mRem = db.getUnsyncedSales();
        int remainingSales = mRem.getCount();
        String type = "Sales";
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingSales));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        mRem.close();
        return data;
    }
    private List<UnsyncedDataModel> remainingUsers(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        String type = "Users";
        mRem = db.getUnsyncedUsers();
        int remainingUsers = mRem.getCount();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingUsers));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        mRem.close();
        return data;
    }
    private List<UnsyncedDataModel> remainingReports(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        String type = "Reports";
        mRem = db.getUnsyncedReports();
        int remainingReports = mRem.getCount();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingReports));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        mRem.close();
        return data;
    }
    private List<UnsyncedDataModel> remainingTransactions(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        String type = "Transactions Log";
        mRem = db.getUnsyncedTransactions();
        int remainingTransactions = mRem.getCount();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingTransactions));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        mRem.close();
        return data;
    }

}
