package com.example.derich.bizwiz.clients;

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
public class AllClients extends AppCompatActivity {
    DatabaseHelper myDb;
    Button viewClients,addDebt,addFromContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_clients);
        viewClients = findViewById(R.id.viewClients);
        addFromContacts = findViewById(R.id.addClientFromContacts);
        addDebt = findViewById(R.id.addDebtBtn);
        myDb = new DatabaseHelper(this);



        viewClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllClients.this, ViewClient.class);
                startActivity(intent);
            }
        });

        addFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(AllClients.this, importContactClient.class);
                startActivity(vIntent);
            }
        });
        addDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(AllClients.this, ClientsDetails.class);
                startActivity(vIntent);
            }
        });



    }
}