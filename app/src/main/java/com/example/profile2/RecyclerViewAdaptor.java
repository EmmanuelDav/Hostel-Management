package com.example.profile2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.profile2.model.Entry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder> implements Filterable {
    @NonNull

    private Context context;
    public List<Entry> entryList, filterList;
    CustomFilter filter;
    private List<Entry> ForSearch;
    int pp;

    public void dataChange(Context context, List<Entry> entryList) {
        this.context = context;
        this.entryList = entryList;
        notifyDataSetChanged();
    }

    public RecyclerViewAdaptor(@NonNull Context context, List<Entry> entryList) {
        this.context = context;
        this.entryList = entryList;
        ForSearch = new ArrayList<>(this.entryList);
        this.filterList = entryList;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Entry e = entryList.get(position);
        pp = position;
        holder.name.setText(e.getName());
        holder.dist.setText(String.valueOf(e.getDistance()));
        holder.ph.setText(String.valueOf(e.getPhone()));
        holder.re.setText(String.valueOf(e.getRent()));
        Picasso.get().load(e.getUrl()).placeholder(R.drawable.ic_house).into(holder.image);
        holder.forr.setText("For:" + e.getHf());
        holder.c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, HostelDetails.class);
                i.putExtra("Entry", (Entry) e);
                context.startActivity(i);
            }
        });
    }

    public void copy() {
        ForSearch.clear();
        ForSearch.addAll(entryList);
    }

    public void changenow() {
        entryList.clear();
        entryList.addAll(ForSearch);
        for (Entry i : ForSearch) {
            Log.e("ForSerach", i.getName());
        }
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }
        return filter;
    }

//    @Override
//
//    private Filter exfilter=new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            List<Entry> flist=new ArrayList<>();
//            if(charSequence==null || charSequence.length()==0)
//            {
//                flist.addAll(entryList);
//            }else
//            {
//                String fPattern=charSequence.toString().toLowerCase().trim();
//                for(Entry item:entryList)
//                {
//                    if(item.getName().toLowerCase().contains(fPattern)){
//                        flist.add(item);
//                    }
//                }
//            }
//            FilterResults results=new FilterResults();
//            results.values=flist;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            entryList.clear();
//            entryList.addAll((List)filterResults.values);
//            notifyDataSetChanged();
//            changenow();
//        }
//    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, dist, ph, re, forr;
        CardView c;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.name);
            dist = itemView.findViewById(R.id.distance);
            ph = itemView.findViewById(R.id.phone);
            re = itemView.findViewById(R.id.rent);
            c = itemView.findViewById(R.id.card);
            forr = itemView.findViewById(R.id.For);
        }
    }
};
