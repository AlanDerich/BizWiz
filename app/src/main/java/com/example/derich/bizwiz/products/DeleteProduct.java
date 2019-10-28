package com.example.derich.bizwiz.products;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;

public class DeleteProduct extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mListPro;
    Button delete_product_btn,viewAll;
    Spinner delete_product_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);
        databaseHelper = new DatabaseHelper(this) ;
        delete_product_btn = findViewById(R.id.button_delete_product);
        delete_product_spinner = findViewById(R.id.spinner_delete_product);
        viewAll = findViewById(R.id.button_delete_product_view_all);
        DatabaseHelper databaseHelper= new DatabaseHelper(this);
        mListPro = databaseHelper.getAllProducts();
        Spinner spinner= findViewById(R.id.spinner_delete_product);
        mAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, mListPro);
        spinner.setAdapter(mAdapter);
        DeleteProductMethod();
        viewAllProducts();

    }

    public void DeleteProductMethod() {
        delete_product_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (delete_product_spinner.getSelectedItem() != null) {
                            databaseHelper = new DatabaseHelper(getApplicationContext());
                            sqLiteDatabase = databaseHelper.getReadableDatabase();
                            final String product_name = delete_product_spinner.getSelectedItem().toString();
                            databaseHelper.deleteProduct(product_name);
                            AlertDialog.Builder builder
                                    = new AlertDialog
                                    .Builder(DeleteProduct.this);

                            builder.setMessage("Delete " + product_name + "?");

                            builder.setTitle("Alert !");

                            // Set Cancelable false
                            // for when the user clicks on the outside
                            // the Dialog Box then it will remain show
                            builder.setCancelable(false);

                            // Set the positive button with yes name
                            // OnClickListener method is use of
                            // DialogInterface interface.

                            builder
                                    .setPositiveButton(
                                            "Yes",
                                            new DialogInterface
                                                    .OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which)
                                                {

                                                    int deleted = databaseHelper.clientDeleted(product_name);

                                                    if (!(deleted > 0)) {

                                                        Toast.makeText(DeleteProduct.this, "Product Deleted successfully", Toast.LENGTH_LONG).show();
                                                        refreshList();
                                                    } else {
                                                        Toast.makeText(DeleteProduct.this, "Product not Deleted", Toast.LENGTH_LONG).show();
                                                        refreshList();
                                                    }
                                                }
                                            });

                            // Set the Negative button with No name
                            // OnClickListener method is use
                            // of DialogInterface interface.
                            builder
                                    .setNegativeButton(
                                            "No",
                                            new DialogInterface
                                                    .OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which)
                                                {

                                                    // If user click no
                                                    // then dialog box is canceled.
                                                    dialog.cancel();
                                                }
                                            });

                            // Create the Alert dialog
                            AlertDialog alertDialog = builder.create();

                            // Show the Alert Dialog box
                            alertDialog.show();



                        } else {
                            Toast.makeText(DeleteProduct.this, "Sorry...No product selected!", Toast.LENGTH_LONG).show();
                        }
                    }



                }
        );
    }



    public void viewAllProducts() {
        viewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DeleteProduct.this, DisplayProducts.class);
                        startActivity(intent);
                    }
                }
        );
    }


    private void refreshList() {
        mAdapter.clear();
        mListPro = databaseHelper.getAllProducts();
        mAdapter.addAll(mListPro);
        mAdapter.notifyDataSetChanged();
    }
}