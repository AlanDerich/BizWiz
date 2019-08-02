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
import com.example.derich.bizwiz.sql.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main extends AppCompatActivity {
    private DatabaseHelper db;
    SQLiteDatabase sqLiteDatabase;
    Button SaveButtonInSQLite, ShowSQLiteDataInListView;

    String HttpJSonURL = "https://alanderich.info/bizwiz/test/SubjectFullForm.php";

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

                DeletePreviousData();

                new StoreJSonDataInToSQLiteClass(Main.this).execute();

            }
        });
        }
            }
            else {
            Toast.makeText(Main.this,"No internet connection is detected", Toast.LENGTH_LONG).show();
            }
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

        String FinalJSonResult;

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

            HttpServiceClass httpServiceClass = new HttpServiceClass(HttpJSonURL);

            try {
                httpServiceClass.ExecutePostRequest();

                if (httpServiceClass.getResponseCode() == 200) {

                    FinalJSonResult = httpServiceClass.getResponse();

                    if (FinalJSonResult != null) {

                        JSONArray jsonArray = null;
                        try {

                            jsonArray = new JSONArray(FinalJSonResult);
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
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {

                    Toast.makeText(context, httpServiceClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            sqLiteDatabase.close();

            progressDialog.dismiss();

            Toast.makeText(Main.this,"Load Done", Toast.LENGTH_LONG).show();

        }
    }


    public void SQLiteDataBaseBuild(){

        sqLiteDatabase = openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    public void DeletePreviousData(){

        sqLiteDatabase.execSQL("DELETE FROM "+DatabaseHelper.TABLE_CLIENT+"");

    }
}