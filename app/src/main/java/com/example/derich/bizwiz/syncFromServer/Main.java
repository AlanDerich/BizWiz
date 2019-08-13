package com.example.derich.bizwiz.syncFromServer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.activities.DetectConnection;
import com.example.derich.bizwiz.clients.ViewClient;
import com.example.derich.bizwiz.sql.DatabaseHelper;

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
        Cursor cursorClients = db.getUnsyncedClients();
        Cursor curProducts = db.getUnsyncedProducts();
        Cursor curTransactions = db.getUnsyncedTransactions();
        Cursor curMpesa = db.getUnsyncedMpesa();
        Cursor curUsers = db.getUnsyncedUsers();
        SaveButtonInSQLite = findViewById(R.id.savefromDatabase);
        ShowSQLiteDataInListView = findViewById(R.id.showList);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (DetectConnection.checkConnection(this)) {
            // Its Available...
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
        if (cursorClients.moveToFirst()|| curProducts.moveToFirst() || curTransactions.moveToFirst() || curMpesa.moveToFirst() || curUsers.moveToFirst()){
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

        ShowSQLiteDataInListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Main.this, ViewClient.class);
                startActivity(intent);

            }
        });


    }


}