package com.example.derich.bizwiz.sales;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.derich.bizwiz.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.Myholder>{
    List<ReportModel> reportModelArrayList;

    public ReportAdapter(List<ReportModel> reportModelArrayList) {
        this.reportModelArrayList = reportModelArrayList;
    }
    public class Myholder extends RecyclerView.ViewHolder{
        TextView productName,soldItems,addedItems,expectedCash,remainingItems;
        public Myholder( View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name_report);
            soldItems = itemView.findViewById(R.id.sold_items_report);
            addedItems = itemView.findViewById(R.id.added_items_report);
            expectedCash = itemView.findViewById(R.id.expected_cash_report);
            remainingItems = itemView.findViewById(R.id.remaining_items_report);
        }
    }

    @Override
    public ReportAdapter.Myholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reports,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder( ReportAdapter.Myholder holder, int position) {
    ReportModel reportModel = reportModelArrayList.get(position);
    holder.productName.setText(reportModel.getProductName());
    holder.soldItems.setText(String.valueOf(reportModel.getSoldItems()));
    holder.addedItems.setText(String.valueOf(reportModel.getAddedItems()));
    holder.expectedCash.setText(String.valueOf(reportModel.getExpectedCash()));
    holder.remainingItems.setText(String.valueOf(reportModel.getRemainingItems()));
    }

    @Override
    public int getItemCount() {
        return reportModelArrayList.size();
    }


}
