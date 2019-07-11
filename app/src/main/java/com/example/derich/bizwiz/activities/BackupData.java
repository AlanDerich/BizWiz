package com.example.derich.bizwiz.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.getIntent;

/**
 * Created by group 7 CS project on 3/11/18.
 */
public class BackupData extends AppCompatActivity implements View.OnClickListener {

    /*
     * this is the url to our webservice
     * make sure you are using the ip instead of localhost
     * it will not work if you are using localhost
     * */
    public static final String URL_SAVE_PRODUCT = "http://alanderich.info/bizwiz/saveProduct.php";

    //database helper object
    private DatabaseHelper db;

    //View objects
    private Button buttonSave;
    private EditText editTextName;
    private String ProductName,ProductQuantity, ProductPrice;
    private EditText editTextQuantity;
    private EditText editTextPrice;
    private ListView listViewProducts;

    //List to store all the products
    private List<Products> products;

    //1 means data is synced and 0 means data is not synced
    public static final int PRODUCT_SYNCED_WITH_SERVER = 1;
    public static final int PRODUCT_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "alanderich.info/bizwiz";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    //adapterobject for list view
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String nameFromIntent = getIntent().getStringExtra("EMAIL");

        setContentView(R.layout.activity_backup_data);
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //initializing views and objects
        db = new DatabaseHelper(this);
        products = new ArrayList<>();

        buttonSave = findViewById(R.id.buttonSave);
        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextPrice = findViewById(R.id.editTextProductPrice);
        listViewProducts = findViewById(R.id.listViewProducts);

        //adding click listener to button
        buttonSave.setOnClickListener(this);

        //calling the method to load all the stored products
        loadProducts();

        //the broadcast receiver to update sync status
        broadcastReceiver = new BroadcastReceiver() {
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
        Cursor cursor = db.getProducts();
        if (cursor.moveToFirst()) {
            do {
                Products product = new Products(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_PRICE)),
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
        final String product_price = editTextPrice.getText().toString().trim();
        if (!(product_name.isEmpty()) && !(quantity.isEmpty()) && !(product_price.isEmpty()))
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Saving Product...");
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
                                saveProductToLocalStorage(product_name,quantity,product_price, PRODUCT_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveProductToLocalStorage(product_name,quantity,product_price, PRODUCT_NOT_SYNCED_WITH_SERVER);
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
                        saveProductToLocalStorage(product_name,quantity,product_price, PRODUCT_NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_name", product_name);
                params.put("Quantity", quantity);
                params.put("product_price", product_price);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
        else {
            Toast.makeText(BackupData.this, "All fields are required.", Toast.LENGTH_LONG).show();
        }
    }


    //saving the name to local storage
    private void saveProductToLocalStorage(String product_name, String quantity,String product_price, int status) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());
        String type = "New product added. " + product_name +". Initial quantity is " + quantity;

            ProductName = editTextName.getText().toString();
            ProductQuantity = editTextQuantity.getText().toString();
        ProductPrice = editTextPrice.getText().toString();

        db.addProduct(product_name,quantity,product_price, status, currentDateandTime, type);
        Products n = new Products(product_name,quantity,product_price, status);
        products.add(n);
        refreshList();
            editTextQuantity.setText("");
            editTextName.setText("");
            editTextPrice.setText("");


    }

    @Override
    public void onClick(View view) {
        saveProductToServer();
    }
}