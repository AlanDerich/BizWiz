package com.example.derich.bizwiz.mpesa;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

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
import static com.example.derich.bizwiz.utils.DateAndTime.currentDateandTime;
import static com.example.derich.bizwiz.utils.DateAndTime.currentTimeOfAdd;
import static com.example.derich.bizwiz.utils.DateAndTime.getDate;

public class OpeningFloat extends AppCompatActivity {
    EditText amount;
    Button btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_float);
        amount = findViewById(R.id.editText_opening_float);
        btn_insert = findViewById(R.id.button_opening_float);
        enterOpeningFloat();

    }

    private void enterOpeningFloat() {
        if ((totalOpeningFloat(getDate()))> 0){
            int openingFloat = totalOpeningFloat(getDate());

            Toast.makeText(this,"Today's opening float of " + openingFloat + " has already been inserted",Toast.LENGTH_LONG).show();
        }
        else {


        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String addedAmount = amount.getText().toString().trim();
                if (!(addedAmount.isEmpty())){
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(OpeningFloat.this);

                    builder.setMessage("Do you want to add an opening float of " + addedAmount + " Ksh ?");


                    builder.setTitle("Alert !");
                    builder.setCancelable(false);
                    builder
                            .setPositiveButton(
                                    "Yes",
                                    new DialogInterface
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which)
                                        {
                                            Integer addedCash = Integer.valueOf(addedAmount);
                                            insert( getDate(),addedCash);
                                            amount.setText("");
                                            Toast.makeText(OpeningFloat.this,"Added successfully",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                    builder
                            .setNegativeButton(
                                    "No",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which)
                                        {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
                else {
                    Toast.makeText(OpeningFloat.this, "Sorry amount field cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }
    }

    public int totalOpeningFloat(String date) {
        String[] params = new String[] {date};
        SQLiteOpenHelper dbHelper = new DatabaseHelper(OpeningFloat.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT SUM (DISTINCT opening_float) FROM " + TABLE_MPESA + " WHERE " + DATE_MILLIS + " = ? " + " ORDER BY " + DATE_MILLIS + " ASC;";
        Cursor cursorOpeningCash = db.rawQuery(sql,params);
        int opening_float;
        if (cursorOpeningCash.moveToFirst()) {
            opening_float = cursorOpeningCash.getInt(0);
        } else {
            opening_float = 0;
        }
        cursorOpeningCash.close();
        return opening_float;
    }

    public void insert(String date,Integer openingCash) {

        String type = "An opening float of " + openingCash + " Ksh was added.";
        //Your DB Helper
        SQLiteOpenHelper dbHelper = new DatabaseHelper(OpeningFloat.this);


        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        ContentValues contentValue1 = new ContentValues();
        contentValue.put(DATE_MILLIS, date);
        contentValue.put(COLUMN_OPENING_FLOAT, openingCash);
        contentValue.put(TIME_OF_TRANSACTION, currentTimeOfAdd);
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
        contentValue1.put(TRANSACTION_USER, PreferenceHelper.getUsername());
        contentValue1.put(COLUMN_TRANSACTION_STATUS, 0);

        //insert data in DB
        database.insert(TABLE_MPESA,null,contentValue);
        database.insert(TABLE_TRANSACTIONS,null,contentValue1);

        //Close the DB connection.
        database.close();

    }
}
