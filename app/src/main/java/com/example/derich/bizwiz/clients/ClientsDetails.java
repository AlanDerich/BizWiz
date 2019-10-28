package com.example.derich.bizwiz.clients;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
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
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.activities.VolleySingleton;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.derich.bizwiz.PreferenceHelper.getUsername;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_CLIENT_FULLNAME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_NUMBER;
import static com.example.derich.bizwiz.sql.DatabaseHelper.TABLE_CLIENT;

/**
 * Created by group 7 CS project on 3/11/18.
 */
public class ClientsDetails extends AppCompatActivity implements View.OnClickListener {
    public static final String CLIENT_NAME = "com.example.derich.bizwiz.CLIENT_NAME";
    public static final String CLIENT_PHONE = "com.example.derich.bizwiz.CLIENT_PHONE";
    public static SharedPreferences sharedPreferences;

    public static final String URL_SAVE_CLIENT = "http://alanderich.info/bizwiz/saveClient.php";

    //database helper object
    private DatabaseHelper db;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper dbHelper;


    //View objects
    private Button buttonSave;
    private EditText editTextFullName;
    private EditText editTextClientDebt;
    private EditText editTextNumber;
    private EditText editTextClientEmail;
    private ListView listViewClients;

    //List to store all the clients
    public List<Clients> clients;

    //1 means data is synced and 0 means data is not synced
    public static final int CLIENT_SYNCED_WITH_SERVER = 1;
    public static final int CLIENT_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "alanderich.info/bizwiz";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    //adapterobject for list view
    private ClientAdapter clientAdapter;
    private boolean mIsNewClient;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_details);
        sharedPreferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
         user = getUsername();
        //initializing views and objects
        db = new DatabaseHelper(this);
        dbHelper = new DatabaseHelper(this);
        sqLiteDatabase = db.getReadableDatabase();
        clients = new ArrayList<>();

        buttonSave = findViewById(R.id.buttonSave);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextClientDebt = findViewById(R.id.editTextClientDebt);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextClientEmail = findViewById(R.id.editTextClientEmail);
        listViewClients = findViewById(R.id.listViewClients);
        readDisplayStateValues();

        //adding click listener to button
        buttonSave.setOnClickListener(this);

        //calling the method to load all the stored clients
        loadClients();

        //the broadcast receiver to update sync status
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the clients again
                loadClients();
            }
        };

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        String client_name = intent.getStringExtra(CLIENT_NAME);
        editTextFullName.setText(client_name);
        String client_phone = intent.getStringExtra(CLIENT_PHONE);
        editTextNumber.setText(client_phone);

        if(CLIENT_NAME.isEmpty()) {
            setEmpty();
        }

    }

    /*
     * this method will
     * load the clients from the database
     * with updated sync status
     * */
    private void loadClients() {
        clients.clear();
        Cursor cursor = db.getClients();
        if (cursor.moveToFirst()) {
            do {
                Clients client = new Clients(
                        cursor.getString(cursor.getColumnIndex(COLUMN_CLIENT_FULLNAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_DEBT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_EMAIL)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS))
                );
                clients.add(client);
            } while (cursor.moveToNext());
        }

        clientAdapter = new ClientAdapter(this, R.layout.display_clients, clients);
        listViewClients.setAdapter(clientAdapter);
    }

    /*
     * this method will simply refresh the list
     * */
    private void refreshList() {
        clientAdapter.notifyDataSetChanged();
    }

    /*
     * this method is saving the client to ther server
     * */
    private void saveClientToServer() {
        final String client_fullName = editTextFullName.getText().toString().trim();
        final String client_debt = editTextClientDebt.getText().toString().trim();
        final String Number = editTextNumber.getText().toString().trim();
        final String client_Email = editTextClientEmail.getText().toString().trim();
        final int client_added_debt = 0;

        if (!(client_fullName.isEmpty())) {
            if (!(Number.isEmpty() || !(client_Email.isEmpty())) ) {
                if (!checkClientDetails(client_fullName,Number)){
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Saving Client...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_CLIENT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (!obj.getBoolean("error")) {
                                        //if there is a success
                                        //storing the CLIENT to sqlite with status synced
                                        saveClientToLocalStorage(client_fullName, client_added_debt, client_debt, Number, client_Email, CLIENT_SYNCED_WITH_SERVER);
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                                        String currentDateandTime = sdf.format(new Date());
                                        String type = "Synced clients with server. ";
                                        db.syncAttempt(currentDateandTime,type,user);
                                    } else {
                                        //if there is some error
                                        //saving the name to sqlite with status unsynced
                                        saveClientToLocalStorage(client_fullName,client_added_debt, client_debt, Number, client_Email, CLIENT_NOT_SYNCED_WITH_SERVER);
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                                        String currentDateandTime = sdf.format(new Date());
                                        String type = "Did not Sync clients with server. ";
                                        db.syncAttempt(currentDateandTime,type,user);
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
                                //on error storing the client to sqlite with status unsynced
                                saveClientToLocalStorage(client_fullName, client_added_debt, client_debt, Number, client_Email, CLIENT_NOT_SYNCED_WITH_SERVER);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("client_fullName", client_fullName);
                        params.put("client_debt", client_debt);
                        params.put("Number", Number);
                        params.put("client_Email", client_Email);
                        return params;
                    }
                };

                VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
            }
                else {
                    Toast.makeText(ClientsDetails.this, "Client already exists.", Toast.LENGTH_LONG).show();
                }
            }
            else if (!(Number.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(client_Email).matches())){
                if (!checkClientDetails(client_fullName,Number)){
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Saving Client...");
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_CLIENT,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if (!obj.getBoolean("error")) {
                                            //if there is a success
                                            //storing the CLIENT to sqlite with status synced
                                            saveClientToLocalStorage(client_fullName, client_added_debt, client_debt, Number, client_Email, CLIENT_SYNCED_WITH_SERVER);
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                                            String currentDateandTime = sdf.format(new Date());
                                            String type = "Synced clients with server. ";
                                            db.syncAttempt(currentDateandTime,type,user);
                                        } else {
                                            //if there is some error
                                            //saving the name to sqlite with status unsynced
                                            saveClientToLocalStorage(client_fullName,client_added_debt, client_debt, Number, client_Email, CLIENT_NOT_SYNCED_WITH_SERVER);
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
                                            String currentDateandTime = sdf.format(new Date());
                                            String type = "Did not Sync clients with server. ";
                                            db.syncAttempt(currentDateandTime,type,user);
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
                                    //on error storing the client to sqlite with status unsynced
                                    saveClientToLocalStorage(client_fullName, client_added_debt, client_debt, Number, client_Email, CLIENT_NOT_SYNCED_WITH_SERVER);
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("client_fullName", client_fullName);
                            params.put("client_debt", client_debt);
                            params.put("Number", Number);
                            params.put("client_Email", client_Email);
                            return params;
                        }
                    };

                    VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
                }
                else {
                    Toast.makeText(ClientsDetails.this, "Client already exists.", Toast.LENGTH_LONG).show();
                }

            }

            else {
                Toast.makeText(ClientsDetails.this, "Please enter client's phone number or a valid email address.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(ClientsDetails.this, "Please enter client's Name.", Toast.LENGTH_LONG).show();

        }
    }

    //saving the client to local storage
    private void saveClientToLocalStorage(String client_fullName,int client_added_debt, String client_debt, String Number, String client_Email, int status) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());
        String type = "New client added. " + client_fullName + " by " + getUsername();


        db.addClient(client_fullName,client_added_debt, client_debt, Number, client_Email, status, currentDateandTime, type,getUsername());
        Clients n = new Clients(client_fullName, client_debt, Number, client_Email, status);
        clients.add(n);
        refreshList();
        setEmpty();
    }


    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(ClientsDetails.this);

        builder.setMessage("Do you want to insert the new client " + "  ?");


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
                                saveClientToServer();
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

    public boolean checkClientDetails(String fullNames, String phone) {
         Cursor mCursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = COLUMN_CLIENT_FULLNAME + " = ? " + " OR " + COLUMN_NUMBER + " = ? ";
        String[] selectionArgs = {fullNames, phone};

        String[] vColumns = {
                COLUMN_CLIENT_FULLNAME,
                COLUMN_NUMBER};
        mCursor = db.query(TABLE_CLIENT, vColumns, selection, selectionArgs, null, null, null);
        int cursorCount = mCursor.getCount();
        mCursor.close();
        db.close();

        return cursorCount > 0;
    }

    public void setEmpty(){
        editTextFullName.setText("");
        editTextClientDebt.setText("");
        editTextNumber.setText("");
        editTextClientEmail.setText("");
    }
}