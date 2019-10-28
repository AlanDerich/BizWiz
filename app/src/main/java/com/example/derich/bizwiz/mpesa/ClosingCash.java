package com.example.derich.bizwiz.mpesa;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    int addedFloat = 0;
    int addedCash =0;
    int reductedFloat=0;
    int reductedCash=0;
    int closingCashe=0;
    int openingCash=0;
    int openingFloat=0;
    Integer mExpectedCash = 0;

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

            closingCash.setText("Today's closing cash is " + mExpectedCash);
            amount.setText("");

        }
        else {
            btn_calculate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long timeMillis = System.currentTimeMillis();
                    String closingFloat = amount.getText().toString().trim();
                    if (!(closingFloat.isEmpty())) {
                        insert(getDate(timeMillis), calculateClosingCash(getDate(timeMillis)));
                        closingCash.setText("The expected closing cash is " + mExpectedCash);
                        amount.setText("");
                        mExpectedCash = 0;
                    } else {
                        Toast.makeText(ClosingCash.this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

    }


    public int calculateClosingCash(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ClosingCash.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT time_of_transaction FROM mpesa WHERE date_in_millis=?", new String[]{date});
        if (cursor.moveToFirst()) {
            do {
                String time = cursor.getString(cursor.getColumnIndexOrThrow(TIME_OF_TRANSACTION));
                String[] params = new String[]{date, time};
                String sqli = "SELECT date_in_millis,time_of_transaction,opening_float,opening_cash,added_float,added_cash,reducted_float,reducted_cash,closing_cash FROM " + TABLE_MPESA + " WHERE " + DATE_MILLIS + " = ? " + " AND " + TIME_OF_TRANSACTION + " = ? "+ " ORDER BY " + TIME_OF_TRANSACTION + " ASC;";
                Cursor cursorQUery = db.rawQuery(sqli,params);
            if (cursorQUery.moveToFirst()){
            do {
                String added_float = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_ADDED_FLOAT));
                addedFloat = Integer.valueOf(added_float) + addedFloat;
                String added_cash = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_ADDED_CASH));
                addedCash = Integer.valueOf(added_cash) + addedCash;
                String reducted_cash = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_REDUCTED_CASH));
                reductedCash = Integer.valueOf(reducted_cash) + reductedCash;
                String reducted_float = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_REDUCTED_FLOAT));
                reductedFloat = Integer.valueOf(reducted_float) + reductedFloat;
                String opening_cash = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_OPENING_CASH));
                openingCash = Integer.valueOf(opening_cash) + openingCash;
                String opening_float = cursorQUery.getString(cursorQUery.getColumnIndexOrThrow(COLUMN_OPENING_FLOAT));
                openingFloat = Integer.valueOf(opening_float) + openingFloat;
            }
            while (cursorQUery.moveToNext());
            }
            cursorQUery.close();
        }
            while (cursor.moveToNext());

    }
        String closing = amount.getText().toString().trim();
        int closingFloat = Integer.valueOf(closing);
         int reductions = reductedFloat + reductedCash + closingFloat;
        mExpectedCash = (addedCash + addedFloat + openingFloat+ openingCash) - reductions ;
         addedFloat = 0;
         addedCash =0;
         reductedFloat=0;
         reductedCash=0;
         closingCashe=0;
         openingCash =0;
         openingFloat =0;
         cursor.close();
        return mExpectedCash;
    }




    public int getClosingCash(String date) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ClosingCash.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT closing_cash FROM mpesa WHERE date_in_millis=?", new String[]{date});
        int closingCash;
        if (cursor.moveToFirst()) {
            closingCash = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLOSING_CASH));
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
        String comment = " Closing Float =" + amount.getText().toString();
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
        contentValue.put(COLUMN_COMMENT,comment);

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
