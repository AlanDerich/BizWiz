package com.example.derich.bizwiz.syncFromServer;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Syncronization extends AppCompatActivity {

    private String TAG = Syncronization.class.getSimpleName();
    SQLiteDatabase sqLiteDatabase;
    private DatabaseHelper db;


    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String urlClients = "https://alanderich.info/bizwiz/SubjectFullForm.php";
    private static String urlProducts = "https://alanderich.info/bizwiz/ProductsFullForm.php";
    private static String urlTransactions = "https://alanderich.info/bizwiz/TransactionsFullForm.php";
    private static String urlMpesa = "https://alanderich.info/bizwiz/MpesaFullForm.php";
    private static String urlUsers = "https://alanderich.info/bizwiz/UsersFullForm.php";
    private static String urlSales = "https://alanderich.info/bizwiz/salesFullForm.php";
    private static String urlReports = "https://alanderich.info/bizwiz/reportsFullForm.php";

    ArrayList<HashMap<String, String>> clientList;
    ArrayList<HashMap<String, String>> TransactionList;
   // ArrayList<HashMap<String, String>> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        SQLiteDataBaseBuild();

        clientList = new ArrayList<>();
        TransactionList = new ArrayList<>();

        lv =  findViewById(R.id.listView1);

        new GetFromServer().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetFromServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Syncronization.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            getClients();
            getProducts();
            getMpesaTransactions();
            getTransactions();
            getSales();
            getUsers();
            getReports();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            sqLiteDatabase.close();

            /**
             * Updating parsed JSON data into ListView
             * */
            SimpleAdapter adapter = new SimpleAdapter(
                    Syncronization.this, clientList,
                    R.layout.item_list_clients, new String[]{"client_fullName", "client_debt",
                    "Number","client_Email"}, new int[]{R.id.list_full_name,
                    R.id.list_debt, R.id.list_number, R.id.list_email});

            lv.setAdapter(adapter);

        }


    }
    public void SQLiteDataBaseBuild(){

        sqLiteDatabase = openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    public void DeletePreviousClientsData(){

        sqLiteDatabase.execSQL("DELETE FROM "+ DatabaseHelper.TABLE_CLIENT+"");

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
    public void DeletePreviousSalesData(){
        sqLiteDatabase.execSQL("DELETE FROM "+DatabaseHelper.TABLE_SALES+"");

    }
    public void DeletePreviousReportsData(){
        sqLiteDatabase.execSQL("DELETE FROM "+DatabaseHelper.TABLE_REPORT+"");

    }

    public void getClients(){

        HttpHandler sh = new HttpHandler();

        // Making a request to urlClients and getting response
        String jsonStr = sh.makeServiceCall(urlClients);

        Log.e(TAG, "Response from urlClients: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONArray jsonObj = new JSONArray(jsonStr);
                DeletePreviousClientsData();
                // looping through All Contacts
                for (int i = 0; i < jsonObj.length(); i++) {
                    JSONObject c = jsonObj.getJSONObject(i);

                    String client_fullName = c.getString("client_fullName");
                    String client_debt = c.getString("client_debt");
                    String Number = c.getString("Number");
                    String client_Email = c.getString("client_Email");

                    // tmp hash map for single client
                    HashMap<String, String> client = new HashMap<>();

                    // adding each child node to HashMap key => value
                    client.put("client_fullName", client_fullName);
                    client.put("client_debt", client_debt);
                    client.put("Number", Number);
                    client.put("client_Email", client_Email);

                    // adding client to client list
                    clientList.add(client);
                    String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_CLIENT+" (client_fullName,client_added_debt,client_debt,Number,client_Email,status) VALUES('"+client_fullName+"', '"+0+"' , '"+client_debt+"' , '"+Number+"', '"+client_Email+"' , '"+1+"');";

                    sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get Clients from server. Check your internet connection and try again!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
    }
    public void getTransactions(){

        HttpHandler sh = new HttpHandler();

        // Making a request to urlClients and getting response
        String jsonStr = sh.makeServiceCall(urlTransactions);

        Log.e(TAG, "Response from urlTransactions: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONArray jsonObj = new JSONArray(jsonStr);
                DeletePreviousTransactionsData();
                // looping through All Transactions
                for (int i = 0; i < jsonObj.length(); i++) {
                    JSONObject t = jsonObj.getJSONObject(i);

                    String transaction_type = t.getString("transaction_type");
                    String transaction_date = t.getString("transaction_date");
                    String transaction_user = t.getString("transaction_user");

                    // tmp hash map for single client
                    HashMap<String, String> transaction = new HashMap<>();

                    // adding each child node to HashMap key => value
                    transaction.put("transaction_type", transaction_type);
                    transaction.put("transaction_date", transaction_date);
                    transaction.put("transaction_user", transaction_user);

                    // adding client to client list
                    TransactionList.add(transaction);
                    String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_TRANSACTIONS+" (transaction_type,transaction_date,transaction_user,transaction_status) VALUES('"+transaction_type+"', '"+transaction_date+"' ,'"+transaction_user+"' , '"+1+"');";

                    sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get Transactions from server. Check your internet connection and try again!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
    }
    public void getProducts(){

        HttpHandler sh = new HttpHandler();

        // Making a request to urlProducts and getting response
        String jsonStr = sh.makeServiceCall(urlProducts);

        Log.e(TAG, "Response from urlProducts: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONArray jsonObj = new JSONArray(jsonStr);
                DeletePreviousProductssData();
                // looping through All Products
                for (int i = 0; i < jsonObj.length(); i++) {
                    JSONObject p = jsonObj.getJSONObject(i);

                    String product_name = p.getString("product_name");
                    String quantity = p.getString("quantity");
                    String product_buying_price = p.getString("product_buying_price");
                    String product_retail_price = p.getString("product_retail_price");
                    String product_wholesale_price = p.getString("product_wholesale_price");

                    // adding client to client list
                    String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_PRODUCTS+" (product_name,product_quantity,product_buying_price,product_retail_price,product_wholesale_price,product_sold_product,status) VALUES('"+product_name+"', '"+quantity+"' , '"+product_buying_price+"', '"+product_retail_price+"', '"+product_wholesale_price+"','"+0+"', '"+1+"');";

                    sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get Products from server. Check your internet connection and try again!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
    }
    public void getReports(){

        HttpHandler sh = new HttpHandler();

        // Making a request to urlReports and getting response
        String jsonStr = sh.makeServiceCall(urlReports);

        Log.e(TAG, "Response from urlReports: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONArray jsonObj = new JSONArray(jsonStr);
                DeletePreviousReportsData();
                // looping through All Products
                for (int i = 0; i < jsonObj.length(); i++) {
                    JSONObject p = jsonObj.getJSONObject(i);

                    String report_date = p.getString("report_date");
                    String report_user = p.getString("report_user");
                    String report_product = p.getString("report_product");
                    String report_wholesale_sales = p.getString("report_wholesale_sales");
                    String report_retail_sales = p.getString("report_retail_sales");
                    String report_debt_sales = p.getString("report_debt_sales");
                    String report_added_items = p.getString("report_added_items");
                    String report_sold_items = p.getString("report_sold_items");


                    String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_REPORT+" (report_date,report_user,report_product,report_wholesale_sales,report_retail_sales,report_debt_sales,report_added_items,report_sold_items,report_status) VALUES('"+report_date+"', '"+report_user+"' , '"+report_product+"', '"+report_wholesale_sales+"', '"+report_retail_sales+"','"+report_debt_sales+"','"+report_added_items+"','"+report_sold_items+"', '"+1+"');";

                    sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get Reports from server. Check your internet connection and try again!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
    }


    public void getMpesaTransactions(){

        HttpHandler sh = new HttpHandler();

        // Making a request to urlProducts and getting response
        String jsonStr = sh.makeServiceCall(urlMpesa);

        Log.e(TAG, "Response from urlMpesa: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONArray jsonObj = new JSONArray(jsonStr);
                DeletePreviousMpesaData();
                // looping through All Products
                for (int i = 0; i < jsonObj.length(); i++) {
                    JSONObject m = jsonObj.getJSONObject(i);

                    String date_in_millis = m.getString("date_in_millis");
                    String opening_float = m.getString("opening_float");
                    String opening_cash = m.getString("opening_cash");
                    String added_float = m.getString("added_float");
                    String added_cash = m.getString("added_cash");
                    String reducted_float = m.getString("reducted_float");
                    String reducted_cash = m.getString("reducted_cash");
                    String closing_cash = m.getString("closing_cash");
                    String time_of_transaction = m.getString("time_of_transaction");
                    String comment = m.getString("comment");

                    String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_MPESA+" (date_in_millis,opening_float,opening_cash,added_float,added_cash,reducted_float,reducted_cash,closing_cash,time_of_transaction,comment,status) " +
                            "VALUES('"+date_in_millis+"', '"+opening_float+"' , '"+opening_cash+"', '"+added_float+"', '"+added_cash+"' , '"+reducted_float+"', '"+reducted_cash+"', '"+closing_cash+"', '"+time_of_transaction+"' , '"+comment+"',  '"+1+"');";

                    sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get MpesaTransactions from server. Check your internet connection and try again!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
    }
    public void getSales(){

        HttpHandler sh = new HttpHandler();

        // Making a request to urlProducts and getting response
        String jsonStr = sh.makeServiceCall(urlSales);

        Log.e(TAG, "Response from urlSales: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONArray jsonObj = new JSONArray(jsonStr);
                DeletePreviousSalesData();
                // looping through All Products
                for (int i = 0; i < jsonObj.length(); i++) {
                    JSONObject m = jsonObj.getJSONObject(i);

                    String opening_cash_sales = m.getString("opening_cash_sales");
                    String wholesale_sales = m.getString("wholesale_sales");
                    String retail_sales = m.getString("retail_sales");
                    String sales_date = m.getString("sales_date");
                    String sales_time = m.getString("sales_time");
                    String sales_user = m.getString("sales_user");
                    String daily_expense = m.getString("daily_expense");
                    String debts_paid = m.getString("debts_paid");
                    String daily_debt = m.getString("daily_debt");

                    String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_SALES+" (opening_cash_sales,wholesale_sales,retail_sales,sales_date,sales_time,sales_user,daily_expense,debts_paid,daily_debt,sales_status) " +
                            "VALUES('"+opening_cash_sales+"', '"+wholesale_sales+"' , '"+retail_sales+"', '"+sales_date+"', '"+sales_time+"' , '"+sales_user+"', '"+daily_expense+"', '"+debts_paid+"', '"+daily_debt+"',  '"+1+"');";

                    sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get Sales from server. Check your internet connection and try again!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
    }
    public void getUsers(){

        HttpHandler sh = new HttpHandler();

        // Making a request to urlProducts and getting response
        String jsonStr = sh.makeServiceCall(urlUsers);

        Log.e(TAG, "Response from urlUsers: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONArray jsonObj = new JSONArray(jsonStr);
                DeletePreviousUserData();
                // looping through All Products
                for (int i = 0; i < jsonObj.length(); i++) {
                    JSONObject p = jsonObj.getJSONObject(i);

                    String user_name = p.getString("user_name");
                    String user_email = p.getString("user_email");
                    String user_question = p.getString("user_question");
                    String user_answer = p.getString("user_answer");
                    String user_password = p.getString("user_password");

                    String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_USER+" (user_name,user_email,user_question,user_answer,user_password,user_status) VALUES('"+user_name+"', '"+user_email+"' ,'"+user_question+"' , '"+user_answer+"', '"+user_password+"',  '"+1+"');";

                    sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get data from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get Users from server. Check your internet connection and try again!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
    }
}