package com.example.derich.bizwiz.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

/**
 * Created by group 7 on 3/27/17.
 */
public class Sales extends AppCompatActivity {
    DatabaseHelper myDb;
    Button paidSales,addDebt,clearDebt;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        paidSales = findViewById(R.id.paidSalesBtn);
        addDebt = findViewById(R.id.debt);
        clearDebt = findViewById(R.id.clearDebt);
        myDb = new DatabaseHelper(this);



        paidSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales.this, PaidSale.class);
                startActivity(intent);
            }
        });
        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales.this, AddDebt.class);
                startActivity(intent);
            }
        });
        clearDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales.this, ClearDebt.class);
                startActivity(intent);
            }
        });
    }


}