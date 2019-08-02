package com.example.derich.bizwiz.mpesa;

import android.content.ContentValues;
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

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_REDUCTED_CASH;
import static com.example.derich.bizwiz.sql.DatabaseHelper.DATE_MILLIS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_MPESA;

public class ReductedCash extends AppCompatActivity {
    EditText amount;
    DatabaseHelper myDb;
    Button btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reducted_cash);
        btn_insert = findViewById(R.id.button_reducted_cash);
        amount = findViewById(R.id.editText_reducted_cash);
        reductedCash();
    }

    private void reductedCash() {
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String reductedAmount = amount.getText().toString().trim();
                if (!(reductedAmount.isEmpty())) {
                    Integer addedCash = Integer.valueOf(reductedAmount);
                    long timeMillis = System.currentTimeMillis();
                    insert(getDate(timeMillis), addedCash);
                    amount.setText("");
                    Toast.makeText(ReductedCash.this,"Added successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ReductedCash.this,"Amount cannot be empty",Toast.LENGTH_SHORT).show();
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
        SQLiteOpenHelper dbHelper = new DatabaseHelper(ReductedCash.this);


        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DATE_MILLIS, date);
        contentValue.put(COLUMN_REDUCTED_CASH, addedCash);

        //insert data in DB
        database.insert(TABLE_MPESA,null,contentValue);

        //Close the DB connection.
        database.close();

    }
}
