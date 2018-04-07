package com.app.android.medicalapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohit Mehta on 4/7/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context context;

    ArrayList<String> medicine_name;
    ArrayList<String> med_company_name;


    class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView med_name;
        TextView company_name;

        public SearchViewHolder(View itemView) {
            super(itemView);
            med_name=(TextView) itemView.findViewById(R.id.med_name);
            company_name=(TextView) itemView.findViewById(R.id.company_name);
        }
    }

    public SearchAdapter(Context context, ArrayList<String> medicine_name, ArrayList<String> med_company_name) {
        this.context = context;
        this.medicine_name = medicine_name;
        this.med_company_name = med_company_name;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.search_list_items,parent,false);

        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {

        holder.med_name.setText(medicine_name.get(position));
        holder.company_name.setText(med_company_name.get(position));

    }



    @Override
    public int getItemCount() {
        return medicine_name.size();
    }
}
