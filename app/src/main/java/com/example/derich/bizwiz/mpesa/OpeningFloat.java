package com.example.derich.bizwiz.mpesa;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_OPENING_FLOAT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.DATE_MILLIS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_MPESA;

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
        if ((totalOpeningFloat(getDate(timeMillis)))> 0){
            int openingFloat = totalOpeningFloat(getDate(timeMillis));

            Toast.makeText(this,"Today's opening float of " + openingFloat + " has already been inserted",Toast.LENGTH_LONG).show();
        }
        else {
            enterOpeningFloat();
        }
    }

    private void enterOpeningFloat() {
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
        //Your DB Helper
        SQLiteOpenHelper dbHelper = new DatabaseHelper(OpeningFloat.this);


        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DATE_MILLIS, date);
        contentValue.put(COLUMN_OPENING_FLOAT, openingCash);

        //insert data in DB
        database.insert(TABLE_MPESA, null, contentValue);

        //Close the DB connection.
        database.close();

    }
}
