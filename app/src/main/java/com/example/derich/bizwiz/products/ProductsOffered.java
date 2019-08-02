package com.example.derich.bizwiz.products;

import android.content.Intent;
import android.os.Build;
import android.service.autofill.SaveInfo;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

public class ProductsOffered extends AppCompatActivity {
    DatabaseHelper myDb;
    Button AddProduct;

    private TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_offered);
        AddProduct = findViewById(R.id.add_new_product);
        myDb = new DatabaseHelper(this);



        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsOffered.this, BackupData.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addProduct(View view)
    {
        startActivity(new Intent(this, SaveInfo.class));

    }
    public void selectOption (View view) {
        String button_text;
        button_text = ((Button) view).getText().toString();
        if   (button_text.equals("Update Stock")) {
            Intent intent = new Intent(this, UpdateProducts.class);
            startActivity(intent);
        }
        else if (button_text.equals("Update Stock")) {
            Intent intent = new Intent(this, UpdateProducts.class);
            startActivity(intent);
        }


    }
}