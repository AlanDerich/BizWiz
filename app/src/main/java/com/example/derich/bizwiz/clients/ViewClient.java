package com.example.derich.bizwiz.clients;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_CLIENT_FULLNAME;
import static com.example.derich.bizwiz.sql.DatabaseHelper.COLUMN_NUMBER;

public class ViewClient  extends AppCompatActivity {
    DatabaseHelper myDB;
    public List<Clients> clients;
    private ClientAdapter clientAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_clientss);
        clients = new ArrayList<>();
        ListView listView = findViewById(R.id.listview);
        myDB = new DatabaseHelper(this);

        //populate an ArrayList<String> from the database and then view it
        clients.clear();
        Cursor cursor = myDB.getClients();
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
        listView.setAdapter(clientAdapter);
        }


    }

