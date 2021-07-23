package com.example.profile2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.profile2.Adapter.HostelOccupantAdapter;
import com.example.profile2.model.HostelOccupant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HostelOccupantAdapter mHostelOccupantAdapter;
    ArrayList<HostelOccupant> sHostelOccupants;
    FirebaseFirestore mFirebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        recyclerView = findViewById(R.id.hostelOccupantR);
        findViewById(R.id.admission).setOnClickListener(view -> {
            openChrome("http://rnu.edu.ng/rnu_admission/");
        });
        findViewById(R.id.news).setOnClickListener(view -> {
            openChrome("http://rnu.edu.ng/news.php");
        });
        loadData();
    }

    void openChrome(String uri) {
        CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
        customIntent.setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent));
        customIntent.setShowTitle(false);
        openCustomTab(this, customIntent.build(), Uri.parse(uri));
    }

    private void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        String packageName = "com.android.chrome";
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, uri);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    void loadData() {
        sHostelOccupants = new ArrayList<>();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mHostelOccupantAdapter = new HostelOccupantAdapter(this, sHostelOccupants);
        mFirebaseFirestore.collection("HostelOccupant").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot pQueryDocumentSnapshots) {
                List<DocumentSnapshot> li;
                li = pQueryDocumentSnapshots.getDocuments();
                if (!pQueryDocumentSnapshots.isEmpty()) {
                    String n = "", p = "", r = "", d = "", u = "";
                    for (DocumentSnapshot i : li) {
                        if (i.exists()) {
                            String hsname = (String) i.get("hostel name");
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
        });
    }
}