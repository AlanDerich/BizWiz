package com.example.derich.bizwiz.syncFromServer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.derich.bizwiz.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> ID;
    ArrayList<String> C_Name;
    ArrayList<String> C_Debt;
    ArrayList<String> C_Number;
    ArrayList<String> C_Email;


    public ListAdapter(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> client_name,
            ArrayList<String> client_debt,
            ArrayList<String> client_number,
            ArrayList<String> client_email

    )
    {

        this.context = context2;
        this.ID = id;
        this.C_Name = client_name;
        this.C_Debt = client_debt;
        this.C_Number = client_number;
        this.C_Email = client_email;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return ID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            child = layoutInflater.inflate(R.layout.items, null);

            holder = new Holder();

            holder.Name_TextView = child.findViewById(R.id.textViewName);
            holder.Debt_TextView = child.findViewById(R.id.textViewDebt);
            holder.Number_TextView = child.findViewById(R.id.textViewNumber);
            holder.Email_TextView = child.findViewById(R.id.textViewEmail);

            child.setTag(holder);holder.Name_TextView = child.findViewById(R.id.textViewName);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.Name_TextView.setText(C_Name.get(position));
        holder.Debt_TextView.setText(C_Debt.get(position));
        holder.Number_TextView.setText(C_Number.get(position));
        holder.Email_TextView.setText(C_Email.get(position));

        return child;
    }

    public class Holder {

        TextView Name_TextView;
        TextView Debt_TextView;
        TextView Number_TextView;
        TextView Email_TextView;
    }

}
