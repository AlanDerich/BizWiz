package com.example.derich.bizwiz.products;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;

public class SpinnerProducts extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_products);
        DatabaseHelper databaseHelper= new DatabaseHelper(this);
        ArrayList<String> listProducts = databaseHelper.getAllProducts();
        Spinner spinner=(Spinner) findViewById(R.id.spinnerProducts);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner_layout,R.id.txt,listProducts);
        spinner.setAdapter(adapter);


    }
}
