package com.example.derich.bizwiz.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.mpesa.DataModel;
import com.example.derich.bizwiz.mpesa.MpesaAdapter;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MpesaLogs extends AppCompatActivity {
    DatabaseHelper database;
    RecyclerView recyclerView;
    MpesaAdapter recycler;
    Spinner spinnerLogs;
    List<DataModel> datamodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa_logs);
        datamodel =new ArrayList<DataModel>();
        recyclerView =  findViewById(R.id.recyclerView_log);
        database = new DatabaseHelper(MpesaLogs.this);
        spinnerLogs = findViewById(R.id.spinner_log);
        populateSpinner();
        if (spinnerLogs.getSelectedItem() != null){
        populateRecyclerView();}
        else{
            Toast.makeText(this,"Please select a date.",Toast.LENGTH_SHORT).show();
        }
        spinnerLogs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void populateSpinner(){
        ArrayList<String> listPro = database.getAllMpesaDates();
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, listPro);
        spinnerLogs.setAdapter(adapter);
    }
    public void populateRecyclerView(){
        String date = spinnerLogs.getSelectedItem().toString();
        datamodel=  database.getdata(date);
        recycler =new MpesaAdapter(datamodel);
        RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);
    }

    }
