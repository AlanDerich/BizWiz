package com.example.derich.bizwiz.sales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.derich.bizwiz.R;

public class CustomAdapter extends BaseAdapter {
    Context mContext;
    String[] prices;
    LayoutInflater mInflater;

    public CustomAdapter(Context applicationContext,String[] prices){
        this.mContext = applicationContext;
        this.prices = prices;
        mInflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return prices.length;
    }

    @Override
    public Object getItem(int position) {
        return prices[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = mInflater.inflate(R.layout.spinner_layout,null);
        TextView names = view.findViewById(R.id.txt);
        names.setText(prices[position]);
        return view;
    }
}
