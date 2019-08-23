package com.example.derich.bizwiz.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.derich.bizwiz.clients.ClientsDetails;
import com.example.derich.bizwiz.products.BackupData;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkStateChecker extends BroadcastReceiver {

    private DatabaseHelper db;
    public Context context;





    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new DatabaseHelper(context);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = db.getUnsyncedClients();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced client to MySQL

                        saveClient(
                                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_FULLNAME)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_ADDED_DEBT)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_DEBT)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_EMAIL))
                        );
                    } while (cursor.moveToNext());
                }
                //getting all the unsynced products
                Cursor cur = db.getUnsyncedProducts();
                if (cur.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced client to MySQL
                        saveProduct(
                                cur.getInt(cur.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_ID)),
                                cur.getString(cur.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_NAME)),
                                cur.getString(cur.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)),
                                cur.getString(cur.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_BUYING_PRICE)),
                                cur.getString(cur.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_RETAIL_PRICE)),
                                cur.getString(cur.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_WHOLESALE_PRICE)),
                                cur.getString(cur.getColumnIndex(DatabaseHelper.COLUMN_SOLD_PRODUCT))

                        );
                    } while (cur.moveToNext());
                }
                Cursor trans = db.getUnsyncedTransactions();
                if (trans.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced client to MySQL
                        saveTransaction(
                                trans.getInt(trans.getColumnIndex(DatabaseHelper.TRANSACTION_ID)),
                                trans.getString(trans.getColumnIndex(DatabaseHelper.TRANSACTION_TYPE)),
                                trans.getString(trans.getColumnIndex(DatabaseHelper.TRANSACTION_USER)),
                                trans.getString(trans.getColumnIndex(DatabaseHelper.TRANSACTION_DATE))
                        );
                    } while (trans.moveToNext());
                }

                Cursor users = db.getUnsyncedUsers();
                if (users.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced client to MySQL
                        saveUser(
                                users.getInt(users.getColumnIndex(DatabaseHelper.COLUMN_USER_ID)),
                                users.getString(users.getColumnIndex(DatabaseHelper.COLUMN_USER_NAME)),
                                users.getString(users.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL)),
                                users.getString(users.getColumnIndex(DatabaseHelper.COLUMN_USER_PASSWORD)),
                                users.getString(users.getColumnIndex(DatabaseHelper.COLUMN_USER_QUESTION)),
                                users.getString(users.getColumnIndex(DatabaseHelper.COLUMN_USER_ANSWER))

                        );
                    } while (users.moveToNext());
                }
                Cursor reports = db.getUnsyncedReports();
                if (reports.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced client to MySQL
                        saveReport(
                                reports.getInt(reports.getColumnIndex(DatabaseHelper.REPORT_ID)),
                                reports.getString(reports.getColumnIndex(DatabaseHelper.REPORT_DATE)),
                                reports.getString(reports.getColumnIndex(DatabaseHelper.REPORT_USER)),
                                reports.getString(reports.getColumnIndex(DatabaseHelper.REPORT_PRODUCT)),
                                reports.getString(reports.getColumnIndex(DatabaseHelper.REPORT_WHOLESALE_SALES)),
                                reports.getString(reports.getColumnIndex(DatabaseHelper.REPORT_RETAIL_SALES)),
                                reports.getString(reports.getColumnIndex(DatabaseHelper.REPORT_DEBT_SALES)),
                                reports.getString(reports.getColumnIndex(DatabaseHelper.REPORT_ADDED_ITEMS)),
                                reports.getString(reports.getColumnIndex(DatabaseHelper.REPORT_SOLD_ITEMS))

                        );
                    } while (reports.moveToNext());
                }

                Cursor mpesa = db.getUnsyncedMpesa();
                if (mpesa.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced client to MySQL
                        saveMpesa(
                                mpesa.getInt(mpesa.getColumnIndex(DatabaseHelper.MPESA_ID)),
                                mpesa.getString(mpesa.getColumnIndex(DatabaseHelper.DATE_MILLIS)),
                                mpesa.getString(mpesa.getColumnIndex(DatabaseHelper.COLUMN_OPENING_FLOAT)),
                                mpesa.getString(mpesa.getColumnIndex(DatabaseHelper.COLUMN_OPENING_CASH)),
                                mpesa.getString(mpesa.getColumnIndex(DatabaseHelper.COLUMN_ADDED_FLOAT)),
                                mpesa.getString(mpesa.getColumnIndex(DatabaseHelper.COLUMN_ADDED_CASH)),
                                mpesa.getString(mpesa.getColumnIndex(DatabaseHelper.COLUMN_REDUCTED_FLOAT)),
                                mpesa.getString(mpesa.getColumnIndex(DatabaseHelper.COLUMN_REDUCTED_CASH)),
                                mpesa.getString(mpesa.getColumnIndex(DatabaseHelper.COLUMN_CLOSING_CASH)),
                                mpesa.getString(mpesa.getColumnIndex(DatabaseHelper.TIME_OF_TRANSACTION)),
                                mpesa.getString(mpesa.getColumnIndex(DatabaseHelper.COLUMN_COMMENT))

                        );
                    } while (mpesa.moveToNext());
                }
                Cursor sales = db.getUnsyncedSales();
                if (sales.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced client to MySQL
                        saveSales(
                                sales.getInt(sales.getColumnIndex(DatabaseHelper.COLUMN_SALES_ID)),
                                sales.getString(sales.getColumnIndex(DatabaseHelper.COLUMN_OPENING_CASH_SALES)),
                                sales.getString(sales.getColumnIndex(DatabaseHelper.COLUMN_WHOLESALE_SALES)),
                                sales.getString(sales.getColumnIndex(DatabaseHelper.COLUMN_RETAIL_SALES)),
                                sales.getString(sales.getColumnIndex(DatabaseHelper.COLUMN_SALES_DATE)),
                                sales.getString(sales.getColumnIndex(DatabaseHelper.COLUMN_SALES_TIME)),
                                sales.getString(sales.getColumnIndex(DatabaseHelper.COLUMN_SALES_USER)),
                                sales.getString(sales.getColumnIndex(DatabaseHelper.COLUMN_DAILY_EXPENSE)),
                                sales.getString(sales.getColumnIndex(DatabaseHelper.COLUMN_DEBTS_PAID)),
                                sales.getString(sales.getColumnIndex(DatabaseHelper.COLUMN_DAILY_DEBT))

                        );
                    } while (mpesa.moveToNext());
                }
            }
        }



    }




    private void saveProduct(final int product_id,final String product_name, final String product_quantity, final String product_buying_price,final String product_retail_price,final String product_wholesale_price, final String product_sold_product) {
        StringRequest stringReq = new StringRequest(Request.Method.POST, BackupData.URL_SAVE_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateProductStatus(product_id, BackupData.PRODUCT_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(BackupData.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_name", product_name);
                params.put("quantity", product_quantity);
                params.put("product_buying_price", product_buying_price);
                params.put("product_retail_price", product_retail_price);
                params.put("product_wholesale_price", product_wholesale_price);
                params.put("product_sold_product", product_sold_product);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringReq);
    }

    private void saveTransaction(final int transaction_id,final String transaction_type,final String transaction_user, final String transaction_date) {
        StringRequest stringReq = new StringRequest(Request.Method.POST, BackupData.URL_SAVE_TRANSACTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateTransactionStatus(transaction_id, BackupData.PRODUCT_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(BackupData.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("transaction_type", transaction_type);
                params.put("transaction_date", transaction_date);
                params.put("transaction_user", transaction_user);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringReq);
    }
    private void saveUser(final int user_id,final String user_name, final String user_email,final String user_password,final String user_question,final String user_answer) {
        StringRequest stringReq = new StringRequest(Request.Method.POST, BackupData.URL_SAVE_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateUSerStatus(user_id, BackupData.PRODUCT_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(BackupData.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", user_name);
                params.put("user_email", user_email);
                params.put("user_question",user_question);
                params.put("user_answer",user_answer);
                params.put("user_password", user_password);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringReq);
    }
    private void saveReport(final int report_id,final String report_date, final String report_user,final String report_product,final String report_wholesale_sales,final String report_retail_sales,final String report_debt_sales,final String report_added_items,final String report_sold_items) {
        StringRequest stringReq = new StringRequest(Request.Method.POST, BackupData.URL_SAVE_REPORTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateReportStatus(report_id, BackupData.PRODUCT_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(BackupData.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("report_date", report_date);
                params.put("report_user", report_user);
                params.put("report_product",report_product);
                params.put("report_wholesale_sales",report_wholesale_sales);
                params.put("report_retail_sales", report_retail_sales);
                params.put("report_debt_sales",report_debt_sales);
                params.put("report_added_items",report_added_items);
                params.put("report_sold_items", report_sold_items);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringReq);
    }

    private void saveMpesa(final int mpesa_id,final String date_in_millis, final String opening_float,final String opening_cash, final String added_float,final String added_cash, final String reducted_float,final String reducted_cash, final String closing_cash,final String time_of_transaction,final String comment) {
        StringRequest stringReq = new StringRequest(Request.Method.POST, BackupData.URL_SAVE_MPESA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateMpesaStatus(mpesa_id, BackupData.PRODUCT_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(BackupData.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("date_in_millis", date_in_millis);
                params.put("opening_float", opening_float);
                params.put("opening_cash", opening_cash);
                params.put("added_float", added_float);
                params.put("added_cash", added_cash);
                params.put("reducted_float", reducted_float);
                params.put("reducted_cash", reducted_cash);
                params.put("closing_cash", closing_cash);
                params.put("time_of_transaction", time_of_transaction);
                params.put("comment", comment);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringReq);
    }
    private void saveSales(final int sales_id,final String opening_cash_sales, final String wholesale_sales,final String retail_sales, final String sales_date,final String sales_time, final String sales_user,final String daily_expense, final String debts_paid,final String daily_debt) {
        StringRequest stringReq = new StringRequest(Request.Method.POST, BackupData.URL_SAVE_SALES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateSalesStatus(sales_id, BackupData.PRODUCT_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(BackupData.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opening_cash_sales", opening_cash_sales);
                params.put("wholesale_sales", wholesale_sales);
                params.put("retail_sales", retail_sales);
                params.put("sales_date", sales_date);
                params.put("sales_time", sales_time);
                params.put("sales_user", sales_user);
                params.put("daily_expense", daily_expense);
                params.put("debts_paid", debts_paid);
                params.put("daily_debt", daily_debt);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringReq);
    }

    /*
     * method taking two arguments
     * name that is to be saved and id of the name from SQLite
     * if the name is successfully sent
     * we will update the status as synced in SQLite
     * */
    public void saveClient(final int client_id, final String client_fullName, final String client_added_debt, final String client_debt, final String Number, final String client_Email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClientsDetails.URL_SAVE_CLIENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateClientStatus(client_id, ClientsDetails.CLIENT_SYNCED_WITH_SERVER);
                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(ClientsDetails.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("client_fullName", client_fullName);
                params.put("client_debt", client_debt);
                params.put("client_added_debt", client_added_debt);
                params.put("Number", Number);
                params.put("client_Email", client_Email);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}