package com.example.derich.bizwiz.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

/**
 * Created by group 7 on 3/27/17.
 */
public class UsersActivity extends AppCompatActivity {
    DatabaseHelper myDb;

    private TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        myDb = new DatabaseHelper(this);

        textViewName = (TextView) findViewById(R.id.text1);
        String nameFromIntent = getIntent().getStringExtra("USERNAME");
        textViewName.setText("Welcome " + nameFromIntent + ". What would you like to do?");

    }
    public void selectOption (View view) {
        String button_text;
        button_text = ((Button) view).getText().toString();
        if (button_text.equals("check client's debt")) {
            Intent intent = new Intent(this, ClientsDebts.class);
            startActivity(intent);
        } else if (button_text.equals("check your business statistics")) {
            Intent intent = new Intent(this, BusinessStats.class);
            startActivity(intent);
        } else if (button_text.equals("backup your data")) {
            Intent intent = new Intent(this, BackupData.class);
            startActivity(intent);
        } else if (button_text.equals("Check your clients")) {
            Intent intent = new Intent(this, ClientsDetails.class);
            startActivity(intent);
        }
    }



    }