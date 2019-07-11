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


public class AddDebt extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText   editTextQuantity;
    Button btnViewAll;
    Button btnViewUpdate;
    Spinner editTextName, ProductName;
    Button btnViewProducts;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_debt);
        myDb = new DatabaseHelper(this) ;
        editTextName = findViewById(R.id.editText_Name);
        btnViewAll = findViewById(R.id.button_view);
        btnViewUpdate = findViewById(R.id.button_update);
        btnViewProducts = findViewById(R.id.button_viewProdkts);
        ProductName = findViewById(R.id.Prod_Name);
        editTextQuantity = findViewById(R.id.editText_quantity);
        DatabaseHelper databaseHelper= new DatabaseHelper(this);
        ArrayList<String> listPro = databaseHelper.getAllClients();
        Spinner spinner=(Spinner) findViewById(R.id.editText_Name);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner_layout,R.id.txt,listPro);
        spinner.setAdapter(adapter);
        ArrayList<String> listProducts = databaseHelper.getAllProducts();
        Spinner spinner1=(Spinner) findViewById(R.id.Prod_Name);
        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(this, R.layout.spinner_layout1,R.id.txts,listProducts);
        spinner1.setAdapter(adapter1);
        viewAll();
        UpdateData();
        viewProducts();

    }

    public void UpdateData() {
        btnViewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String client_name = editTextName.getSelectedItem().toString();
                        String productName = ProductName.getSelectedItem().toString();
                        String quantity = editTextQuantity.getText().toString();
                        databaseHelper = new DatabaseHelper(getApplicationContext());
                        sqLiteDatabase = databaseHelper.getReadableDatabase();
                        int previousBal=databaseHelper.previousBal(productName);
                        int previousDebt=databaseHelper.previousDebt(client_name);
                        int price = databaseHelper.productPrice(productName);
                        int amount = price * Integer.valueOf(quantity);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                        String currentDateandTime = sdf.format(new Date());



                        if ( !(productName.isEmpty()) && !(quantity.isEmpty())) {

                            int new_value = previousBal - Integer.valueOf(quantity);
                            int new_debt = previousDebt + amount;
                            String type = amount + " Ksh added to " + client_name + "'s debt " + " from " + productName + " ." + new_value + " " +  productName + " left." ;

                            if (new_value > 0) {
                                boolean Update = myDb.updateData(client_name, String.valueOf(new_debt), productName, String.valueOf(new_value), currentDateandTime, type);
                                if (Update == true) {
                                    Toast.makeText(AddDebt.this, "Data Updated", Toast.LENGTH_LONG).show();
                                    emptyInputEditText();
                                } else {
                                    Toast.makeText(AddDebt.this, "Data not Updated", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(AddDebt.this, "No items left", Toast.LENGTH_LONG).show();
                                emptyInputEditText();
                            }
                        }
                        else {
                            Toast.makeText(AddDebt.this, "Sorry...Values cannot be empty!", Toast.LENGTH_LONG).show();
                            emptyInputEditText();
                        }

                    }

                }
        );
    }


    private void emptyInputEditText(){
        editTextQuantity.setText(null);
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
    public void viewProducts() {
        btnViewProducts.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getProducts();
                        if (res.getCount() == 0) {
                            showMessage("Error", "Nothing found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :").append(res.getString(0)).append("\n");
                            buffer.append("Name :").append(res.getString(1)).append("\n");
                            buffer.append("Quantity :").append(res.getString(2)).append("\n");
                            buffer.append("Price :").append(res.getString(3)).append("\n\n");
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