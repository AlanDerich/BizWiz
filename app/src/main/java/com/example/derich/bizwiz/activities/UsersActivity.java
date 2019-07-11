package com.example.derich.bizwiz.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.SaveInfo;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;
import com.example.derich.bizwiz.utils.PreferenceUtils;

import static android.content.Intent.getIntent;
import static com.example.derich.bizwiz.activities.LoginActivity.MyPREFERENCES;
import static com.example.derich.bizwiz.activities.LoginActivity.Name;

/**
 * Created by group 7 on 3/27/17.
 */
public class UsersActivity extends AppCompatActivity {
    FloatingActionButton fab_plus,fab_client,fab_product,fab_debt;
    TextView add_product,add_debt,add_client;
    Animation FabOpen,FabClose,FabClockwise,FabAntiClockwise;
    boolean isOpen = false;
    DatabaseHelper myDb;
    Button viewClients,clients,sales,products,transactionss;

    private TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        viewClients = findViewById(R.id.viewClients);
        clients = findViewById(R.id.clients);
        sales = findViewById(R.id.sales);
        products = findViewById(R.id.products);
        transactionss = findViewById(R.id.transactionss);
        add_client= findViewById(R.id.add_client_text);
        add_debt= findViewById(R.id.add_debt_text);
        add_product= findViewById(R.id.add_product_text);
        fab_plus = (FloatingActionButton)findViewById(R.id.add);
        fab_client =(FloatingActionButton)findViewById(R.id.addClient);
        fab_debt = (FloatingActionButton) findViewById(R.id.addDebt);
        fab_product= (FloatingActionButton) findViewById(R.id.addProduct);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
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
                            Intent intent = new Intent(UsersActivity.this, ClientsDetails.class);
                            startActivity(intent);
                        }
                    });
                    fab_debt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(UsersActivity.this, AddDebt.class);
                            startActivity(intent);
                        }
                    });
                    fab_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(UsersActivity.this, BackupData.class);
                            startActivity(intent);
                        }
                    });
                }
        }});

        myDb = new DatabaseHelper(this);

        textViewName = findViewById(R.id.text1);
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES , Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(Name, null);
        textViewName.setText("Welcome " +  name + ". What would you like to do?");




        clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsersActivity.this, AllClients.class);
                startActivity(intent);
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsersActivity.this, Sales.class);
                startActivity(intent);
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsersActivity.this, ProductsOffered.class);
                startActivity(intent);
            }
        });
        transactionss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsersActivity.this, Transactions.class);
                startActivity(intent);
            }
        });


        viewClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsersActivity.this, ViewClient.class);
                startActivity(intent);
            }
        });
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
                PreferenceUtils.savePassword(null, this);
                PreferenceUtils.saveEmail(null, this);
                PreferenceUtils.saveUsername(null, this);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    }