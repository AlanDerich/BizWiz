package com.example.derich.bizwiz.clients;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.derich.bizwiz.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    public List<Contacts> cont;
    protected List<Contacts> originalList;
    protected Context context;
    Contacts list;
    private ArrayList<Contacts> arraylist;


    public ContactsRecyclerAdapter(Context context,LayoutInflater inflater, List<Contacts> items) {
        this.layoutInflater = inflater;
        this.cont = items;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(cont);
        this.originalList = cont;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_contacts_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        list = cont.get(position);
        String name = (list.getName());

        holder.title.setText(name);
        holder.phone.setText(list.getPhone());

    }

    @Override
    public int getItemCount() {
        return cont.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cont = (List<Contacts>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Contacts> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView phone;
        public TextView email;
        public LinearLayout contact_select_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            title =  itemView.findViewById(R.id.name);
            phone =  itemView.findViewById(R.id.no);
            contact_select_layout =  itemView.findViewById(R.id.contact_select_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String client_name = title.getText().toString().trim();
                    String client_phone = phone.getText().toString().trim();
                    Intent intent = new Intent(context, ClientsDetails.class);
                    intent.putExtra(ClientsDetails.CLIENT_NAME, client_name );
                    intent.putExtra(ClientsDetails.CLIENT_PHONE,client_phone);
                    context.startActivity(intent);
                }
            });

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    protected List<Contacts> getFilteredResults(String constraint) {
        List<Contacts> results = new ArrayList<>();

        for (Contacts item : originalList) {
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

}