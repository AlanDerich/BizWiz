package com.example.derich.bizwiz.products;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_PRODUCT_NAME;

public class DisplayProducts extends AppCompatActivity {
    DatabaseHelper myDB;
    public List<Products> products;
    private ProductAdapter productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);
        products = new ArrayList<>();
        ListView listView = findViewById(R.id.listviewProducts);
        myDB = new DatabaseHelper(this);

        //populate an ArrayList<String> from the database and then view it
        products.clear();
        Cursor cursor = myDB.getProducts();
        if (cursor.moveToFirst()) {
            do {
                Products product = new Products(
                        cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_BUYING_PRICE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_RETAIL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_WHOLESALE_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUSS))
                );
                products.add(product);
            } while (cursor.moveToNext());
        }

        productAdapter = new ProductAdapter(this, R.layout.display_products, products);
        listView.setAdapter(productAdapter);
    }


}