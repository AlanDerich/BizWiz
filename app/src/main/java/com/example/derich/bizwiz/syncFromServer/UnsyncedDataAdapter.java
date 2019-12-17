package com.example.derich.bizwiz.syncFromServer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.activities.MpesaLogs;
import com.example.derich.bizwiz.activities.Transactions;
import com.example.derich.bizwiz.clients.ViewClient;
import com.example.derich.bizwiz.credentials.ViewUsers;
import com.example.derich.bizwiz.products.DisplayProducts;
import com.example.derich.bizwiz.sales.DailyReport;

import java.util.List;

public class UnsyncedDataAdapter extends RecyclerView.Adapter<UnsyncedDataAdapter.Myholder>{
    List<UnsyncedDataModel> dataModelArrayList;
    protected Context context;

    public UnsyncedDataAdapter(Context context,List<UnsyncedDataModel> dataModelArrayList) {
        this.dataModelArrayList = dataModelArrayList;
        this.context = context;

    }

    class Myholder extends RecyclerView.ViewHolder{
        TextView remainingTransactions, TransactionType;

        public Myholder(final View itemView) {
            super(itemView);


            remainingTransactions =  itemView.findViewById(R.id.textView_number_remaining);
            TransactionType =  itemView.findViewById(R.id.textView_type_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type =TransactionType.getText().toString().trim();
                    if (type.equals("Clients")){
                    Intent intent = new Intent(context, ViewClient.class);
                    intent.putExtra("status","0");
                    context.startActivity(intent);
                    }
                    else if(type.equals("Products")){
                        Intent intent = new Intent(context, DisplayProducts.class);
                        intent.putExtra("status","0");
                        context.startActivity(intent);
                    }
                    else if(type.equals("Mpesa Transactions")){
                        Intent intent = new Intent(context, MpesaLogs.class);
                        intent.putExtra("status","0");
                        context.startActivity(intent);
                    }
                   /* else if(type.equals("Sales")){
                        Intent intent = new Intent(context, MpesaLogs.class);
                        intent.putExtra("status","0");
                        context.startActivity(intent);
                    }
                    */
                    else if(type.equals("Users")){
                        Intent intent = new Intent(context, ViewUsers.class);
                        intent.putExtra("status","0");
                        context.startActivity(intent);
                    }
                    else if(type.equals("Reports")){
                        Intent intent = new Intent(context, DailyReport.class);
                        intent.putExtra("status","0");
                        context.startActivity(intent);
                    }
                    else if(type.equals("Transactions Log")){
                        Intent intent = new Intent(context, Transactions.class);
                        intent.putExtra("status","0");
                        context.startActivity(intent);
                    }
                    else {
                        Toast.makeText(context,"Sorry an error occured!",Toast.LENGTH_LONG).show();
                    }
                    }
            });
        }
    }


    @Override
    public UnsyncedDataAdapter.Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_unsynced_data,null);
        return new UnsyncedDataAdapter.Myholder(view);

    }

    @Override
    public void onBindViewHolder(UnsyncedDataAdapter.Myholder holder, int position) {
        UnsyncedDataModel dataModel=dataModelArrayList.get(position);
        holder.remainingTransactions.setText(dataModel.getItemsRemaining());
        holder.TransactionType.setText(dataModel.getTransactionType());


    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

}


