package com.example.derich.bizwiz.clients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.derich.bizwiz.R;

import java.util.List;

public class ClientAdapter  extends ArrayAdapter<Clients> {

    //storing all the names in the list
    private List<Clients> clients;

    //context object
    private Context context;

    //constructor
    public ClientAdapter(Context context, int resource, List<Clients> clients) {
        super(context, resource, clients);
        this.context = context;
        this.clients = clients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //getting the layoutinflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview items
        View listViewItem = inflater.inflate(R.layout.display_clients, null, false);
        TextView textViewfName = listViewItem.findViewById(R.id.textViewClientfName);
        TextView textViewDebt = listViewItem.findViewById(R.id.textViewClientsDebt);
        TextView textViewPhone = listViewItem.findViewById(R.id.textViewClientsPhone);
        TextView textViewEmail = listViewItem.findViewById(R.id.textViewClientsEmail);
        ImageView imageViewStatus = listViewItem.findViewById(R.id.imageViewStatus);

        //getting the current name
        Clients client = clients.get(position);

        //setting the name to textview
        textViewfName.setText("Full Name: " + client.getClient_fullName());
        textViewDebt.setText("Debt: " + client.getClient_debt());
        textViewPhone.setText("Number: 0" + client.getClient_number());
        textViewEmail.setText("Email: " + client.getClient_Email());

        //if the synced status is 0 displaying
        //queued icon
        //else displaying synced icon
        if (client.getStatus() == 0)
            imageViewStatus.setBackgroundResource(R.drawable.stopwatch);
        else
            imageViewStatus.setBackgroundResource(R.drawable.success);

        return listViewItem;
    }
}