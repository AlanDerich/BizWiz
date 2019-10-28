package com.example.derich.bizwiz.sales;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.clients.ViewClient;
import com.example.derich.bizwiz.mpesa.ReductedCash;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ClearDebt extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText  editAmount;
    Button btnViewAll;
    Spinner clientName;
    Button btnViewUpdate;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_debt);
        myDb = new DatabaseHelper(this) ;
        clientName = findViewById(R.id.client_Name);
        editAmount = findViewById(R.id.editText_paid_amount);
        btnViewAll = findViewById(R.id.button_view_clients);
        btnViewUpdate = findViewById(R.id.button_update_debt);
        DatabaseHelper databaseHelper= new DatabaseHelper(this);
        ArrayList<String> listPro = databaseHelper.getAllClients();
        Spinner spinner= findViewById(R.id.client_Name);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner_layout,R.id.txt,listPro);
        spinner.setAdapter(adapter);
        viewAll();
        UpdateData();

    }

    public void UpdateData() {
        btnViewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String amount = editAmount.getText().toString();
                        if (clientName.getSelectedItem() !=null){
                        final String client_name = clientName.getSelectedItem().toString();
                        databaseHelper = new DatabaseHelper(getApplicationContext());
                        sqLiteDatabase = databaseHelper.getReadableDatabase();
                        int previousDebt=databaseHelper.previousDebt(client_name);
                        int previousUnsyncedDebt = databaseHelper.previousUnsyncedDebt(client_name);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                        final String currentDateandTime = sdf.format(new Date());
                            long timeMillis = System.currentTimeMillis();
                            final String date = ReductedCash.getDate(timeMillis);
                            SimpleDateFormat sdfAdd = new SimpleDateFormat("HH:mm:ss");
                            final String currentTimeOfAdd = sdfAdd.format(new Date());

                        if ( !(client_name.isEmpty()) && !(amount.isEmpty())) {

                            final int new_debt = previousDebt -Integer.valueOf(amount) ;
                            final String type = amount + " Ksh Paid by " + client_name + " .Client new debt is " + new_debt;
                            final int new_unsynced_debt = previousUnsyncedDebt - Integer.valueOf(amount);


                            if (new_debt < previousDebt && new_debt >(-1)) {
                                AlertDialog.Builder builder
                                        = new AlertDialog
                                        .Builder(ClearDebt.this);

                                builder.setMessage("Do you want to clear a debt of ksh " + amount +" from " +client_name +" ? Remaining debt will be " +new_debt);


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
                                                        boolean Update = myDb.updateDebt(client_name, String.valueOf(new_unsynced_debt), String.valueOf(new_debt), currentDateandTime, type,PreferenceHelper.getUsername());
                                                        boolean updateDebtsPaid = myDb.insertDebtsPaid(amount,PreferenceHelper.getUsername(),date,currentTimeOfAdd);
                                                        if (Update && updateDebtsPaid) {
                                                            Toast.makeText(ClearDebt.this, "Debt Updated", Toast.LENGTH_LONG).show();
                                                            emptyInputEditText();
                                                        } else {
                                                            Toast.makeText(ClearDebt.this, "Debt not Updated", Toast.LENGTH_LONG).show();
                                                        }
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


                            } else {
                                Toast.makeText(ClearDebt.this, "Error! Amount paid is more than debt", Toast.LENGTH_LONG).show();
                                emptyInputEditText();
                            }
                        }
                        else {
                            Toast.makeText(ClearDebt.this, "Sorry...Values cannot be empty!", Toast.LENGTH_LONG).show();
                            emptyInputEditText();
                        }
                        }
                        else {
                            Toast.makeText(ClearDebt.this, "Sorry...Values cannot be empty!", Toast.LENGTH_LONG).show();
                            emptyInputEditText();
                        }
                    }

                }
        );
    }


    private void emptyInputEditText(){
        editAmount.setText(null);
    }
    public void viewAll() {
        btnViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ClearDebt.this, ViewClient.class);
                        startActivity(intent);
                    }
                }
        );
    }

}