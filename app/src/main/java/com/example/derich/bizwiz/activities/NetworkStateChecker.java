package com.example.derich.bizwiz.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NetworkStateChecker extends BroadcastReceiver {

    private DatabaseHelper db;
    public Context context;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey" ;
    public static final String Email = "emailKey";
    SharedPreferences sharedPreferences;





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
                                cur.getString(cur.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_PRICE))
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
                                users.getString(users.getColumnIndex(DatabaseHelper.COLUMN_USER_PASSWORD))

                        );
                    } while (users.moveToNext());
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
                                mpesa.getString(mpesa.getColumnIndex(DatabaseHelper.COLUMN_COMMENT))

                        );
                    } while (mpesa.moveToNext());
                }
            }
        }



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
                params.put("client_added_debt", client_added_debt);
                params.put("client_debt", client_debt);
                params.put("Number", Number);
                params.put("client_Email", client_Email);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void saveProduct(final int product_id,final String product_name, final String product_quantity, final String product_price) {
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
                params.put("product_price", product_price);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringReq);
    }

    private void saveTransaction(final int transaction_id,final String transaction_type, final String transaction_date) {
        StringRequest stringReq = new StringRequest(Request.Method.POST, BackupData.URL_SAVE_TRANSACTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateProductStatus(transaction_id, BackupData.PRODUCT_SYNCED_WITH_SERVER);

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
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringReq);
    }
    private void saveUser(final int user_id,final String user_name, final String user_email,final String user_password) {
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
                params.put("user_password", user_password);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringReq);
    }

    private void saveMpesa(final int client_id,final String date_in_millis, final String opening_float,final String opening_cash, final String added_float,final String added_cash, final String reducted_float,final String reducted_cash, final String closing_cash,final String comment) {
        StringRequest stringReq = new StringRequest(Request.Method.POST, BackupData.URL_SAVE_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateUSerStatus(client_id, BackupData.PRODUCT_SYNCED_WITH_SERVER);

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
                params.put("comment", comment);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringReq);
    }

}