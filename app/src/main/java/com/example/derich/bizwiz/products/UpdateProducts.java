package com.example.derich.bizwiz.products;


import android.content.DialogInterface;
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
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;

import static com.example.derich.bizwiz.utils.DateAndTime.currentDateandTime;
import static com.example.derich.bizwiz.utils.DateAndTime.currentTimeOfAdd;
import static com.example.derich.bizwiz.utils.DateAndTime.getDate;


public class UpdateProducts extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText  editStock;
    Button btnAddStock;
    Spinner productName;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_products);
        myDb = new DatabaseHelper(this) ;
        productName = findViewById(R.id.product_namee);
        editStock = findViewById(R.id.added_qty);
        btnAddStock= findViewById(R.id.addStock);
        DatabaseHelper databaseHelper= new DatabaseHelper(this);
        ArrayList<String> listProducts = databaseHelper.getAllProducts();
        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(this, R.layout.spinner_layout,R.id.txt,listProducts);
        productName.setAdapter(adapter1);
        UpdateStock();

    }

    public void UpdateStock() {
        btnAddStock.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String stock = editStock.getText().toString();
                        if (productName.getSelectedItem() != null && !(stock.isEmpty())){
                        final String product_name = productName.getSelectedItem().toString();
                        databaseHelper = new DatabaseHelper(getApplicationContext());
                        sqLiteDatabase = databaseHelper.getReadableDatabase();
                        Float product_buying_price = myDb.getProductsPrice(product_name);
                        int previousStock=databaseHelper.previousBal(product_name);
                        int previousSoldSales = databaseHelper.previousSoldBal(product_name);
                        final int soldSales = previousSoldSales + Integer.valueOf(stock);
                        final Float amount = product_buying_price * Float.valueOf(stock);

                        if ( !(product_name.isEmpty()) && !(stock.isEmpty())) {

                            final int new_stock = Integer.valueOf(stock) + previousStock;
                            final String type = stock + " " + product_name +" added. New balance is " + new_stock  ;


                            if (Integer.valueOf(stock) > 0) {
                                AlertDialog.Builder builder
                                        = new AlertDialog
                                        .Builder(UpdateProducts.this);

                                builder.setMessage("Do you want to insert a  of " + stock + " "+ product_name + " items ?");


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
                                                        boolean Update = myDb.increaseQuantity(product_name, String.valueOf(new_stock),String.valueOf(soldSales), currentDateandTime, type, PreferenceHelper.getUsername());
                                                        myDb.insertSalesExpenses(String.valueOf(amount),PreferenceHelper.getUsername(), getDate(),currentTimeOfAdd,product_name,stock);
                                                        if (Update) {
                                                            Toast.makeText(UpdateProducts.this, "Quantities Updated", Toast.LENGTH_LONG).show();
                                                            emptyInputEditText();
                                                        } else {
                                                            Toast.makeText(UpdateProducts.this, "Quantities not Updated", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(UpdateProducts.this, "Error! Stock cannot be less than 1", Toast.LENGTH_LONG).show();
                                emptyInputEditText();
                            }
                        }
                        else {
                            Toast.makeText(UpdateProducts.this, "Sorry...Values cannot be empty!", Toast.LENGTH_LONG).show();
                            emptyInputEditText();
                        }

                    }
                        else{
                            Toast.makeText(UpdateProducts.this, "Sorry...No selected product!", Toast.LENGTH_LONG).show();
                            emptyInputEditText();
                        }
                    }


                }
        );
    }


    private void emptyInputEditText(){
        editStock.setText(null);
    }


    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}