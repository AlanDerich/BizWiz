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
import android.widget.TextView;
import android.widget.Toast;

import com.example.derich.bizwiz.PreferenceHelper;
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
import static com.example.derich.bizwiz.sql.DatabaseHelper.TRANSACTION_USER;

public class ClosingCash extends AppCompatActivity {
    EditText amount;
    DatabaseHelper myDb;
    Button btn_calculate;
    TextView closingCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closing_cash);
        amount = findViewById(R.id.editText_closingFloat);
        closingCash = findViewById(R.id.textView_closing_cash);
        btn_calculate = findViewById(R.id.button_calculate);
        calculateClosingCashButton();
    }
    public void calculateClosingCashButton() {
        long timeMillis = System.currentTimeMillis();
        if((getClosingCash(getDate(timeMillis) )!= 0))
        {
            int closingCassh = totalClosingCash(getDate(timeMillis));
            closingCash.setText("Today's closing cash is " + closingCassh);
            amount.setText("");
        }
        else {
            btn_calculate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long timeMillis = System.currentTimeMillis();
                    String closingCashe = amount.getText().toString().trim();
                    if (!(closingCashe.isEmpty())) {
                        insert(getDate(timeMillis), calculateClosingCash());
                        closingCash.setText("The expected closing cash is " + calculateClosingCash());
                        amount.setText("");
                    } else {
                        Toast.makeText(ClosingCash.this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

    }

    private int calculateClosingCash() {
        //expectedFloat = (openingFloat + Amount + addedCash + addedFloat) - (reductedFloat + reductedCash - closingCash)
        //expectedCash = (openingFloat + Amount + addedCash + addedFloat) - (reductedFloat + reductedCash - closingFloat)
        long timeMillis = System.currentTimeMillis();
        int openingCash = openingCash(getDate(timeMillis));
        int openingFloat = openingFloat(getDate(timeMillis));
        int totalAddedCash = totalAddedCash(getDate(timeMillis));
        int totalAddedFloat = totalAddedFloat(getDate(timeMillis));
        int reductedFloat = reductedFloat(getDate(timeMillis));
        int reductedCash = reductedCash(getDate(timeMillis));
        String closing = amount.getText().toString().trim();
        int closingFloat = Integer.valueOf(closing);
        int expectedCash;

        expectedCash = (openingCash + openingFloat + totalAddedCash + totalAddedFloat) - (reductedCash + reductedFloat + closingFloat);
        return expectedCash;

    }

    public int totalAddedFloat(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ClosingCash.this);
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

    public int totalAddedCash(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ClosingCash.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(added_cash) FROM mpesa WHERE date_in_millis=?", new String[]{date});
        int addedCash;
        if (cursor.moveToFirst()) {
            addedCash = cursor.getInt(0);
        } else {
            addedCash = 0;
        }
        cursor.close();
        return addedCash;
    }

    public int openingFloat(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ClosingCash.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(opening_float) FROM mpesa WHERE date_in_millis=?", new String[]{date});
        int opening_float;
        if (cursor.moveToFirst()) {
            opening_float = cursor.getInt(0);
        } else {
            opening_float = 0;
        }
        cursor.close();
        return opening_float;
    }

    public int openingCash(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ClosingCash.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(opening_cash) FROM mpesa WHERE date_in_millis=?", new String[]{date});
        int opening_cash;
        if (cursor.moveToFirst()) {
            opening_cash = cursor.getInt(0);
        } else {
            opening_cash = 0;
        }
        cursor.close();
        return opening_cash;
    }

    public int reductedFloat(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ClosingCash.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(reducted_float) FROM mpesa WHERE date_in_millis=?", new String[]{date});
        int reducted_float;
        if (cursor.moveToFirst()) {
            reducted_float = cursor.getInt(0);
        } else {
            reducted_float = 0;
        }
        cursor.close();
        return reducted_float;
    }

    public int reductedCash(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ClosingCash.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(reducted_cash) FROM mpesa WHERE date_in_millis=?", new String[]{date});
        int reducted_cash;
        if (cursor.moveToFirst()) {
            reducted_cash = cursor.getInt(0);
        } else {
            reducted_cash = 0;
        }
        cursor.close();
        return reducted_cash;
    }
    public int getClosingCash(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ClosingCash.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(closing_cash) FROM mpesa WHERE date_in_millis=?", new String[]{date});
        int closingCash;
        if (cursor.moveToFirst()) {
            closingCash = cursor.getInt(0);
        } else {
            closingCash = 0;
        }
        cursor.close();
        return closingCash;
    }

    public int totalClosingCash(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ClosingCash.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(closing_cash) FROM mpesa WHERE date_in_millis=?", new String[]{date});
        int closingCash;
        if (cursor.moveToFirst()) {
            closingCash = cursor.getInt(0);
        } else {
            closingCash = 0;
        }
        cursor.close();
        return closingCash;
    }

    public static String getDate(long milliseconds){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar vCalendar = Calendar.getInstance();
        vCalendar.setTimeInMillis(milliseconds);
        return sdf.format(vCalendar.getTime());
    }

    public void insert(String date,Integer closingCash) {
        SimpleDateFormat sdif = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
        String currentDateandTime = sdif.format(new Date());
        SimpleDateFormat sdfAdd = new SimpleDateFormat("HH:mm:ss");
        String currentDateandTimeOfAdd = sdfAdd.format(new Date());
        String type = date + " closing cash was " + closingCash + " Ksh.";
        //Your DB Helper
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ClosingCash.this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        ContentValues contentValue1 = new ContentValues();
        contentValue.put(DATE_MILLIS, date);
        contentValue.put(COLUMN_CLOSING_CASH, closingCash);
        contentValue.put(TIME_OF_TRANSACTION, currentDateandTimeOfAdd);
        contentValue.put(COLUMN_MPESA_STATUS, 0);

        contentValue.put(COLUMN_ADDED_CASH, 0);
        contentValue.put(COLUMN_OPENING_CASH, 0);
        contentValue.put(COLUMN_ADDED_FLOAT, 0);
        contentValue.put(COLUMN_OPENING_FLOAT, 0);
        contentValue.put(COLUMN_REDUCTED_CASH, 0);
        contentValue.put(COLUMN_REDUCTED_FLOAT, 0);
        contentValue.put(COLUMN_COMMENT,0);

        contentValue1.put(TRANSACTION_TYPE, type);
        contentValue1.put(TRANSACTION_DATE, currentDateandTime);
        contentValue1.put(TRANSACTION_USER, PreferenceHelper.getUsername());
        contentValue1.put(COLUMN_TRANSACTION_STATUS, 0);

        //insert data in DB
        database.insert(TABLE_MPESA,null,contentValue);
        database.insert(TABLE_TRANSACTIONS,null,contentValue1);


        //Close the DB connection.
        database.close();

    }
}
