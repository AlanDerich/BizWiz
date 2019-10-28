package com.example.derich.bizwiz.syncFromServer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UnsyncedData extends AppCompatActivity {
    public DatabaseHelper db;
    RecyclerView unsyncedRecycler;
    UnsyncedDataAdapter recycler;
    List<UnsyncedDataModel> datamodel;
    StringBuffer stringBuffer;
    UnsyncedDataModel unsyncedDataModel;
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
    /*public List<DataModel> getdata(String date){
        // DataModel dataModel = new DataModel();

        String[] params = new String[] {date};
        String sql = "SELECT DISTINCT time_of_transaction FROM " + TABLE_MPESA + " WHERE " + DATE_MILLIS + " = ? " + " ORDER BY " + TIME_OF_TRANSACTION + " ASC;";
        Cursor cursor = db.rawQuery(sql,params);
        StringBuffer stringBuffer = new StringBuffer();
        DataModel dataModel;
        if (cursor.moveToFirst()) {
            do {
                String time = cursor.getString(cursor.getColumnIndexOrThrow(TIME_OF_TRANSACTION));
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
                        data.add(dataModel);
                    } while (cur.moveToNext());
                }
            } while (cursor.moveToNext());
        }


        //

        return data;
    }*/
    public void unsyncedList(){

        datamodel = getUnsynced();
        recycler =new UnsyncedDataAdapter(datamodel);
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
        int remainingClients = db.unsyncedClients();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingClients));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        return data;
    }
    private List<UnsyncedDataModel> remainingProducts(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        String type = "Products";
        int remainingProducts = db.unsyncedProducts();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingProducts));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        return data;
    }
    private List<UnsyncedDataModel> remainingMpesa(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        String type = "Mpesa Transactions";
        int remainingMpesa = db.unsyncedMpesa();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingMpesa));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        return data;
    }
    private List<UnsyncedDataModel> remainingSales(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        int remainingSales = db.unsyncedSales();
        String type = "Sales";
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingSales));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        return data;
    }
    private List<UnsyncedDataModel> remainingUsers(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        String type = "Users";
        int remainingUsers = db.unsyncedUsers();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingUsers));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        return data;
    }
    private List<UnsyncedDataModel> remainingReports(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        String type = "Reports";
        int remainingReports = db.unsyncedReports();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingReports));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        return data;
    }
    private List<UnsyncedDataModel> remainingTransactions(){
        List<UnsyncedDataModel> data = new ArrayList<>();
        UnsyncedDataModel dataModel;
        dataModel = new UnsyncedDataModel();
        String type = "Transactions Log";
        int remainingTransactions = db.unsyncedTransactions();
        dataModel.setTransactionType(type);
        dataModel.setItemsRemaining(String.valueOf(remainingTransactions));
        stringBuffer.append(dataModel);
        data.add(dataModel);
        return data;
    }

}
