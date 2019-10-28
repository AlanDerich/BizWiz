package com.example.derich.bizwiz.syncFromServer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.activities.DetectConnection;
import com.example.derich.bizwiz.activities.VolleySingleton;
import com.example.derich.bizwiz.credentials.LoginActivity;
import com.example.derich.bizwiz.sql.DatabaseHelper;
import com.example.derich.bizwiz.utils.PreferenceUtils;

public class Main extends AppCompatActivity {
    private DatabaseHelper db;
    Button SaveButtonInSQLite,unsyncedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        Cursor cursorClients = db.getUnsyncedClients();
        Cursor curProducts = db.getUnsyncedProducts();
        Cursor curTransactions = db.getUnsyncedTransactions();
        Cursor curMpesa = db.getUnsyncedMpesa();
        Cursor curUsers = db.getUnsyncedUsers();
        Cursor curSales = db.getUnsyncedSales();
        Cursor curReports = db.getUnsyncedReports();
        SaveButtonInSQLite = findViewById(R.id.savefromDatabase);
        unsyncedData = findViewById(R.id.checkUnsyncedData);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        unsyncedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this,UnsyncedData.class);
                startActivity(intent);
            }
        });
        if (DetectConnection.checkConnection(this)) {
            // Its Available...
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
        if (cursorClients.moveToFirst()|| curProducts.moveToFirst() || curTransactions.moveToFirst() || curMpesa.moveToFirst() || curUsers.moveToFirst() || curSales.moveToFirst() || curReports.moveToFirst()){
            Toast.makeText(this,"Please refresh to sync unsaved data first",Toast.LENGTH_LONG).show();
        }

        else{
            SaveButtonInSQLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent = new Intent(Main.this, Syncronization.class);
               startActivity(intent);

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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.log_out:
                PreferenceUtils.saveEmail(null, this);
                PreferenceUtils.saveUsername(null, this);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.refresh:
                VolleySingleton.getInstance(this).getRequestQueue();
                Intent intent1 = new Intent(Main.this, Main.class);
                startActivity(intent1);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


}