package com.example.derich.bizwiz.products;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

public class ProductsOffered extends AppCompatActivity {
    DatabaseHelper myDb;
    Button AddProduct, updateStock, deleteProduct, viewProducts;
    public static SharedPreferences sharedPreferences;
    private String Admin = "Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_offered);
        AddProduct = findViewById(R.id.add_new_product);
        updateStock = findViewById(R.id.Update_products);
        deleteProduct = findViewById(R.id.delete_product_btn);
        viewProducts = findViewById(R.id.view_products_btn);
        myDb = new DatabaseHelper(this);

        sharedPreferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        String administrator = PreferenceHelper.getUsername();

        if (administrator.equals(Admin)){
            deleteProduct.setVisibility(View.VISIBLE);
        }
        else{
            deleteProduct.setVisibility(View.GONE);
        }



        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsOffered.this, BackupData.class);
                startActivity(intent);
            }
        });
        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsOffered.this, DeleteProduct.class);
                startActivity(intent);
            }
        });
        updateStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsOffered.this, UpdateProducts.class);
                startActivity(intent);
            }
        });
        viewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsOffered.this, DisplayProducts.class);
                startActivity(intent);
            }
        });
    }




}