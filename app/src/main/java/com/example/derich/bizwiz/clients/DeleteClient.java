package com.example.derich.bizwiz.clients;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;

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
        mAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.txt, mListPro);
        delete_client_spinner.setAdapter(mAdapter);
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
                            final String client_name = delete_client_spinner.getSelectedItem().toString();
                            databaseHelper.deleteClient(client_name);
                            AlertDialog.Builder builder
                                    = new AlertDialog
                                    .Builder(DeleteClient.this);

                            builder.setMessage("Delete " + client_name + "?");

                            builder.setTitle("Alert !");

                            // Set Cancelable false
                            // for when the user clicks on the outside
                            // the Dialog Box then it will remain show
                            builder.setCancelable(false);

                            // Set the positive button with yes name
                            // OnClickListener method is use of
                            // DialogInterface interface.

                            builder
                                    .setPositiveButton(
                                            "Yes",
                                            new DialogInterface
                                                    .OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which)
                                                {

                                                    int deleted = databaseHelper.clientDeleted(client_name);


                                                    if (!(deleted > 0)) {
                                                        Toast.makeText(DeleteClient.this, "Client Deleted successfully", Toast.LENGTH_LONG).show();
                                                        refreshList();
                                                    } else {
                                                        Toast.makeText(DeleteClient.this, "Client not Deleted", Toast.LENGTH_LONG).show();
                                                        refreshList();
                                                    }
                                                }
                                            });

                            // Set the Negative button with No name
                            // OnClickListener method is use
                            // of DialogInterface interface.
                            builder
                                    .setNegativeButton(
                                            "No",
                                            new DialogInterface
                                                    .OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which)
                                                {

                                                    // If user click no
                                                    // then dialog box is canceled.
                                                    dialog.cancel();
                                                }
                                            });

                            // Create the Alert dialog
                            AlertDialog alertDialog = builder.create();

                            // Show the Alert Dialog box
                            alertDialog.show();


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
                        Intent intent = new Intent(DeleteClient.this, ViewClient.class);
                        startActivity(intent);
                    }
                }
        );
    }

    private void refreshList() {
        mAdapter.clear();
        mListPro = databaseHelper.getAllClients();
        mAdapter.addAll(mListPro);
        mAdapter.notifyDataSetChanged();
    }
}