package com.example.profile2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile2.model.HostelOccupant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HostelOccupantAdapter extends RecyclerView.Adapter<HostelOccupantAdapter.ViewHolder> {
    @NonNull

    private Context context;
    List<HostelOccupant> mHostelOccupants;

    public HostelOccupantAdapter(@NonNull Context pContext, List<HostelOccupant> pHostelOccupants) {
        context = pContext;
        mHostelOccupants = pHostelOccupants;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hostel_occupant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final HostelOccupant e = mHostelOccupants.get(position);
        holder.depatment.setText("Department : "+e.getDepartment());
        holder.level.setText("Level : "+e.getLevel());
        holder.ph.setText("Phone Number : "+ e.getPhoneNum());
        holder.name.setText("Name : "+e.getName());
        Picasso.get().load(e.getPicture()).placeholder(R.drawable.ic_house).into(holder.image);
    }



    @Override
    public int getItemCount() {
        return mHostelOccupants.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView level, depatment, ph, name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profilePic);
            depatment = itemView.findViewById(R.id.department);
            level = itemView.findViewById(R.id.Level);
            ph = itemView.findViewById(R.id.phoneNum);
            name = itemView.findViewById(R.id.name);
        }
    }
};
