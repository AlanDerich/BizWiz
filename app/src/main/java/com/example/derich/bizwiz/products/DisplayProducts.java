package com.example.derich.bizwiz.products;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_PRODUCT_ID;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_PRODUCT_NAME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_STATUSS;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_PRODUCTS;

public class DisplayProducts extends AppCompatActivity {
    DatabaseHelper myDB;
    public List<Products> products;
    private ProductAdapter productAdapter;
    private Cursor mCursor;
    private String mSql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);
        products = new ArrayList<>();
        Intent intent = getIntent();
        String status = intent.getStringExtra("status");
        myDB = new DatabaseHelper(this);
        if (status == null){
            mSql = "SELECT * FROM " + TABLE_PRODUCTS + " ORDER BY " + COLUMN_PRODUCT_ID + " ASC;";
            mCursor = myDB.getProducts(mSql);
        }
        else {
            mSql = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_STATUSS + " =0 "+ " ORDER BY " + COLUMN_PRODUCT_ID + " ASC;";
            mCursor = myDB.getProducts(mSql);
        }
        ListView listView = findViewById(R.id.listviewProducts);

        //populate an ArrayList<String> from the database and then view it
        if (PreferenceHelper.getUsername().equals("Admin")){
        products.clear();

        if (mCursor.moveToFirst()) {
            do {
                Products product = new Products(
                        mCursor.getString(mCursor.getColumnIndex(COLUMN_PRODUCT_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)),
                        mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_BUYING_PRICE)),
                        mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_RETAIL_PRICE)),
                        mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_WHOLESALE_PRICE)),
                        mCursor.getInt(mCursor.getColumnIndex(COLUMN_STATUSS))
                );
                products.add(product);
            } while (mCursor.moveToNext());
        }

        productAdapter = new ProductAdapter(this, R.layout.display_products, products);
        listView.setAdapter(productAdapter);
    }
        else {
            products.clear();
            if (mCursor.moveToFirst()) {
                do {
                    Products product = new Products(
                            mCursor.getString(mCursor.getColumnIndex(COLUMN_PRODUCT_NAME)),
                            mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)),
                            "Null",
                            mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_RETAIL_PRICE)),
                            mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_WHOLESALE_PRICE)),
                            mCursor.getInt(mCursor.getColumnIndex(COLUMN_STATUSS))
                    );
                    products.add(product);
                } while (mCursor.moveToNext());
            }

            productAdapter = new ProductAdapter(this, R.layout.display_products, products);
            listView.setAdapter(productAdapter);
            mCursor.close();
        }
    }

}