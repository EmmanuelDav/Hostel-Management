package com.example.profile2.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile2.HostelDialogFragment.AddHostelFragmentDialog;
import com.example.profile2.HostelDialogFragment.ViewHostelOccupantDialog;
import com.example.profile2.R;
import com.example.profile2.model.Entry;
import com.example.profile2.model.HostelOccupant;
import com.itextpdf.text.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AvailableHostelAdapter extends RecyclerView.Adapter<AvailableHostelAdapter.ViewHolder> {

    private Context context;
    List<Entry> mHostelOccupants;
    FragmentManager manager;

    public AvailableHostelAdapter(@NonNull Context pContext, List<Entry> pHostelOccupants, FragmentManager manager) {
       this.context = pContext;
        mHostelOccupants = pHostelOccupants;
        this.manager = manager;
    }

    @NonNull
    @Override
    public AvailableHostelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.avalible_hostel, parent, false);
        return new AvailableHostelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableHostelAdapter.ViewHolder holder, int position) {
        Entry mHostelOccupant = mHostelOccupants.get(position);
        holder.hostelName.setText("Hostel name: "+mHostelOccupant.getName());
        holder.numOfOccupants.setText("num of Occupant: "+mHostelOccupant.getNp());
        holder.distance.setText(mHostelOccupant.getDistance());
        Picasso.get().load(mHostelOccupant.getImage()).into(holder.imageView);
        holder.view_hostel.setOnClickListener(view ->{
            ViewHostelOccupantDialog sAddHostelFragmentDialog = new ViewHostelOccupantDialog();
            Bundle mBundle = new Bundle();
            mBundle.putString("hostelName",mHostelOccupant.getName());
            sAddHostelFragmentDialog.setArguments(mBundle);
            sAddHostelFragmentDialog.show(manager, "Fragment");
        });
    }

    @Override
    public int getItemCount() {
        return mHostelOccupants.size();
    }

    protected class ViewHolder extends  RecyclerView.ViewHolder {
        TextView hostelName, numOfOccupants,distance;
        Button view_hostel;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hostelName = itemView.findViewById(R.id.name);
            distance = itemView.findViewById(R.id.distance);
            numOfOccupants = itemView.findViewById(R.id.occupant_num);
            view_hostel = itemView.findViewById(R.id.view_hostelOccupant);
            imageView = itemView.findViewById(R.id.hostel_image);
        }
    }
}
