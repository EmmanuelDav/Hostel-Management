package com.example.profile2;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile2.model.HostelOccupant;

import java.util.ArrayList;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    RecyclerView recyclerView;
    public DialogFragment() {}
    HostelOccupantAdapter mHostelOccupantAdapter;
    ArrayList<HostelOccupant> sHostelOccupants;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog, null );
        sHostelOccupants = new ArrayList<>();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        recyclerView = v.findViewById(R.id.hostelOccupantR);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mHostelOccupantAdapter = new HostelOccupantAdapter(getActivity(), sHostelOccupants);
        recyclerView.setAdapter(mHostelOccupantAdapter);
        alertDialogBuilder.setView(v);
        sHostelOccupants.add(new HostelOccupant("Pascal Chidi", "Computer Science", "200", R.drawable.profile1, "07068789978"));
        sHostelOccupants.add(new HostelOccupant("Emmanuel Bisi", "Bio Chemistry", "100", R.drawable.profile2, "09068589975"));
        sHostelOccupants.add(new HostelOccupant("Amos Kingsley", "Food Administration", "300", R.drawable.profile3, "09068589975"));
        sHostelOccupants.add(new HostelOccupant("Amadi Chisom", "Nursing", "400", R.drawable.profile4, "09068589975"));
        sHostelOccupants.add(new HostelOccupant("Alwell Nnamdi", "Agro economics", "200", R.drawable.profile5, "09068589975"));
        return alertDialogBuilder.create();
    }
}