package com.example.profile2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile2.model.Entry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdaptor mRecyclerViewAdaptor;
    FirebaseFirestore mFirebaseFirestore;
    List<Entry> mEntryList;
    ArrayAdapter<String> Aadapter;
    ArrayList<String> ListOFHostels, flist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        ListOFHostels = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEntryList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewAdaptor = new RecyclerViewAdaptor(MainActivity.this, mEntryList);

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
                                    n = (String) i.get("Name");
                                    ListOFHostels.add(n);
                                    p = (String) i.get("Phone");
                                    r = (String) i.getString("Rentp");
                                    d = (String) i.getString("Distance");
                                    add = (String) i.get("Address");
                                    hf = (String) i.get("HostelFor");
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
                                    mRecyclerViewAdaptor.dataChange(MainActivity.this, mEntryList);
                                } else
                                    break;

                            }
                            mRecyclerViewAdaptor.copy();
                        }
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.hostelfinder, menu);
        mRecyclerViewAdaptor.copy();
        MenuItem search = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSearchResult(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    getAllResult();
                }
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Signout: {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent i = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(i);
                finish();
                break;
            }
            case R.id.SRA: {
                sortRent();
                break;
            }
            case R.id.SDA: {
                sortdist();
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void sortdist() {
        for (int i = 0; i < mEntryList.size(); i++) {
            mEntryList.get(i).setR();
        }
        Collections.sort(mEntryList);
        mRecyclerViewAdaptor.notifyDataSetChanged();
    }

    private void sortRent() {
        for (int i = 0; i < mEntryList.size(); i++) {
            mEntryList.get(i).unsetR();
        }
        Collections.sort(mEntryList);
        mRecyclerViewAdaptor.notifyDataSetChanged();
    }


    void getSearchResult(String str) {
        List<Entry> finallist = new ArrayList<>();
        str = str.toLowerCase();
        for (int i = 0; i < mEntryList.size(); i++) {
            if (mEntryList.get(i).getName().toLowerCase().contains(str)) {
                finallist.add(mEntryList.get(i));
            }
        }
        mEntryList.clear();
        mEntryList.addAll(finallist);
        mRecyclerViewAdaptor.notifyDataSetChanged();
    }

    void getAllResult() {
        flist = new ArrayList<>();
        mEntryList.clear();
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
                                    n = (String) i.get("Name");
                                    ListOFHostels.add(n);
                                    p = (String) i.get("Phone");
                                    r = (String) i.getString("Rentp");
                                    d = (String) i.getString("Distance");
                                    add = (String) i.get("Address");
                                    hf = (String) i.get("HostelFor");
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
                                    mRecyclerViewAdaptor.dataChange(MainActivity.this, mEntryList);
                                } else
                                    break;

                            }

                        }
                    }
                });


    }

}
