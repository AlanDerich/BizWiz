package com.example.derich.bizwiz.sales;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.derich.bizwiz.R;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.Myholder>{
    List<SummaryModel> summaryModelArrayList;
    public SummaryAdapter(List<SummaryModel> summaryModelArrayList) {
        this.summaryModelArrayList = summaryModelArrayList;
    }

    @NonNull
    @Override
    public SummaryAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_summary,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.Myholder holder, int position) {
        SummaryModel summaryModel = summaryModelArrayList.get(position);
        holder.textViewSummaryProductName.setText(summaryModel.getProductName());
        holder.textViewSummaryQuantity.setText(String.valueOf(summaryModel.getQuantitySold()));
        holder.textViewSummaryRetail.setText(String.valueOf(summaryModel.getRetailSales()));
        holder.textViewSummaryAdded.setText(String.valueOf(summaryModel.getAdded()));
        holder.textViewSummaryWholesale.setText(String.valueOf(summaryModel.getWholesaleSales()));
        holder.textViewSummaryExpected.setText(String.valueOf(summaryModel.getExpectedCash()));
    }

    @Override
    public int getItemCount() {
        return summaryModelArrayList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder{
        TextView textViewSummaryProductName,textViewSummaryQuantity,textViewSummaryRetail,textViewSummaryWholesale,textViewSummaryAdded,textViewSummaryExpected;
        public Myholder( View itemView) {
            super(itemView);
            textViewSummaryProductName = itemView.findViewById(R.id.textViewSummaryProductName);
            textViewSummaryQuantity = itemView.findViewById(R.id.textViewSummaryQuantity);
            textViewSummaryRetail = itemView.findViewById(R.id.textViewSummaryRetail);
            textViewSummaryAdded = itemView.findViewById(R.id.textViewSummaryAdded);
            textViewSummaryWholesale = itemView.findViewById(R.id.textViewSummaryWholesale);
            textViewSummaryExpected = itemView.findViewById(R.id.textViewSummaryExpected);
        }
    }
}
