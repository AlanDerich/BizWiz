package com.example.derich.bizwiz.clients;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sales.AddDebt;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_CLIENT_DEBT;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_CLIENT_FULLNAME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_CLIENT_ID;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_NUMBER;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_STATUS;

public class DeleteClient extends AppCompatActivity {

    Spinner delete_client_spinner;
    Button delete_client_btn, viewAll;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mListPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_client);
        databaseHelper = new DatabaseHelper(this) ;
        delete_client_btn = findViewById(R.id.button_delete);
        delete_client_spinner = findViewById(R.id.spinner_delete_client);
        viewAll = findViewById(R.id.button_view_allC);
        DatabaseHelper databaseHelper= new DatabaseHelper(this);
        mListPro = databaseHelper.getAllClients();
        Spinner spinner= findViewById(R.id.spinner_delete_client);
        mAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, mListPro);
        spinner.setAdapter(mAdapter);
        DeleteClientMethod();
        viewAllClients();

    }

    public void DeleteClientMethod() {
        delete_client_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = null;
                        if (delete_client_spinner.getSelectedItem() != null) {
                            databaseHelper = new DatabaseHelper(getApplicationContext());
                            sqLiteDatabase = databaseHelper.getReadableDatabase();
                            String client_name = delete_client_spinner.getSelectedItem().toString();
                            databaseHelper.deleteClient(client_name);
                            int deleted = databaseHelper.clientDeleted(client_name);


                            if (!(deleted > 0)) {
                                Toast.makeText(DeleteClient.this, "Client Deleted successfully", Toast.LENGTH_LONG).show();
                                refreshList();
                            } else {
                                Toast.makeText(DeleteClient.this, "Client not Deleted", Toast.LENGTH_LONG).show();
                                refreshList();
                            }
                        } else {
                            Toast.makeText(DeleteClient.this, "Sorry...No client selected!", Toast.LENGTH_LONG).show();
                        }
                    }



    }
                        );
                    }



    public void viewAllClients() {
        viewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = databaseHelper.getAllData();
                        if (res.getCount() == 0) {
                            showMessage("Error", "No clients found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :").append(res.getString(res.getColumnIndex(COLUMN_CLIENT_ID))).append("\n");
                            buffer.append("Name :").append(res.getString(res.getColumnIndex(COLUMN_CLIENT_FULLNAME))).append("\n");
                            buffer.append("Phone :").append(res.getString(res.getColumnIndex(COLUMN_NUMBER))).append("\n");
                            buffer.append("Debt Amount :").append(res.getString(res.getColumnIndex(COLUMN_CLIENT_DEBT))).append("\n");
                            buffer.append("Sync status :").append(res.getString(res.getColumnIndex(COLUMN_STATUS))).append("\n\n");
                        }

                        // Show all data
                        showMessage("Clients", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteClient.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
    private void refreshList() {
        mAdapter.clear();
        mListPro = databaseHelper.getAllClients();
        mAdapter.addAll(mListPro);
        mAdapter.notifyDataSetChanged();
    }
}