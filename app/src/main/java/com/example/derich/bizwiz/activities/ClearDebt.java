package com.example.derich.bizwiz.activities;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
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
        Spinner spinner=(Spinner) findViewById(R.id.client_Name);
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
                        String amount = editAmount.getText().toString();
                        String client_name = clientName.getSelectedItem().toString();
                        databaseHelper = new DatabaseHelper(getApplicationContext());
                        sqLiteDatabase = databaseHelper.getReadableDatabase();
                        int previousDebt=databaseHelper.previousDebt(client_name);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                        String currentDateandTime = sdf.format(new Date());

                        if ( !(client_name.isEmpty()) && !(amount.isEmpty())) {

                            int new_debt = Integer.valueOf(amount) - previousDebt;
                            String type = amount + " Ksh Paid by " + client_name + " .Client's new debt is " + new_debt;


                            if (new_debt < previousDebt && new_debt >(-1)) {
                                boolean Update = myDb.updateDebt(client_name, String.valueOf(new_debt), currentDateandTime, type);
                                if (Update == true) {
                                    Toast.makeText(ClearDebt.this, "Debt Updated", Toast.LENGTH_LONG).show();
                                    emptyInputEditText();
                                } else {
                                    Toast.makeText(ClearDebt.this, "Debt not Updated", Toast.LENGTH_LONG).show();
                                }
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
                        Cursor res = myDb.getAllData();
                        if (res.getCount() == 0) {
                            showMessage("Error", "Nothing found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :").append(res.getString(0)).append("\n");
                            buffer.append("Name :").append(res.getString(1)).append("\n");
                            buffer.append("Phone :").append(res.getString(3)).append("\n");
                            buffer.append("Amount :").append(res.getString(2)).append("\n\n");
                        }

                        // Show all data
                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}