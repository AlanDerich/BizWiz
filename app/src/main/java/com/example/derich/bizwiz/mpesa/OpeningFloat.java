package com.example.derich.bizwiz.mpesa;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_ADDED_CASH;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_ADDED_FLOAT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_CLOSING_CASH;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_COMMENT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_MPESA_STATUS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_OPENING_CASH;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_OPENING_FLOAT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_REDUCTED_CASH;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_REDUCTED_FLOAT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_TRANSACTION_STATUS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.DATE_MILLIS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_MPESA;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_TRANSACTIONS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TIME_OF_TRANSACTION;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TRANSACTION_DATE;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TRANSACTION_TYPE;

public class OpeningFloat extends AppCompatActivity {
    EditText amount;
    DatabaseHelper myDb;
    Button btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_float);
        amount = findViewById(R.id.editText_opening_float);
        btn_insert = findViewById(R.id.button_opening_float);
        long timeMillis = System.currentTimeMillis();
        enterOpeningFloat();

    }

    private void enterOpeningFloat() {
        long timeMillis = System.currentTimeMillis();
        if ((totalOpeningFloat(getDate(timeMillis)))> 0){
            int openingFloat = totalOpeningFloat(getDate(timeMillis));

            Toast.makeText(this,"Today's opening float of " + openingFloat + " has already been inserted",Toast.LENGTH_LONG).show();
        }
        else {


        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addedAmount = amount.getText().toString().trim();
                if (!(addedAmount.isEmpty())){
                Integer addedCash = Integer.valueOf(addedAmount);
                long timeMillis = System.currentTimeMillis();
                insert( getDate(timeMillis),addedCash);
                amount.setText("");
                    Toast.makeText(OpeningFloat.this,"Added successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(OpeningFloat.this, "Sorry amount field cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }
    }

    public int totalOpeningFloat(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(OpeningFloat.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(opening_float) FROM mpesa WHERE date_in_millis=?", new String[]{date});
        int openingFloat;
        if (cursor.moveToFirst()) {
            openingFloat = cursor.getInt(0);
        } else {
            openingFloat = 0;
        }
        cursor.close();
        return openingFloat;


    }
    public static String getDate(long milliseconds){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar vCalendar = Calendar.getInstance();
        vCalendar.setTimeInMillis(milliseconds);
        return sdf.format(vCalendar.getTime());
    }

    public void insert(String date,Integer openingCash) {
        SimpleDateFormat sdif = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
        String currentDateandTime = sdif.format(new Date());
        SimpleDateFormat sdfAdd = new SimpleDateFormat("HH:mm:ss");
        String currentDateandTimeOfAdd = sdfAdd.format(new Date());
        String type = "An opening float of " + openingCash + " Ksh was added.";
        //Your DB Helper
        SQLiteOpenHelper dbHelper = new DatabaseHelper(OpeningFloat.this);


        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        ContentValues contentValue1 = new ContentValues();
        contentValue.put(DATE_MILLIS, date);
        contentValue.put(COLUMN_OPENING_FLOAT, openingCash);
        contentValue.put(TIME_OF_TRANSACTION, currentDateandTimeOfAdd);
        contentValue.put(COLUMN_MPESA_STATUS, 0);

        contentValue.put(COLUMN_ADDED_CASH, 0);
        contentValue.put(COLUMN_CLOSING_CASH, 0);
        contentValue.put(COLUMN_ADDED_FLOAT, 0);
        contentValue.put(COLUMN_OPENING_CASH, 0);
        contentValue.put(COLUMN_REDUCTED_CASH, 0);
        contentValue.put(COLUMN_REDUCTED_FLOAT, 0);
        contentValue.put(COLUMN_COMMENT,0);

        contentValue1.put(TRANSACTION_TYPE, type);
        contentValue1.put(TRANSACTION_DATE,currentDateandTime);
        contentValue1.put(COLUMN_TRANSACTION_STATUS, 0);

        //insert data in DB
        database.insert(TABLE_MPESA,null,contentValue);
        database.insert(TABLE_TRANSACTIONS,null,contentValue1);

        //Close the DB connection.
        database.close();

    }
}
