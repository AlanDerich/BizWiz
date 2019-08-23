package com.example.derich.bizwiz.sales;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.clients.ViewClient;
import com.example.derich.bizwiz.mpesa.ReductedCash;
import com.example.derich.bizwiz.products.DisplayProducts;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AddDebt extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText   editTextQuantity;
    Button btnViewAll;
    Button btnViewUpdate;
    Spinner editTextName, ProductName;
    Button btnViewProducts;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    String[] prices = {"Wholesale", "Retail"};
    private Spinner mSpinnerPrices;
    private int mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_debt);
        databaseHelper = new DatabaseHelper(this) ;
        editTextName = findViewById(R.id.editText_Name);
        btnViewAll = findViewById(R.id.button_view);
        btnViewUpdate = findViewById(R.id.button_update);
        btnViewProducts = findViewById(R.id.button_viewProdkts);
        ProductName = findViewById(R.id.Prod_Name);
        editTextQuantity = findViewById(R.id.editText_quantity);
        ArrayList<String> listPro = databaseHelper.getAllClients();
        Spinner spinner= findViewById(R.id.editText_Name);
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        spinner.setAdapter(adapter);
        ArrayList<String> listProducts = databaseHelper.getAllProducts();
        Spinner spinner1= findViewById(R.id.Prod_Name);
        ArrayAdapter<String> adapter1= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listProducts);
        spinner1.setAdapter(adapter1);
        mSpinnerPrices = findViewById(R.id.product_price_spinner);
        mSpinnerPrices.setOnItemSelectedListener(this);
        viewAll();
        UpdateData();
        viewProducts();
        CustomAdapter customAdapter= new CustomAdapter(AddDebt.this, prices);
        mSpinnerPrices.setAdapter(customAdapter);

    }

    public void UpdateData() {
        btnViewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String quantity = editTextQuantity.getText().toString();
                        if (editTextName.getSelectedItem() !=null && ProductName.getSelectedItem() != null && !(quantity.isEmpty())){
                        String client_name = editTextName.getSelectedItem().toString();
                        String productName = ProductName.getSelectedItem().toString();
                        String productPrice = mSpinnerPrices.getSelectedItem().toString();
                        databaseHelper = new DatabaseHelper(getApplicationContext());
                        sqLiteDatabase = databaseHelper.getReadableDatabase();
                        int previousBal=databaseHelper.previousBal(productName);
                        int previousDebt=databaseHelper.previousDebt(client_name);
                        int previousUnsyncedDebt=databaseHelper.previousUnsyncedDebt(client_name);
                        int previousSoldSales = databaseHelper.previousSoldBal(productName);
                        int soldSales = previousSoldSales - Integer.valueOf(quantity);

                        int syncStatus = databaseHelper.syncStatus(client_name);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                            String currentDateandTime = sdf.format(new Date());
                            long timeMillis = System.currentTimeMillis();
                            String date = ReductedCash.getDate(timeMillis);
                            SimpleDateFormat sdfAdd = new SimpleDateFormat("HH:mm:ss");
                            String currentDateandTimeOfAdd = sdfAdd.format(new Date());
                            int previousSalesDebt = databaseHelper.previousSalesDebt(PreferenceHelper.getUsername(),date);
                            if (productPrice.equals("Wholesale")) {
                            int price = databaseHelper.productWholesalePrice(productName);
                            mAmount = price * Integer.valueOf(quantity);
                        }
                        else {
                            int price = databaseHelper.productRetailPrice(productName);
                            mAmount = price * Integer.valueOf(quantity);
                        }


                        if ( !(productName.isEmpty()) && !(quantity.isEmpty())) {

                            if (syncStatus == 1){
                                int new_value = previousBal - Integer.valueOf(quantity);
                            int new_debt = previousDebt + mAmount;
                            int new_unsynced_debt = previousUnsyncedDebt + mAmount;

                            String type = mAmount + " Ksh added to " + client_name + " debt " + " from " + productName + " ." + new_value + " " +  productName + " left." ;

                            if (new_value > -1) {
                                boolean Update = databaseHelper.updateData(client_name, String.valueOf(new_unsynced_debt), String.valueOf(new_debt), productName, String.valueOf(new_value),String.valueOf(soldSales), currentDateandTime, type, PreferenceHelper.getUsername());
                               boolean updateSales = databaseHelper.insertDebtSales((String.valueOf(mAmount + previousSalesDebt)),PreferenceHelper.getUsername(),date,currentDateandTimeOfAdd,productName,quantity);
                                if (Update && updateSales) {
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
                            else{
                                int new_value = previousBal - Integer.valueOf(quantity);
                                int new_debt = previousDebt + mAmount;
                                int new_unsynced_debt = previousUnsyncedDebt + mAmount;
                                String type = mAmount + " Ksh added to " + client_name + " debt " + " from " + productName + " ." + new_value + " " +  productName + " left." ;

                                if (new_value > -1) {
                                    boolean Update = databaseHelper.updateData(client_name, String.valueOf(new_unsynced_debt), String.valueOf(new_debt), productName, String.valueOf(new_value),String.valueOf(soldSales), currentDateandTime, type, PreferenceHelper.getUsername());
                                    boolean updateSales = databaseHelper.insertDebtSales((String.valueOf(mAmount+ previousSalesDebt)),PreferenceHelper.getUsername(),date,currentDateandTimeOfAdd,productName,quantity);
                                    if (Update && updateSales) {
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
                        }
                        else {
                            Toast.makeText(AddDebt.this, "Sorry...Values cannot be empty!", Toast.LENGTH_LONG).show();
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
                        Intent intent = new Intent(AddDebt.this, ViewClient.class);
                        startActivity(intent);
                    }
                }
        );
    }
    public void viewProducts() {
        btnViewProducts.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AddDebt.this, DisplayProducts.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, prices[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}