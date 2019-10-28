package com.example.derich.bizwiz.mpesa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.derich.bizwiz.R;

import java.util.List;

public class MpesaAdapter extends RecyclerView.Adapter<MpesaAdapter.Myholder> {
    List<DataModel> dataModelArrayList;

    public MpesaAdapter(List<DataModel> dataModelArrayList) {
        this.dataModelArrayList = dataModelArrayList;
    }

    class Myholder extends RecyclerView.ViewHolder{
        TextView openingCash, comment, timeOfTransaction, TransactionType;
        ImageView syncStatus;

        public Myholder(View itemView) {
            super(itemView);

            openingCash =  itemView.findViewById(R.id.textView_transaction_amount_list);
            comment =  itemView.findViewById(R.id.textView_transaction_comment);
            timeOfTransaction =  itemView.findViewById(R.id.textView_transaction_time);
            syncStatus =  itemView.findViewById(R.id.imageViewStatusLog);
            TransactionType =  itemView.findViewById(R.id.textView_transaction_type);
        }
    }


    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mpesa_log_list,null);
        return new Myholder(view);

    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        DataModel dataModel=dataModelArrayList.get(position);
        holder.openingCash.setText(dataModel.getAmount());
        holder.comment.setText(dataModel.getComment());
        holder.timeOfTransaction.setText(dataModel.getTimeOfTransaction());
        holder.TransactionType.setText(dataModel.getTypeOfTransaction());
        int status = Integer.valueOf(dataModel.getSyncStatus());
        if (status == 0)
            holder.syncStatus.setBackgroundResource(R.drawable.stopwatch);
        else
            holder.syncStatus.setBackgroundResource(R.drawable.success);

    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

}








