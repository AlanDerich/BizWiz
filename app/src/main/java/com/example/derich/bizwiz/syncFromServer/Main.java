package com.example.derich.bizwiz.syncFromServer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.activities.DetectConnection;
import com.example.derich.bizwiz.activities.Transactions;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main extends AppCompatActivity {
    private DatabaseHelper db;
    SQLiteDatabase sqLiteDatabase;
    Button SaveButtonInSQLite, ShowSQLiteDataInListView;

    String ClientHttpJSonURL = "https://alanderich.info/bizwiz/SubjectFullForm.php";
    String ProductHttpJSonURL = "https://alanderich.info/bizwiz/ProductsFullForm.php";
    String UserHttpJSonURL = "https://alanderich.info/bizwiz/UsersFullForm.php";
    String TransactionHttpJSonURL = "https://alanderich.info/bizwiz/TransactionsFullForm.php";
    String MpesaHttpJSonURL = "https://alanderich.info/bizwiz/MpesaFullForm.php";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        Cursor cursor = db.getUnsyncedClients();
        SaveButtonInSQLite = findViewById(R.id.savefromDatabase);
        ShowSQLiteDataInListView = findViewById(R.id.showList);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (DetectConnection.checkConnection(this)) {
            // Its Available...
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
        if (cursor.moveToFirst()){
            Toast.makeText(this,"Please refresh to sync unsaved data first",Toast.LENGTH_LONG).show();
        }

        else{
            SaveButtonInSQLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDataBaseBuild();

                new StoreJSonDataInToSQLiteClass(Main.this).execute();

            }
        });
        }
            }
            else {
            Toast.makeText(Main.this,"No internet connection is detected", Toast.LENGTH_LONG).show();
            }
        }

        }
        else {
            Toast.makeText(Main.this,"No internet connection is detected. Please enable to continue", Toast.LENGTH_LONG).show();
        }

        ShowSQLiteDataInListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Main.this, ShowData.class);
                startActivity(intent);

            }
        });


    }

    private class StoreJSonDataInToSQLiteClass extends AsyncTask<Void, Void, Void> {

        public Context context;

        String ClientJSonResult;
        String ProductJsonResult;
        String MpesaJsonResult;
        String UsersJsonResult;
        String TransactionsJsonResult;

        public StoreJSonDataInToSQLiteClass(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = new ProgressDialog(Main.this);
            progressDialog.setTitle("LOADING");
            progressDialog.setMessage("Please Wait");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            getClients();
            getProducts();
            getUsers();
            getTransactions();
            getMpesa();

                        return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            sqLiteDatabase.close();

            progressDialog.dismiss();

        }

        public void getClients(){
            HttpServiceClass httpServiceClass = new HttpServiceClass(ClientHttpJSonURL);

            try {
                httpServiceClass.ExecutePostRequest();


                if (httpServiceClass.getResponseCode() == 200) {

                    ClientJSonResult = httpServiceClass.getResponse();

                    if (ClientJSonResult != null) {
                        DeletePreviousClientsData();
                        JSONArray jsonArray = null;
                        try {

                            jsonArray = new JSONArray(ClientJSonResult);
                            JSONObject jsonObject;

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);

                                String client_fullName = jsonObject.getString("client_fullName");
                                String client_debt = jsonObject.getString("client_debt");
                                String Number = jsonObject.getString("Number");
                                String clientEmail = jsonObject.getString("client_Email");
                                String time = jsonObject.getString("time");


                                String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_CLIENT+" (client_fullName,client_added_debt,client_debt,Number,client_Email,status) VALUES('"+client_fullName+"', '"+0+"' , '"+client_debt+"' , '"+Number+"', '"+clientEmail+"' , '"+1+"');";

                                sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                            }
                            Toast.makeText(Main.this,"Clients Updated", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                     }
                 else {

                    Toast.makeText(Main.this,"clients not updated", Toast.LENGTH_SHORT).show();
                }
            }
                else{
                    Toast.makeText(Main.this, httpServiceClass.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        public void getMpesa(){
            HttpServiceClass httpServiceClass = new HttpServiceClass(MpesaHttpJSonURL);

            try {
                httpServiceClass.ExecutePostRequest();


                if (httpServiceClass.getResponseCode() == 200) {

                    MpesaJsonResult = httpServiceClass.getResponse();

                    if (MpesaJsonResult != null) {
                        DeletePreviousMpesaData();
                        JSONArray jsonArray = null;
                        try {

                            jsonArray = new JSONArray(MpesaJsonResult);
                            JSONObject jsonObject;

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);

                                String date_in_millis = jsonObject.getString("date_in_millis");
                                String opening_float = jsonObject.getString("opening_float");
                                String opening_cash = jsonObject.getString("opening_cash");
                                String added_float = jsonObject.getString("added_float");
                                String added_cash = jsonObject.getString("added_cash");
                                String reducted_float = jsonObject.getString("reducted_float");
                                String reducted_cash = jsonObject.getString("reducted_cash");
                                String closing_cash = jsonObject.getString("closing_cash");
                                String comment = jsonObject.getString("comment");

                                String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_MPESA+" (date_in_millis,opening_float,opening_cash,added_float,added_cash,reducted_float,reducted_cash,closing_cash,comment,status) " +
                                        "VALUES('"+date_in_millis+"', '"+opening_float+"' , '"+opening_cash+"', '"+added_float+"', '"+added_cash+"' , '"+reducted_float+"', '"+reducted_cash+"', '"+closing_cash+"' , '"+comment+"',  '"+1+"');";

                                sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                            }
                            Toast.makeText(context,"Mpesa table updated", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                else {
                        Toast.makeText(context,"Mpesa table not updated", Toast.LENGTH_SHORT).show();
                    }
                }
                 else {

                    Toast.makeText(context, httpServiceClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        public void getUsers(){
            HttpServiceClass httpServiceClass = new HttpServiceClass(UserHttpJSonURL);

            try {
                httpServiceClass.ExecutePostRequest();


                if (httpServiceClass.getResponseCode() == 200) {

                    UsersJsonResult = httpServiceClass.getResponse();

                    if (UsersJsonResult != null) {
                        DeletePreviousUserData();
                        JSONArray jsonArray = null;
                        try {

                            jsonArray = new JSONArray(UsersJsonResult);
                            JSONObject jsonObject;

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);

                                String user_name = jsonObject.getString("user_name");
                                String user_email = jsonObject.getString("user_email");
                                String user_password = jsonObject.getString("user_password");

                                String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_USER+" (user_name,user_email,user_password,status) VALUES('"+user_name+"', '"+user_email+"' , '"+user_password+"',  '"+1+"');";

                                sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                            }
                            Toast.makeText(context,"Users updated", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                else {
                        Toast.makeText(context,"Users not updated", Toast.LENGTH_SHORT).show();
                    }
                }
                 else {

                        Toast.makeText(context, httpServiceClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        public void getTransactions(){
            HttpServiceClass httpServiceClass = new HttpServiceClass(TransactionHttpJSonURL);

            try {
                httpServiceClass.ExecutePostRequest();


                if (httpServiceClass.getResponseCode() == 200) {

                    TransactionsJsonResult = httpServiceClass.getResponse();

                    if (TransactionsJsonResult != null) {
                        DeletePreviousTransactionsData();
                        JSONArray jsonArray = null;
                        try {

                            jsonArray = new JSONArray(TransactionsJsonResult);
                            JSONObject jsonObject;

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);

                                String transaction_type = jsonObject.getString("transaction_type");
                                String transaction_date = jsonObject.getString("transaction_date");

                                String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_TRANSACTIONS+" (transaction_type,transaction_date,transaction_status) VALUES('"+transaction_type+"', '"+transaction_date+"',  '"+1+"');";

                                sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                            }
                            Toast.makeText(context,"Users updated", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(context,"Users not updated", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                    Toast.makeText(context, httpServiceClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        public void getProducts(){
            HttpServiceClass httpServiceClass = new HttpServiceClass(ProductHttpJSonURL);

            try {
                httpServiceClass.ExecutePostRequest();


                if (httpServiceClass.getResponseCode() == 200) {

                    ProductJsonResult = httpServiceClass.getResponse();

                    if (ProductJsonResult != null) {
                        DeletePreviousProductssData();
                        JSONArray jsonArray = null;
                        try {

                            jsonArray = new JSONArray(ProductJsonResult);
                            JSONObject jsonObject;

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);

                                String product_name = jsonObject.getString("product_name");
                                String quantity = jsonObject.getString("quantity");
                                String product_price = jsonObject.getString("product_price");

                                String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_PRODUCTS+" (product_name,product_quantity,product_price,status) VALUES('"+product_name+"', '"+quantity+"' , '"+product_price+"',  '"+1+"');";

                                sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                            }
                            Toast.makeText(context,"Products updated", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(context,"Products not updated", Toast.LENGTH_SHORT).show();
                    }
                }
                 else {

                        Toast.makeText(context, httpServiceClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


    public void SQLiteDataBaseBuild(){

        sqLiteDatabase = openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    public void DeletePreviousClientsData(){

        sqLiteDatabase.execSQL("DELETE FROM "+DatabaseHelper.TABLE_CLIENT+"");

    }
    public void DeletePreviousProductssData(){

        sqLiteDatabase.execSQL("DELETE FROM "+DatabaseHelper.TABLE_PRODUCTS+"");

    }
    public void DeletePreviousUserData(){

        sqLiteDatabase.execSQL("DELETE FROM "+DatabaseHelper.TABLE_USER+"");

    }
    public void DeletePreviousTransactionsData(){
        sqLiteDatabase.execSQL("DELETE FROM "+DatabaseHelper.TABLE_TRANSACTIONS+"");

    }
    public void DeletePreviousMpesaData(){
        sqLiteDatabase.execSQL("DELETE FROM "+DatabaseHelper.TABLE_MPESA+"");

    }


}