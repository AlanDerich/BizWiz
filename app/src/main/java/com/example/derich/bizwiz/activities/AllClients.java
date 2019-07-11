package com.example.derich.bizwiz.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.SaveInfo;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

/**
 * Created by group 7 on 3/27/17.
 */
public class AllClients extends AppCompatActivity {
    DatabaseHelper myDb;
    Button viewClients;

    private TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_clients);
        viewClients = findViewById(R.id.viewClients);
        myDb = new DatabaseHelper(this);



        viewClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllClients.this, ViewClient.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addProduct(View view)
    {
        startActivity(new Intent(this,SaveInfo.class));

    }
    public void selectOption (View view) {
        String button_text;
        button_text = ((Button) view).getText().toString();
        if   (button_text.equals("Add Client")) {
            Intent intent = new Intent(this, ClientsDetails.class);
            startActivity(intent);
        }
        else if (button_text.equals("View Clients")) {
            Intent intent = new Intent(this, AllClients.class);
            startActivity(intent);
        }



    }
}