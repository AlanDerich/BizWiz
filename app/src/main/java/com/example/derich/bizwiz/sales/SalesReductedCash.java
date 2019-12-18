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

import static com.example.derich.bizwiz.utils.DateAndTime.currentDateandTime;
import static com.example.derich.bizwiz.utils.DateAndTime.currentTimeOfAdd;
import static com.example.derich.bizwiz.utils.DateAndTime.getDate;

public class SalesReductedCash extends AppCompatActivity {
    Button btnAdd;
    EditText editTextAmount;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_reducted_cash);
        btnAdd = findViewById(R.id.buttonReduct);
        editTextAmount = findViewById(R.id.salesReductCashEditText);
        db = new DatabaseHelper(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String reductedAmount = editTextAmount.getText().toString().trim();
                if (!(reductedAmount.isEmpty())){
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(SalesReductedCash.this);

                    builder.setMessage("Do you want to deduct " + reductedAmount + " ksh from today's cash ?");


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

                                            final String type = reductedAmount + " ksh deducted. " ;
                                            float cash = totalOpeningCash(getDate(),PreferenceHelper.getUsername());
                                            float reducted = Float.valueOf(reductedAmount);
                                            if ( reducted> 0){
                                                if ((cash - reducted) >= 0){
                                                    db.ReductCashSales(reductedAmount, PreferenceHelper.getUsername(),getDate(), currentTimeOfAdd,currentDateandTime,type);
                                                    editTextAmount.setText("");
                                                    Toast.makeText(SalesReductedCash.this,"Reducted successfully",Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(SalesReductedCash.this,"Reducted amount cannot be greater than total current cash which is " + cash,Toast.LENGTH_SHORT).show();
                                                    editTextAmount.setText("");
                                                }
                                            }
                                            else {
                                                Toast.makeText(SalesReductedCash.this,"Amount cannot be less than 0",Toast.LENGTH_SHORT).show();
                                            }
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
                    Toast.makeText(SalesReductedCash.this, "Sorry amount field cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public int totalOpeningCash(String date, String user) {
        SQLiteOpenHelper dbHelper = new DatabaseHelper(SalesReductedCash.this);
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

    }
