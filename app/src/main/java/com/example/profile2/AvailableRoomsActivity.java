package com.example.profile2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.profile2.Adapter.AvailableHostelAdapter;
import com.example.profile2.Adapter.RecyclerViewAdaptor;
import com.example.profile2.model.Entry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AvailableRoomsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AvailableHostelAdapter mRecyclerViewAdaptor;
    FirebaseFirestore mFirebaseFirestore;
    List<Entry> mEntryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_rooms_activty);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.available_recyclerview);
        recyclerView.setHasFixedSize(true);
        mEntryList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData();
    }

    void loadData() {
        mFirebaseFirestore.collection("hostellist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                                    n = (String) i.get("Name");
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
                                } else
                                    break;
                            }
                            mRecyclerViewAdaptor = new AvailableHostelAdapter(AvailableRoomsActivity.this, mEntryList,getSupportFragmentManager());
                            recyclerView.setAdapter(mRecyclerViewAdaptor);
                        }
                    }
                });
    }

}