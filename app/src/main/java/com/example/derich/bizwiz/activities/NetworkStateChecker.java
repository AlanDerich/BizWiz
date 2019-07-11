package com.example.derich.bizwiz.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.derich.bizwiz.sql.DatabaseHelper;
import com.example.derich.bizwiz.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;
import static com.example.derich.bizwiz.activities.LoginActivity.MyPREFERENCES;
import static com.example.derich.bizwiz.activities.LoginActivity.Name;

public class NetworkStateChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private DatabaseHelper db;
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

        //if there is a network
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
                    } while (cursor.moveToNext());
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
    private void saveClient(final int client_id,final String client_fullName, final String client_debt, final String Number, final String client_Email) {
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
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                                String currentDateandTime = sdf.format(new Date());
                                String type = "Synced clients with server. ";
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
                                db.updateClientStatus(product_id, BackupData.PRODUCT_SYNCED_WITH_SERVER);

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
                params.put("product_quantity", product_quantity);
                params.put("product_price", product_price);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringReq);
    }

}