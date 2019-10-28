package com.example.derich.bizwiz.syncFromServer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.derich.bizwiz.R;

import java.util.List;

public class UnsyncedDataAdapter extends RecyclerView.Adapter<UnsyncedDataAdapter.Myholder>{
    List<UnsyncedDataModel> dataModelArrayList;

    public UnsyncedDataAdapter(List<UnsyncedDataModel> dataModelArrayList) {
        this.dataModelArrayList = dataModelArrayList;
    }

    class Myholder extends RecyclerView.ViewHolder{
        TextView remainingTransactions, TransactionType;

        public Myholder(View itemView) {
            super(itemView);


            remainingTransactions =  itemView.findViewById(R.id.textView_number_remaining);
            TransactionType =  itemView.findViewById(R.id.textView_type_name);
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


