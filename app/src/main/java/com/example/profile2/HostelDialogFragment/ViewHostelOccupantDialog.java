package com.example.profile2.HostelDialogFragment;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile2.Adapter.HostelOccupantAdapter;
import com.example.profile2.AddHostelActivity;
import com.example.profile2.R;
import com.example.profile2.model.HostelOccupant;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.example.profile2.AddHostelActivity.staticHostelName;

public class ViewHostelOccupantDialog extends DialogFragment {

    private String hostelName;

    public ViewHostelOccupantDialog(){}

    RecyclerView recyclerView;
    HostelOccupantAdapter mHostelOccupantAdapter;
    FirebaseFirestore mFirebaseFirestore;
    static ArrayList<HostelOccupant> sHostelOccupants;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.view_hostel_occupant_dialog, null);
        recyclerView = dialogView.findViewById(R.id.hostelOccupantR);
        recyclerView.setHasFixedSize(true);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        sHostelOccupants = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mHostelOccupantAdapter = new HostelOccupantAdapter(requireContext(), sHostelOccupants);
        mFirebaseFirestore.collection("HostelOccupant").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot pQueryDocumentSnapshots) {
                List<DocumentSnapshot> li;
                li = pQueryDocumentSnapshots.getDocuments();
                if (!pQueryDocumentSnapshots.isEmpty()) {
                    String n = "", p = "", r = "", d = "",u= "";
                    for (DocumentSnapshot i : li) {
                        if (i.exists()) {
                            String hsname =(String) i.get("hostel name");
                            if (hostelName.equals(hsname)){
                                n = (String) i.get("name");
                                r = (String) i.get("level");
                                d = (String) i.get("department");
                                p = (String) i.get("phoneNum");
                                u = (String) i.get("Url");
                                HostelOccupant sHostelOccupant = new HostelOccupant(n, d, r, u, p);
                                sHostelOccupants.add(sHostelOccupant);
                                recyclerView.setAdapter(mHostelOccupantAdapter);
                            }
                        }
                    }
                }
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setView(dialogView);
        return alertDialogBuilder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        assert args != null;
        hostelName = args.getString("hostelName");
    }
}
