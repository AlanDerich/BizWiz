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

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_OPENING_CASH;
import static com.example.derich.bizwiz.sql.DatabaseHelper.DATE_MILLIS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_MPESA;

public class OpeningCash extends AppCompatActivity {
    EditText amount;
    DatabaseHelper myDb;
    Button btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_cash);
        amount = findViewById(R.id.editText_opening_cash);
        btn_insert = findViewById(R.id.button_opening_cash);
        long timeMillis = System.currentTimeMillis();
        if ((totalOpeningCash(getDate(timeMillis)))> 0){
            int openingCash = totalOpeningCash(getDate(timeMillis));

            Toast.makeText(this,"Today's opening cash of " + openingCash + " has already been inserted",Toast.LENGTH_LONG).show();
        }
        else {
            executeButton();
        }




    }

public void executeButton(){

    btn_insert.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String addedAmount = amount.getText().toString().trim();

            if (!(addedAmount.isEmpty())) {
                Integer addedCash = Integer.valueOf(addedAmount);
                long timeMillis = System.currentTimeMillis();
                insert(getDate(timeMillis), addedCash);
                amount.setText("");
                Toast.makeText(OpeningCash.this,"Added successfully",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(OpeningCash.this,"Amount cannot be empty",Toast.LENGTH_LONG).show();
            }
        }
    });
}


    public int totalOpeningCash(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(OpeningCash.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(opening_cash) FROM mpesa WHERE date_in_millis=?", new String[]{date});
        int openingCash;
        if (cursor.moveToFirst()) {
            openingCash = cursor.getInt(0);
        } else {
            openingCash = 0;
        }
        cursor.close();
        return openingCash;


    }

    public static String getDate(long milliseconds){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar vCalendar = Calendar.getInstance();
        vCalendar.setTimeInMillis(milliseconds);
        return sdf.format(vCalendar.getTime());
    }

    public void insert(String date,Integer openingCash) {
        //Your DB Helper
        SQLiteOpenHelper dbHelper = new DatabaseHelper(OpeningCash.this);


        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DATE_MILLIS, date);
        contentValue.put(COLUMN_OPENING_CASH, openingCash);

        //insert data in DB
        database.insert(TABLE_MPESA,null,contentValue);

        //Close the DB connection.
        database.close();

    }
}
