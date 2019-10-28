package com.example.derich.bizwiz.sales;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OpeningCashSales extends AppCompatActivity {
    EditText amount;
    Button btn_insert;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_cash_sales);
        amount = findViewById(R.id.editText_opening_cash_sales);
        btn_insert = findViewById(R.id.button_opening_cash_sales);
        db = new DatabaseHelper(this);
        enterOpeningCash();

    }

    private void enterOpeningCash() {
        long timeMillis = System.currentTimeMillis();
        if ((totalOpeningCash(getDate(timeMillis), PreferenceHelper.getUsername()))> 0){
            int openingCash = totalOpeningCash(getDate(timeMillis),PreferenceHelper.getUsername());

            Toast.makeText(this,"Today opening cash of " + openingCash + " has already been inserted",Toast.LENGTH_LONG).show();
        }
        else {


            btn_insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String addedAmount = amount.getText().toString().trim();
                    if (!(addedAmount.isEmpty())){
                        final long timeMillis = System.currentTimeMillis();
                        SimpleDateFormat sdfAdd = new SimpleDateFormat("HH:mm:ss");
                        final String currentDateandTimeOfAdd = sdfAdd.format(new Date());
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(OpeningCashSales.this);

                        builder.setMessage("Do you want to add an opening cash of " + addedAmount + " ksh ?");


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
                                                db.insertOpeningCashSales(addedAmount,PreferenceHelper.getUsername(),getDate(timeMillis), currentDateandTimeOfAdd);
                                                amount.setText("");
                                                Toast.makeText(OpeningCashSales.this,"Added successfully",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(OpeningCashSales.this, "Sorry amount field cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public int totalOpeningCash(String date, String user) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(OpeningCashSales.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(opening_cash_sales) FROM sales WHERE sales_date=? AND sales_user=? ", new String[]{date, user});
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


}
