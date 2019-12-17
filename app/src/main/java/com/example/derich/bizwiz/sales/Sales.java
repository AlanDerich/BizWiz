package com.example.derich.bizwiz.sales;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.activities.SalesTransactions;
import com.example.derich.bizwiz.sql.DatabaseHelper;

/**
 * Created by group 7 on 3/27/17.
 */
public class Sales extends AppCompatActivity {
    DatabaseHelper myDb;
    Button paidSales,addDebt,clearDebt,stats,openingCash,reductCash, dailyReport,transactions,summary;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        paidSales = findViewById(R.id.paidSalesBtn);
        summary = findViewById(R.id.summaryBtn);
        addDebt = findViewById(R.id.debt);
        clearDebt = findViewById(R.id.clearDebt);
        stats = findViewById(R.id.dailyStatisticsBtn);
        openingCash = findViewById(R.id.addOpeningCashSalesBtn);
        transactions = findViewById(R.id.transactionsSales);
        dailyReport = findViewById(R.id.dailyReport);
        reductCash = findViewById(R.id.reductCash);
        myDb = new DatabaseHelper(this);



        paidSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales.this, PaidSale.class);
                startActivity(intent);
            }
        });
        if (PreferenceHelper.getUsername().equals("Admin")){
            summary.setVisibility(View.VISIBLE);
        }
        else {
            summary.setVisibility(View.GONE);
        }
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales.this,Summary.class);
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
        reductCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reduct = new Intent(Sales.this,SalesReductedCash.class);
                startActivity(reduct);
            }
        });
        clearDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales.this, ClearDebt.class);
                startActivity(intent);
            }
        });
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales.this, Statistics.class);
                startActivity(intent);
            }
        });
        openingCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales.this, OpeningCashSales.class);
                startActivity(intent);
            }
        });
        dailyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales.this, DailyReport.class);
                startActivity(intent);
            }
        });
        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales.this, SalesTransactions.class);
                startActivity(intent);
            }
        });
    }


}