package com.example.profile2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.profile2.Adapter.RecyclerViewAdaptor;
import com.example.profile2.model.Entry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HostelActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdaptor mRecyclerViewAdaptor;
    FirebaseFirestore mFirebaseFirestore;
    List<Entry> mEntryList;
    ArrayList<String> ListOFHostels, flist;
    String people;
    TextView ifEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        ListOFHostels = new ArrayList<>();
        mEntryList = new ArrayList<>();
        ifEmpty = findViewById(R.id.empty);
        recyclerView = (RecyclerView) findViewById(R.id.home);
        recyclerView.setHasFixedSize(true);
        people = getIntent().getExtras().getString("People");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewAdaptor = new RecyclerViewAdaptor(HostelActivity.this, mEntryList);
        loadData();
    }


    void loadData(){
        mFirebaseFirestore.collection("hostellist")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> li;
                        li = queryDocumentSnapshots.getDocuments();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String n = "", p = "", r = "", d = "";
                            String add, hf, nb, np, rp, rt, type, bed;
                            for (DocumentSnapshot i : li) {
                                if (i.exists()) {
                                    hf = (String) i.get("HostelFor");
                                    assert hf != null;
                                    if (hf.equals(people)) {
                                        n = (String) i.get("Name");
                                        ListOFHostels.add(n);
                                        p = (String) i.get("Phone");
                                        r = (String) i.getString("Rentp");
                                        d = (String) i.getString("Distance");
                                        add = (String) i.get("Address");
                                        nb = (String) i.get("NumberB");
                                        np = (String) i.get("NumberP");
                                        rp = (String) i.get("Rentp");
                                        rt = (String) i.get("Rentt");
                                        type = (String) i.get("Type");
                                        bed = (String) i.get("NoBedrooms");
                                        String uu = (String) i.get("Url");
                                        String lat = (String) i.get("Latitude");
                                        String lon = (String) i.get("Longitute");
                                        Entry entry = new Entry(n, p, r, d, R.drawable.ic_house, add, hf, nb, np, rp, rt, type, bed, uu, lat, lon);
                                        mEntryList.add(entry);
                                        recyclerView.setAdapter(mRecyclerViewAdaptor);
                                        mRecyclerViewAdaptor.dataChange(HostelActivity.this, mEntryList);
                                    }
                                } else
                                    break;
                            }
                            if (mEntryList.isEmpty()){
                                ifEmpty.setText("No Hostel Have been created for "+people);
                            }
                            mRecyclerViewAdaptor.copy();
                        }
                    }
                });
    }
}