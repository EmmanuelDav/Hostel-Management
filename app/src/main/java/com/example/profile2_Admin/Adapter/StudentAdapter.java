package com.example.profile2_Admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile2_Admin.R;
import com.example.profile2_Admin.model.HostelOccupant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    @NonNull

    private Context context;
    List<HostelOccupant> mHostelOccupants;

    public StudentAdapter(@NonNull Context pContext, List<HostelOccupant> pHostelOccupants) {
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
        holder.depatment.setText(e.getDepartment());
        holder.level.setText(e.getLevel());
        holder.ph.setText(e.getPhoneNum());
        holder.name.setText(e.getName());
        holder.num.setText(String.valueOf(1+position));
        holder.state.setText(e.getStateOfOrigin());
        holder.gender.setText(e.getGender());
        Picasso.get().load(e.getPicture()).placeholder(R.drawable.logo11).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mHostelOccupants.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView level, depatment, ph, name, state, num, gender;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profilePic);
            depatment = itemView.findViewById(R.id.department);
            level = itemView.findViewById(R.id.Level);
            ph = itemView.findViewById(R.id.phoneNum);
            name = itemView.findViewById(R.id.name);
            state = itemView.findViewById(R.id.stateOfOrigin);
            num = itemView.findViewById(R.id.roomNum);
            gender = itemView.findViewById(R.id.gender);
        }
    }
}
