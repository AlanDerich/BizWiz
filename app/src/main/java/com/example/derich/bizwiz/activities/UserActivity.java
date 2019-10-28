package com.example.derich.bizwiz.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.clients.AllClients;
import com.example.derich.bizwiz.clients.ClientsDetails;
import com.example.derich.bizwiz.clients.DeleteClient;
import com.example.derich.bizwiz.clients.ViewClient;
import com.example.derich.bizwiz.credentials.LoginActivity;
import com.example.derich.bizwiz.credentials.RegisterActivity;
import com.example.derich.bizwiz.mpesa.Mpesa;
import com.example.derich.bizwiz.products.BackupData;
import com.example.derich.bizwiz.products.ProductsOffered;
import com.example.derich.bizwiz.sales.AddDebt;
import com.example.derich.bizwiz.sales.Sales;
import com.example.derich.bizwiz.sql.DatabaseHelper;
import com.example.derich.bizwiz.syncFromServer.Main;
import com.example.derich.bizwiz.utils.PreferenceUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import static com.example.derich.bizwiz.PreferenceHelper.getUsername;
import static com.example.derich.bizwiz.credentials.LoginActivity.sharedPreferences;

public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab_plus,fab_client,fab_product,fab_debt;
    TextView add_product,add_debt,add_client;
    Animation FabOpen,FabClose,FabClockwise,FabAntiClockwise;
    boolean isOpen = false;
    DatabaseHelper myDb;
    public DatabaseHelper db;

    Button viewClients,clients,sales,products,transactionss, sync, delete_client, addUser;
    private final int REQUEST_PERMISSION_READ_CONTACTS = 1;

    private TextView textViewName;

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;
    private Cursor mCursor;
    private String Admin = "Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);

        setContentView(R.layout.activity_user);
        viewClients = findViewById(R.id.viewClients);
        clients = findViewById(R.id.clients);
        sales = findViewById(R.id.sales);
        products = findViewById(R.id.products);
        transactionss = findViewById(R.id.transactionss);
        sync = findViewById(R.id.sync);
        add_client= findViewById(R.id.add_client_text);
        add_debt= findViewById(R.id.add_debt_text);
        add_product= findViewById(R.id.add_product_text);
        fab_plus = findViewById(R.id.add);
        fab_client =findViewById(R.id.addClient);
        fab_debt = findViewById(R.id.addDebt);
        fab_product= findViewById(R.id.addProduct);
        delete_client = findViewById(R.id.delete_client);
        addUser = findViewById(R.id.addClientButton);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showPhoneStatePermission();



        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen)
                {
                    fab_client.startAnimation(FabClose);
                    add_client.startAnimation(FabClose);
                    fab_debt.startAnimation(FabClose);
                    add_debt.startAnimation(FabClose);
                    fab_product.startAnimation(FabClose);
                    add_product.startAnimation(FabClose);
                    fab_plus.startAnimation(FabAntiClockwise);
                    fab_debt.setClickable(false);
                    fab_client.setClickable(false);
                    fab_product.setClickable(false);
                    isOpen= false;
                }

                else {
                    fab_client.startAnimation(FabOpen);
                    add_client.startAnimation(FabOpen);
                    fab_debt.startAnimation(FabOpen);
                    add_debt.startAnimation(FabOpen);
                    fab_product.startAnimation(FabOpen);
                    add_product.startAnimation(FabOpen);
                    fab_plus.startAnimation(FabClockwise);
                    fab_client.setClickable(true);
                    fab_debt.setClickable(true);
                    fab_product.setClickable(true);
                    isOpen= true;
                    fab_client.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(UserActivity.this, ClientsDetails.class);
                            startActivity(intent);
                        }
                    });
                    fab_debt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(UserActivity.this, AddDebt.class);
                            startActivity(intent);
                        }
                    });
                    fab_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(UserActivity.this, BackupData.class);
                            startActivity(intent);
                        }
                    });
                }
            }});
        myDb = new DatabaseHelper(this);

        textViewName = findViewById(R.id.text1);
        sharedPreferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

        textViewName.setText("Welcome " + getUsername() + ". What would you like to do?");
        String Administrator = getUsername();


if (Administrator.equals(Admin)){
    addUser.setVisibility(View.VISIBLE);
    delete_client.setVisibility(View.VISIBLE);
}
else {
    addUser.setVisibility(View.GONE);
    delete_client.setVisibility(View.GONE);
}

        clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, AllClients.class);
                startActivity(intent);
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, Sales.class);
                startActivity(intent);
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, ProductsOffered.class);
                startActivity(intent);
            }
        });
        transactionss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, Transactions.class);
                startActivity(intent);
            }
        });
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserActivity.this, Main.class);
                startActivity(intent);
            }
        });
        viewClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, ViewClient.class);
                startActivity(intent);
            }
        });
        delete_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DeleteClient.class);
                startActivity(intent);
            }

        });
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mAccount = CreateSyncAccount(UserActivity.this);
    }

    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(UserActivity.this);

            builder.setMessage("Do you want to exit ?");


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
                                    finish();
                                }
                            });
            builder
                    .setNegativeButton(
                            "No",
                            new DialogInterface
                                    .OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    dialog.cancel();
                                }
                            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(UserActivity.this, UserActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_clients) {
            Intent intent = new Intent(UserActivity.this, AllClients.class);
            startActivity(intent);

        } else if (id == R.id.nav_mpesa_logs) {
            Intent intent = new Intent(UserActivity.this, MpesaLogs.class);
            startActivity(intent);

        } else if (id == R.id.nav_mpesa) {
            Intent intent = new Intent(UserActivity.this, Mpesa.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(UserActivity.this);

                builder.setMessage("Remaining items = " + getUnsynced());


                builder.setTitle("Sync !");
                builder.setCancelable(false);
                builder
                        .setPositiveButton(
                                "Refresh",
                                new DialogInterface
                                        .OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        finish();
                                        startActivity(getIntent());
                                       dialog.cancel();
                                    }
                                });

                builder
                        .setNegativeButton(
                                "Cancel",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void showPhoneStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CONTACTS);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                showExplanation("Permission Needed", "Permission Needed to access contacts", Manifest.permission.READ_CONTACTS, REQUEST_PERMISSION_READ_CONTACTS);

            } else {
                requestPermission(Manifest.permission.READ_CONTACTS, REQUEST_PERMISSION_READ_CONTACTS);

            }
        } else {


        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_READ_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    private int getUnsynced(){
        int remainingClients = db.unsyncedClients();
        int remainingProducts = db.unsyncedProducts();
        int remainingMpesa = db.unsyncedMpesa();
        int remainingSales = db.unsyncedSales();
        int remainingUsers = db.unsyncedUsers();
        int remainingReports = db.unsyncedReports();
        int remainingTransactions = db.unsyncedTransactions();
        int totalCount = remainingClients + remainingProducts + remainingMpesa + remainingSales + remainingUsers + remainingReports + remainingTransactions;
        return totalCount;
    }


}
