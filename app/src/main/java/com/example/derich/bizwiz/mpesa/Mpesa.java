package com.example.derich.bizwiz.mpesa;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Mpesa extends AppCompatActivity {
    Button add_float,add_cash,opening_float,opening_cash, reducted_float, reducted_cash, expected_closing_cash;
    TextView totalExpectedFloat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);
        opening_cash = findViewById(R.id.btn_opening_cash);
        opening_float = findViewById(R.id.btn_opening_float);
        add_float = findViewById(R.id.btn_added_float);
        add_cash = findViewById(R.id.btn_added_cash);
        reducted_cash = findViewById(R.id.btn_reducted_cash);
        reducted_float = findViewById(R.id.btn_reducted_float);
        expected_closing_cash = findViewById(R.id.btn_expected_closing_cash);
        totalExpectedFloat = findViewById(R.id.textView_expected_float);
        long timeMillis = System.currentTimeMillis();
        int totalAddedFloat = totalAddedFloat(getDate(timeMillis));
        totalExpectedFloat.setText(String.valueOf(totalAddedFloat));




        opening_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(Mpesa.this, OpeningCash.class);
                startActivity(vIntent);
            }
        });

        opening_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(Mpesa.this, OpeningFloat.class);
                startActivity(vIntent);
            }
        });

        add_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(Mpesa.this, AddedFloat.class);
                startActivity(vIntent);
            }
        });

        add_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(Mpesa.this, AddedCash.class);
                startActivity(vIntent);
            }
        });

        reducted_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(Mpesa.this, ReductedFloat.class);
                startActivity(vIntent);
            }
        });

        reducted_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(Mpesa.this, ReductedCash.class);
                startActivity(vIntent);
            }
        });

        expected_closing_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(Mpesa.this, ClosingCash.class);
                startActivity(vIntent);
            }
        });

    }

    public int totalAddedFloat(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(Mpesa.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(added_float) FROM mpesa WHERE date_in_millis=?", new String[]{date});
        int addedFloat;
        if (cursor.moveToFirst()) {
            addedFloat = cursor.getInt(0);
        } else {
            addedFloat = 0;
        }
        cursor.close();
        return addedFloat;


    }

    public static String getDate(long milliseconds){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar vCalendar = Calendar.getInstance();
        vCalendar.setTimeInMillis(milliseconds);
        return sdf.format(vCalendar.getTime());
    }
}
