package com.example.derich.bizwiz.products;


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
        Spinner spinner1=(Spinner) findViewById(R.id.product_namee);
        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(this, R.layout.spinner_layout1,R.id.txts,listProducts);
        spinner1.setAdapter(adapter1);
        UpdateStock();

    }

    public void UpdateStock() {
        btnAddStock.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stock = editStock.getText().toString();
                        String product_name = productName.getSelectedItem().toString();
                        databaseHelper = new DatabaseHelper(getApplicationContext());
                        sqLiteDatabase = databaseHelper.getReadableDatabase();
                        int previousStock=databaseHelper.previousBal(product_name);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                        String currentDateandTime = sdf.format(new Date());

                        if ( !(product_name.isEmpty()) && !(stock.isEmpty())) {

                            int new_stock = Integer.valueOf(stock) + previousStock;
                            String type = stock + " " + product_name +" added. New balance is " + new_stock  ;


                            if (Integer.valueOf(stock) > 0) {
                                boolean Update = myDb.updateQuantity(product_name, String.valueOf(new_stock), currentDateandTime, type);
                                if (Update == true) {
                                    Toast.makeText(UpdateProducts.this, "Quantities Updated", Toast.LENGTH_LONG).show();
                                    emptyInputEditText();
                                } else {
                                    Toast.makeText(UpdateProducts.this, "Quantities not Updated", Toast.LENGTH_LONG).show();
                                }
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