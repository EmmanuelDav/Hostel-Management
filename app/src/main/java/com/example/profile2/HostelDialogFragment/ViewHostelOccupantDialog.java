package com.example.profile2.HostelDialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile2.Adapter.HostelOccupantAdapter;
import com.example.profile2.R;
import com.example.profile2.model.HostelOccupant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewHostelOccupantDialog extends androidx.fragment.app.DialogFragment {
    RecyclerView recyclerView;
    public ViewHostelOccupantDialog() {}
    HostelOccupantAdapter mHostelOccupantAdapter;
    ArrayList<HostelOccupant> sHostelOccupants;
    FirebaseFirestore mFirebaseFirestore;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle sBundle = getArguments();
        String hostelName = sBundle.getString("Hostel name");
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog, null );
        sHostelOccupants = new ArrayList<>();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        recyclerView = v.findViewById(R.id.hostelOccupantR);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mHostelOccupantAdapter = new HostelOccupantAdapter(getActivity(), sHostelOccupants);
        alertDialogBuilder.setView(v);
        mFirebaseFirestore.collection("HostelOccupant").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot pQueryDocumentSnapshots) {
                List<DocumentSnapshot> li;
                li = pQueryDocumentSnapshots.getDocuments();
                if (!pQueryDocumentSnapshots.isEmpty()) {
                    String n = "", p = "", r = "", d = "";
                    for (DocumentSnapshot i : li) {
                        if (i.exists()) {
                            String hsname =(String) i.get("hostel name");
                            if (hostelName.equals(hsname)){
                                n = (String) i.get("name");
                                r = (String) i.get("level");
                                d = (String) i.get("department");
                                p = (String) i.get("phoneNum");
                                HostelOccupant sHostelOccupant = new HostelOccupant(n, d, r, R.drawable.profile5, p);
                                sHostelOccupants.add(sHostelOccupant);
                                recyclerView.setAdapter(mHostelOccupantAdapter);
                            }
                        }
                    }
                }
            }
        });
        return alertDialogBuilder.create();
    }


}