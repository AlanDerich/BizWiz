package com.example.derich.bizwiz.products;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.activities.VolleySingleton;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_PRODUCT_ID;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_PRODUCT_NAME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_PRODUCTS;
import static com.example.derich.bizwiz.utils.DateAndTime.currentDateandTime;
import static com.example.derich.bizwiz.utils.DateAndTime.currentTimeOfAdd;
import static com.example.derich.bizwiz.utils.DateAndTime.getDate;

/**
 * Created by group 7 CS project on 3/11/18.
 */
public class AddProduct extends AppCompatActivity implements View.OnClickListener {

    /*
     * this is the url to our webservice
     * make sure you are using the ip instead of localhost
     * it will not work if you are using localhost
     * */
    public static final String URL_SAVE_PRODUCT = "http://alanderich.info/bizwiz/saveProduct.php";
    public static final String URL_SAVE_TRANSACTION = "http://alanderich.info/bizwiz/saveTransaction.php";
    public static final String URL_SAVE_USER = "http://alanderich.info/bizwiz/saveUsers.php";
    public static final String URL_SAVE_MPESA = "http://alanderich.info/bizwiz/saveMpesa.php";
    public static final String URL_SAVE_SALES = "http://alanderich.info/bizwiz/saveSales.php";
    public static final String URL_SAVE_REPORTS = "http://alanderich.info/bizwiz/saveReports.php";

    //database helper object
    private DatabaseHelper db;
    DatabaseHelper dbHelper;

    private EditText editTextName,editTextQuantity,editTextBuyingPrice,editTextRetailPrice,editTextWholesalePrice;
    private ListView listViewProducts;

    //List to store all the products
    private List<Products> products;

    //1 means data is synced and 0 means data is not synced
    public static final int PRODUCT_SYNCED_WITH_SERVER = 1;
    public static final int PRODUCT_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know whether the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "alanderich.info/bizwiz";

    //adapterobject for list view
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_product);

        //initializing views and objects
        db = new DatabaseHelper(this);
        dbHelper = new DatabaseHelper(this);
        products = new ArrayList<>();

        //View objects
        Button buttonSave = findViewById(R.id.buttonSave);
        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextBuyingPrice = findViewById(R.id.editTextProductBuyingPrice);
        editTextRetailPrice = findViewById(R.id.editTextProductRetailPrice);
        editTextWholesalePrice = findViewById(R.id.editTextProductWholesalePrice);
        listViewProducts = findViewById(R.id.listViewProducts);

        //adding click listener to button
        buttonSave.setOnClickListener(this);

        //calling the method to load all the stored products
        loadProducts();

        //the broadcast receiver to update sync status
        //loading the products again
        //Broadcast receiver to know the sync status
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the products again
                loadProducts();
            }
        };

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
    }

    /*
     * this method will
     * load the products from the database
     * with updated sync status
     * */
    private void loadProducts() {
        products.clear();
        String sql = "SELECT * FROM " + TABLE_PRODUCTS + " ORDER BY " + COLUMN_PRODUCT_ID + " ASC;";
        Cursor cursor = db.getProducts(sql);
        if (cursor.moveToFirst()) {
            do {
                Products product = new Products(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_BUYING_PRICE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_RETAIL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_WHOLESALE_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS))
                );
                products.add(product);
            } while (cursor.moveToNext());
        }

        productAdapter = new ProductAdapter(this, R.layout.display_products, products);
        listViewProducts.setAdapter(productAdapter);
    }

    /*
     * this method will simply refresh the list
     * */
    private void refreshList() {
        productAdapter.notifyDataSetChanged();
    }

    /*
     * this method is saving the products to ther server
     * */
    private void saveProductToServer() {


        final String product_name = editTextName.getText().toString().trim();
        final String quantity = editTextQuantity.getText().toString().trim();
        final String product_buying_price = editTextBuyingPrice.getText().toString().trim();
        final String product_retail_price = editTextRetailPrice.getText().toString().trim();
        final String product_wholesale_price = editTextWholesalePrice.getText().toString().trim();

        if (!(product_name.isEmpty()) && !(quantity.isEmpty()) && !(product_buying_price.isEmpty()) && !(product_retail_price.isEmpty()) && !(product_wholesale_price.isEmpty()))
        {
            if (!checkIfProductExists(product_name)){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Saving Product...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the product to sqlite with status synced
                                saveProductToLocalStorage(product_name,quantity,product_buying_price,product_retail_price,product_wholesale_price, PRODUCT_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveProductToLocalStorage(product_name,quantity,product_buying_price,product_retail_price,product_wholesale_price, PRODUCT_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //on error storing the product to sqlite with status unsynced
                        saveProductToLocalStorage(product_name,quantity,product_buying_price,product_retail_price,product_wholesale_price, PRODUCT_NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_name", product_name);
                params.put("quantity", quantity);
                params.put("product_buying_price", product_buying_price);
                params.put("product_retail_price", product_retail_price);
                params.put("product_wholesale_price", product_wholesale_price);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
            else {
                Toast.makeText(AddProduct.this, "Product already exists.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(AddProduct.this, "All fields are required.", Toast.LENGTH_LONG).show();
        }
    }


    //saving the product to local storage
    private void saveProductToLocalStorage(String product_name, String quantity,String product_buying_price,String product_retail_price,String product_wholesale_price, int status) {
        String type = "New product added. " + product_name +". Initial quantity is " + quantity;
        Float amount = Float.valueOf(product_buying_price) * Integer.valueOf(quantity);

        db.addProduct(product_name,quantity,product_buying_price,product_retail_price,product_wholesale_price, status, currentDateandTime, type, PreferenceHelper.getUsername());
        db.insertSalesExpenses(String.valueOf(amount),PreferenceHelper.getUsername(), getDate(), currentTimeOfAdd,product_name,quantity);
        Products n = new Products(product_name,quantity,product_buying_price,product_retail_price,product_wholesale_price, status);
        products.add(n);
        refreshList();
            editTextQuantity.setText("");
            editTextName.setText("");
            editTextBuyingPrice.setText("");
            editTextRetailPrice.setText("");
            editTextWholesalePrice.setText("");


    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(AddProduct.this);

        builder.setMessage("Do you want to insert the new product " + "  ?");


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
                                saveProductToServer();
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

    public boolean checkIfProductExists(String productName) {
        Cursor mCursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = COLUMN_PRODUCT_NAME + " = ? " ;
        String[] selectionArgs = {productName};

        String[] vColumns = {
                COLUMN_PRODUCT_NAME};
        mCursor = db.query(TABLE_PRODUCTS, vColumns, selection, selectionArgs, null, null, null);
        int cursorCount = mCursor.getCount();
        mCursor.close();
        db.close();

        return cursorCount > 0;
    }
}