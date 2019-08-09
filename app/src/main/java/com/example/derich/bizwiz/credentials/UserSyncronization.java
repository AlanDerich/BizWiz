package com.example.derich.bizwiz.credentials;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.derich.bizwiz.syncFromServer.HttpHandler;
import com.example.derich.bizwiz.syncFromServer.Syncronization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSyncronization extends AppCompatActivity {

    private String TAG = Syncronization.class.getSimpleName();
    SQLiteDatabase sqLiteDatabase;
    private DatabaseHelper db;


    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON

    private static String urlUsers = "https://alanderich.info/bizwiz/UsersFullForm.php";

    ArrayList<HashMap<String, String>> clientList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        SQLiteDataBaseBuild();

        clientList = new ArrayList<>();

        lv =  findViewById(R.id.listView1);

        new UserSyncronization.GetClients().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetClients extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UserSyncronization.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            getUsers();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            sqLiteDatabase.close();
            Toast.makeText(UserSyncronization.this, "Successful You can now Login", Toast.LENGTH_LONG).show();

            /**
             * Updating parsed JSON data into ListView
             * */
            Intent intent = new Intent(UserSyncronization.this, LoginActivity.class);
            startActivity(intent);

        }


    }
    public void SQLiteDataBaseBuild(){

        sqLiteDatabase = openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }


    public void DeletePreviousUserData(){

        sqLiteDatabase.execSQL("DELETE FROM "+DatabaseHelper.TABLE_USER+"");

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
                    String user_password = p.getString("user_password");

                    String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DatabaseHelper.TABLE_USER+" (user_name,user_email,user_password,user_status) VALUES('"+user_name+"', '"+user_email+"' , '"+user_password+"',  '"+1+"');";

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