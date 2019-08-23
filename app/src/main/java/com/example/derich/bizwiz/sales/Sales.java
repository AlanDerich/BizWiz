package com.example.derich.bizwiz.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

/**
 * Created by group 7 on 3/27/17.
 */
public class Sales extends AppCompatActivity {
    DatabaseHelper myDb;
    Button paidSales,addDebt,clearDebt,stats,openingCash, dailyReport;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        paidSales = findViewById(R.id.paidSalesBtn);
        addDebt = findViewById(R.id.debt);
        clearDebt = findViewById(R.id.clearDebt);
        stats = findViewById(R.id.dailyStatisticsBtn);
        openingCash = findViewById(R.id.addOpeningCashSalesBtn);
        dailyReport = findViewById(R.id.dailyReport);
        myDb = new DatabaseHelper(this);


if (PreferenceHelper.getUsername().equals("Admin")){
    dailyReport.setVisibility(View.VISIBLE);
}
else {
    dailyReport.setVisibility(View.GONE);
}
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
    }


}