package com.example.derich.bizwiz.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static java.lang.Integer.parseInt;
import static java.util.Date.parse;

public class PaidSale extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editQuantity;
    Spinner productName;
    Button btnViewAll;
    Button btnViewUpdate;
    Button btnSearchQuantity;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_sale);
        myDb = new DatabaseHelper(this);
        productName = findViewById(R.id.product_name);
        editQuantity = findViewById(R.id.editText_Product_quantity);
        btnViewAll = findViewById(R.id.button_view_quantity);
        btnViewUpdate = findViewById(R.id.button_update_sales);
        DatabaseHelper databaseHelper= new DatabaseHelper(this);
        ArrayList<String> listProducts = databaseHelper.getAllProducts();
        Spinner spinner1=(Spinner) findViewById(R.id.product_name);
        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(this, R.layout.spinner_layout1,R.id.txts,listProducts);
        spinner1.setAdapter(adapter1);
        viewAll();
        UpdateData();


    }



    public void UpdateData() {
        btnViewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String value = editQuantity.getText().toString();
                        String product_name = productName.getSelectedItem().toString();
                        databaseHelper = new DatabaseHelper(getApplicationContext());
                        sqLiteDatabase = databaseHelper.getReadableDatabase();
                        int previousBal=databaseHelper.previousBal(product_name);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                        String currentDateandTime = sdf.format(new Date());


                        if ( !(product_name.isEmpty()) && !(value.isEmpty())) {

                            int new_value = previousBal - Integer.valueOf(value);
                            String type = value +" "+ product_name + " sold. " +new_value +" " + product_name + " remaining." ;


                            if (new_value>0) {
                                boolean isUpdate = myDb.updateQuantity(product_name, String.valueOf(new_value), currentDateandTime , type);
                                if (isUpdate == true) {
                                    Toast.makeText(PaidSale.this, "Data Updated", Toast.LENGTH_LONG).show();

                                    emptyInputEditText();
                                } else {
                                    Toast.makeText(PaidSale.this, "Data not Updated", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(PaidSale.this, "No items left", Toast.LENGTH_LONG).show();
                                emptyInputEditText();
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
                        Cursor res = myDb.getAllQuantity();
                        if (res.getCount() == 0) {
                            showMessage("Error", "Nothing found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :").append(res.getString(0)).append("\n");
                            buffer.append("Name :").append(res.getString(1)).append("\n");
                            buffer.append("Quantity :").append(res.getString(2)).append("\n\n");
                        }

                        // Show all data
                        showMessage("Product Quantity", buffer.toString());
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