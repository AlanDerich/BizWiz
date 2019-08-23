package com.example.derich.bizwiz.mpesa;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddedCash extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_CASH = 0;
    EditText amount, comment;
    DatabaseHelper myDb;
    Button btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_cash);
        getLoaderManager().initLoader(LOADER_CASH,null,this);
        btn_insert = findViewById(R.id.button_added_cash);
        amount = findViewById(R.id.editText_added_cash);
        comment = findViewById(R.id.editText_added_cash_comment);

        addedCash();

    }


    public void addedCash(){
        btn_insert.setOnClickListener(new View.OnClickListener() {
         //   Date currentTime = Calendar.getInstance().getTime();

            @Override
            public void onClick(View v) {
                String addedAmount = amount.getText().toString().trim();
                String comments = comment.getText().toString().trim();
                if (!(addedAmount.isEmpty())) {
                    Integer addedCash = Integer.valueOf(addedAmount);
                    long timeMillis = System.currentTimeMillis();
                    insert(getDate(timeMillis),addedCash, comments);
                    amount.setText("");
                    comment.setText("");
                    Toast.makeText(AddedCash.this,"Added successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddedCash.this,"Amount cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String getDate(long milliseconds){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar vCalendar = Calendar.getInstance();
        vCalendar.setTimeInMillis(milliseconds);
        return sdf.format(vCalendar.getTime());
    }

    public void insert(String date,Integer addedCash,String comments) {
        //Your DB Helper
        SQLiteOpenHelper dbHelper = new DatabaseHelper(AddedCash.this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
        SimpleDateFormat sdfAdd = new SimpleDateFormat("HH:mm:ss");
        String currentDateandTimeOfAdd = sdfAdd.format(new Date());
        String currentDateandTime = sdf.format(new Date());
        String type = "A cash of  " + addedCash + " Ksh added.";
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        ContentValues contentValue1 = new ContentValues();
        contentValue.put(DATE_MILLIS, date);

        contentValue.put(COLUMN_ADDED_CASH, addedCash);
        contentValue.put(TIME_OF_TRANSACTION, currentDateandTimeOfAdd);
        contentValue.put(COLUMN_COMMENT, comments);
        contentValue.put(COLUMN_MPESA_STATUS, 0);

        contentValue.put(COLUMN_ADDED_FLOAT, 0);
        contentValue.put(COLUMN_CLOSING_CASH, 0);
        contentValue.put(COLUMN_OPENING_CASH, 0);
        contentValue.put(COLUMN_OPENING_FLOAT, 0);
        contentValue.put(COLUMN_REDUCTED_CASH, 0);
        contentValue.put(COLUMN_REDUCTED_FLOAT, 0);


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




    /*public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
*/


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
