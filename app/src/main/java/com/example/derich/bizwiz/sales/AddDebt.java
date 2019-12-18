package com.example.derich.bizwiz.sales;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.derich.bizwiz.products.DisplayProducts;
import com.example.derich.bizwiz.sql.DatabaseHelper;
import com.example.derich.bizwiz.utils.DateAndTime;

import java.util.ArrayList;

import static com.example.derich.bizwiz.utils.DateAndTime.currentDateandTime;
import static com.example.derich.bizwiz.utils.DateAndTime.currentTimeOfAdd;


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
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        editTextName.setAdapter(adapter);
        ArrayList<String> listProducts = databaseHelper.getAllProducts();
        ArrayAdapter<String> adapter1= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listProducts);
        ProductName.setAdapter(adapter1);
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
                        final String quantity = editTextQuantity.getText().toString();

                        if (editTextName.getSelectedItem() !=null && ProductName.getSelectedItem() != null && !(quantity.isEmpty())){
                        final String client_name = editTextName.getSelectedItem().toString();
                        final String productName = ProductName.getSelectedItem().toString();
                        final String productPrice = mSpinnerPrices.getSelectedItem().toString();
                        databaseHelper = new DatabaseHelper(getApplicationContext());
                        sqLiteDatabase = databaseHelper.getReadableDatabase();
                        int previousBal=databaseHelper.previousBal(productName);
                        int previousDebt=databaseHelper.previousDebt(client_name);
                        int previousUnsyncedDebt=databaseHelper.previousUnsyncedDebt(client_name);
                        int previousSoldSales = databaseHelper.previousSoldBal(productName);
                        final int soldSales = previousSoldSales - Integer.valueOf(quantity);

                        int syncStatus = databaseHelper.syncStatus(client_name);

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
                                final int new_value = previousBal - Integer.valueOf(quantity);
                            final int new_debt = previousDebt + mAmount;
                            final int new_unsynced_debt = previousUnsyncedDebt + mAmount;

                            final String type = mAmount + " Ksh added to " + client_name + " debt " + " from " + productName + " ." + new_value + " " +  productName + " left." ;

                            if (new_value > -1) {
                                AlertDialog.Builder builder
                                            = new AlertDialog
                                            .Builder(AddDebt.this);

                                    builder.setMessage("Do you want to add a debt of " + mAmount +" ksh to " +client_name +" ?");


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
                                                            boolean Update = databaseHelper.updateData(client_name, String.valueOf(new_unsynced_debt), String.valueOf(new_debt), productName, String.valueOf(new_value),String.valueOf(soldSales), currentDateandTime, type, PreferenceHelper.getUsername());
                                                            boolean updateSales = databaseHelper.insertDebtSales((String.valueOf(mAmount )),PreferenceHelper.getUsername(), DateAndTime.getDate(), currentTimeOfAdd,productName,quantity);
                                                            if (Update && updateSales) {
                                                                Toast.makeText(AddDebt.this, "Data Updated", Toast.LENGTH_LONG).show();
                                                                emptyInputEditText();
                                                            } else {
                                                                Toast.makeText(AddDebt.this, "Data not Updated", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(AddDebt.this, "No items left", Toast.LENGTH_LONG).show();
                                emptyInputEditText();
                            }
                            }
                            else{
                                final int new_value = previousBal - Integer.valueOf(quantity);
                                final int new_debt = previousDebt + mAmount;
                                final int new_unsynced_debt = previousUnsyncedDebt + mAmount;
                                final String type = mAmount + " Ksh added to " + client_name + " debt " + " from " + productName + " ." + new_value + " " +  productName + " left." ;

                                if (new_value > -1) {
                                    AlertDialog.Builder builder
                                            = new AlertDialog
                                            .Builder(AddDebt.this);

                                    builder.setMessage("Do you want to add a debt of " + mAmount +" ksh to " +client_name +" ?");


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
                                                            boolean Update = databaseHelper.updateData(client_name, String.valueOf(new_unsynced_debt), String.valueOf(new_debt), productName, String.valueOf(new_value),String.valueOf(soldSales), currentDateandTime, type, PreferenceHelper.getUsername());
                                                            boolean updateSales = databaseHelper.insertDebtSales((String.valueOf(mAmount)),PreferenceHelper.getUsername(),DateAndTime.getDate(), currentTimeOfAdd,productName,quantity);
                                                            if (Update && updateSales) {
                                                                Toast.makeText(AddDebt.this, "Data Updated", Toast.LENGTH_LONG).show();
                                                                emptyInputEditText();
                                                            } else {
                                                                Toast.makeText(AddDebt.this, "Data not Updated", Toast.LENGTH_LONG).show();
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