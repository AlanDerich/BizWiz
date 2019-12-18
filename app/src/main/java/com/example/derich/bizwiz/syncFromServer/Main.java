package com.example.derich.bizwiz.syncFromServer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.activities.DetectConnection;
import com.example.derich.bizwiz.activities.VolleySingleton;
import com.example.derich.bizwiz.credentials.LoginActivity;
import com.example.derich.bizwiz.sql.DatabaseHelper;
import com.example.derich.bizwiz.utils.PreferenceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_PRODUCT_ID;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_STATUSS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_PRODUCTS;

public class Main extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 22;
    private DatabaseHelper db;
    Button SaveButtonInSQLite,unsyncedData,saveToSd,saveFromSd;
    private String mSql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        Cursor cursorClients = db.getUnsyncedClients();
        mSql = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUSS + " =0 "+ " ORDER BY " + COLUMN_PRODUCT_ID + " ASC;";
        Cursor curProducts = db.getProducts(mSql);
        Cursor curTransactions = db.getUnsyncedTransactions();
        Cursor curMpesa = db.getUnsyncedMpesa();
        Cursor curUsers = db.getUnsyncedUsers();
        Cursor curSales = db.getUnsyncedSales();
        Cursor curReports = db.getUnsyncedReports();
        SaveButtonInSQLite = findViewById(R.id.savefromDatabase);
        unsyncedData = findViewById(R.id.checkUnsyncedData);
        saveToSd = findViewById(R.id.saveTosd);
        saveFromSd = findViewById(R.id.saveFromSd);
        if (!PreferenceHelper.getUsername().equals("Admin")){
            saveFromSd.setVisibility(View.GONE);
        }
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        unsyncedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this,UnsyncedData.class);
                startActivity(intent);
            }
        });
        saveToSd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE)){
                    exportDB();
                }

            }
        });
        saveFromSd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(Main.this);

                builder.setMessage("Are you sure you want to replace the current database with the back up one ?");

                builder.setTitle("Alert !");
                builder.setCancelable(false);

                builder
                        .setPositiveButton(
                                "Yes",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        if(checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE)){
                                            String dbPath =Environment.getExternalStorageDirectory().toString() + "/Bizwiz/Bizwiz.db";
                                            try {
                                                importDatabase(dbPath);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                Toast.makeText(Main.this, "Sorry! No database found", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });

                // Set the Negative button with No name
                // OnClickListener method is use
                // of DialogInterface interface.
                builder
                        .setNegativeButton(
                                "No",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {

                                        // If user click no
                                        // then dialog box is canceled.
                                        dialog.cancel();
                                    }
                                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();

                // Show the Alert Dialog box
                alertDialog.show();
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
            Toast.makeText(Main.this,"No internet connection is detected. Please enable DATA or WIFI and refresh to continue", Toast.LENGTH_LONG).show();
        }
        curMpesa.close();
        curProducts.close();
        curReports.close();
        curSales.close();
        cursorClients.close();
        curTransactions.close();
        curUsers.close();


    }
    public boolean checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(Main.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(Main.this,
                    new String[] { permission },
                    requestCode);
            return false;
        }
        else {
            return true;
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Main.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(Main.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
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
    private void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "com.example.derich.bizwiz" +"/databases/Bizwiz.db";
        String backupDBPath = "Bizwiz/Bizwiz.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/Bizwiz");
        dir.mkdirs();
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Export Failed!", Toast.LENGTH_LONG).show();
        }
    }
    public String DB_FILEPATH = Main.this.getFilesDir().getPath() + "/data/com.example.derich.bizwiz/databases/Bizwiz.db";

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public void importDatabase(String dbPath) throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        db.close();
        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);
        if (newDb.exists()) {
            this.deleteDatabase("Bizwiz.db");
            copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            db.getWritableDatabase().close();
            Toast.makeText(Main.this, "Imported successfully!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(Main.this, "No backup found!", Toast.LENGTH_SHORT).show();
        }
    }
    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

}