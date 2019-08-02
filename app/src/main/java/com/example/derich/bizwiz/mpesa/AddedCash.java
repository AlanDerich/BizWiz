package com.example.derich.bizwiz.mpesa;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
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

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_ADDED_CASH;
import static com.example.derich.bizwiz.sql.DatabaseHelper.DATE_MILLIS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_MPESA;

public class AddedCash extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_CASH = 0;
    EditText amount;
    DatabaseHelper myDb;
    Button btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_cash);
        getLoaderManager().initLoader(LOADER_CASH,null,this);
        btn_insert = findViewById(R.id.button_added_cash);
        amount = findViewById(R.id.editText_added_cash);
        addedCash();

    }


    public void addedCash(){
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

    public void insert(String date,Integer addedCash) {
        //Your DB Helper
        SQLiteOpenHelper dbHelper = new DatabaseHelper(AddedCash.this);


        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DATE_MILLIS, date);
        contentValue.put(COLUMN_ADDED_CASH, addedCash);

        //insert data in DB
        database.insert(TABLE_MPESA,null,contentValue);

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
