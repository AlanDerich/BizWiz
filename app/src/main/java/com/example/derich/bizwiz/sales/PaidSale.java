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
import com.example.derich.bizwiz.mpesa.ReductedCash;
import com.example.derich.bizwiz.products.DisplayProducts;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PaidSale extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseHelper myDb;
    EditText editQuantity;
    Spinner productName, mSpinnerPrices;
    Button btnViewAll;
    Button btnViewUpdate;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    String[] prices = {"Wholesale", "Retail"};

    private int mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_sale);
        myDb = new DatabaseHelper(this);
        productName = findViewById(R.id.product_name);
        mSpinnerPrices = findViewById(R.id.product_paid_price_spinner);
        editQuantity = findViewById(R.id.editText_Product_quantity);
        btnViewAll = findViewById(R.id.button_view_quantity);
        btnViewUpdate = findViewById(R.id.button_update_sales);
        DatabaseHelper databaseHelper= new DatabaseHelper(this);
        ArrayList<String> listProducts = databaseHelper.getAllProducts();
        Spinner spinner1= findViewById(R.id.product_name);
        ArrayAdapter<String> adapter1= new ArrayAdapter<>(this, R.layout.spinner_layout,R.id.txt,listProducts);
        spinner1.setAdapter(adapter1);

        mSpinnerPrices.setOnItemSelectedListener(this);
        CustomAdapter customAdapter= new CustomAdapter(PaidSale.this, prices);
        mSpinnerPrices.setAdapter(customAdapter);
        viewAll();
        UpdateData();


    }



    public void UpdateData() {
        btnViewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String value = editQuantity.getText().toString();
                        final String product_name = productName.getSelectedItem().toString();
                        String typeOfSale = mSpinnerPrices.getSelectedItem().toString();
                        if (productName.getSelectedItem() != null && mSpinnerPrices.getSelectedItem() != null && !(value.isEmpty())) {

                            databaseHelper = new DatabaseHelper(getApplicationContext());
                            sqLiteDatabase = databaseHelper.getReadableDatabase();
                            int previousBal = databaseHelper.previousBal(product_name);
                            int previousSoldSales = databaseHelper.previousSoldBal(product_name);
                            final int soldSales = previousSoldSales - Integer.valueOf(value);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                            final String currentDateandTime = sdf.format(new Date());
                            long timeMillis = System.currentTimeMillis();
                            final String date = ReductedCash.getDate(timeMillis);
                            SimpleDateFormat sdfAdd = new SimpleDateFormat("HH:mm:ss");
                            final String currentTimeOfAdd = sdfAdd.format(new Date());
                            if (typeOfSale.equals("Wholesale")){
                                int price = databaseHelper.productWholesalePrice(product_name);
                                mAmount = Integer.valueOf(value) * price;
                          //      final int previousWholesaleAmount = databaseHelper.previousWholesaleAmount(PreferenceHelper.getUsername(),date);



                            if (!(product_name.isEmpty()) && !(value.isEmpty())) {

                                final int new_value = previousBal - Integer.valueOf(value);
                                final String type = value + " " + product_name + " sold. " + new_value + " " + product_name + " remaining." + " Transacted by " + PreferenceHelper.getUsername();


                                if (new_value > -1) {
                                    AlertDialog.Builder builder
                                            = new AlertDialog
                                            .Builder(PaidSale.this);

                                    builder.setMessage("Do you want to add a wholesale sale of " + mAmount +" ksh on " +product_name +" ?");


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
                                                            boolean isUpdate = myDb.updateQuantity(product_name, String.valueOf(new_value),String.valueOf(soldSales), currentDateandTime, type, PreferenceHelper.getUsername());
                                                            boolean updateCashWholeSale = myDb.insertCashWholesaleSale(String.valueOf(mAmount ),PreferenceHelper.getUsername(),date,currentTimeOfAdd,product_name,value);
                                                            if (isUpdate == true && updateCashWholeSale) {
                                                                Toast.makeText(PaidSale.this, "Data Updated", Toast.LENGTH_LONG).show();

                                                                emptyInputEditText();
                                                            } else {
                                                                Toast.makeText(PaidSale.this, "Data not Updated", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(PaidSale.this, "No items left", Toast.LENGTH_LONG).show();
                                    emptyInputEditText();
                                }
                            } else {
                                Toast.makeText(PaidSale.this, "Sorry...Values cannot be empty!", Toast.LENGTH_LONG).show();
                                emptyInputEditText();
                            }

                            }
                            else {
                                int price = databaseHelper.productRetailPrice(product_name);
                                mAmount = Integer.valueOf(value) * price;
                          //      final int previousRetailAmount = databaseHelper.previousRetailAmount(PreferenceHelper.getUsername(),date);


                                if (!(product_name.isEmpty()) && !(value.isEmpty())) {

                                    final int new_value = previousBal - Integer.valueOf(value);
                                    final String type = value + " " + product_name + " sold. " + new_value + " " + product_name + " remaining." + " Transacted by " + PreferenceHelper.getUsername();


                                    if (new_value > -1) {
                                        AlertDialog.Builder builder
                                                = new AlertDialog
                                                .Builder(PaidSale.this);

                                        builder.setMessage("Do you want to add a retail sale of " + mAmount +" ksh on " +product_name +" ?");


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
                                                                boolean isUpdate = myDb.updateQuantity(product_name, String.valueOf(new_value), String.valueOf(soldSales),currentDateandTime, type, PreferenceHelper.getUsername());
                                                                boolean updateCashRetailSale = myDb.insertCashRetailSale(String.valueOf(mAmount),PreferenceHelper.getUsername(),date,currentTimeOfAdd,product_name,value);
                                                                if (isUpdate == true && updateCashRetailSale) {
                                                                    Toast.makeText(PaidSale.this, "Data Updated", Toast.LENGTH_LONG).show();

                                                                    emptyInputEditText();
                                                                } else {
                                                                    Toast.makeText(PaidSale.this, "Data not Updated", Toast.LENGTH_LONG).show();
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
                                        Toast.makeText(PaidSale.this, "No items left", Toast.LENGTH_LONG).show();
                                        emptyInputEditText();
                                    }
                                } else {
                                    Toast.makeText(PaidSale.this, "Sorry...Values cannot be empty!", Toast.LENGTH_LONG).show();
                                    emptyInputEditText();
                                }

                            }

                        }
                        else {
                            Toast.makeText(PaidSale.this, "Sorry...Values cannot be empty!", Toast.LENGTH_LONG).show();
                            emptyInputEditText();
                        }

                    }
                }
        );
    }


    private void emptyInputEditText() {
        editQuantity.setText(null);
    }

    public void viewAll() {
        btnViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PaidSale.this, DisplayProducts.class);
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