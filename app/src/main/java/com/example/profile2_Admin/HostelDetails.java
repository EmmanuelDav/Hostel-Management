package com.example.profile2_Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.profile2_Admin.Adapter.StudentAdapter;
import com.example.profile2_Admin.model.Entry;
import com.example.profile2_Admin.model.HostelOccupant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HostelDetails extends AppCompatActivity {
    int Pick = 234;
    ImageView imageH;
    private StorageReference storageReference;
    TextView mHostelName, mHostelAddress, mHostelDistance, rentPerPerson, rentPerMonth, mHostelFor, mNumOfbed, mNumOfBathroom, nNumOfPersonOccupants, type;
    Button phone, Loc;
    private Uri filepath;
    RecyclerView recyclerView;
    StudentAdapter mStudentAdapter;
    ArrayList<HostelOccupant> sHostelOccupants;
    FirebaseFirestore mFirebaseFirestore;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pick && data.getData() != null) {
            filepath = data.getData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Loc = findViewById(R.id.LOCATION);
        storageReference = FirebaseStorage.getInstance().getReference();
        imageH = findViewById(R.id.imageH);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        mHostelName = findViewById(R.id.NameH);
        mHostelAddress = findViewById(R.id.add);
        mHostelDistance = findViewById(R.id.dist);
        rentPerPerson = findViewById(R.id.rp);
        rentPerMonth = findViewById(R.id.rt);
        mHostelFor = findViewById(R.id.hf);
        mNumOfBathroom = findViewById(R.id.nb);
        mNumOfbed = findViewById(R.id.bed);
        nNumOfPersonOccupants = findViewById(R.id.np);
        type = findViewById(R.id.type);
        phone = findViewById(R.id.phone);
        recyclerView = findViewById(R.id.hostelOccupantR);
        recyclerView.setHasFixedSize(true);
        final Entry e;
        Intent i = getIntent();
        e = (Entry) i.getSerializableExtra("Entry");
        mHostelName.setText(e.getName().toString());
        mHostelAddress.setText(e.getAdd().toString());
        String z = e.getDistance().toString();
        Glide.with(getApplicationContext()).load(e.getUrl()).placeholder(R.drawable.splash).into(imageH);
        mHostelDistance.setText(z.substring(9));
        rentPerPerson.setText(e.getRp().toString());
        rentPerMonth.setText(e.getRt().toString());
        mHostelFor.setText(e.getHf().toString());
        mNumOfBathroom.setText(e.getNb().toString());
        mNumOfbed.setText(e.getBed().toString());
        nNumOfPersonOccupants.setText(e.getNp().toString());
        type.setText(e.getType().toString());
        phone.setText(e.getPhone().toString());
        sHostelOccupants = new ArrayList<>();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mStudentAdapter = new StudentAdapter(this, sHostelOccupants);
        mFirebaseFirestore.collection("HostelOccupant").get().addOnSuccessListener(pQueryDocumentSnapshots -> {
            List<DocumentSnapshot> li;
            li = pQueryDocumentSnapshots.getDocuments();
            if (!pQueryDocumentSnapshots.isEmpty()) {
                String n = "", p = "", r = "", d = "",u= "",f = "",g ="";
                for (DocumentSnapshot i1 : li) {
                    if (i1.exists()) {
                        String hsname =(String) i1.get("hostel name");
                        if (e.getName().equals(hsname)){
                            n = (String) i1.get("name");
                            r = (String) i1.get("level");
                            d = (String) i1.get("department");
                            p = (String) i1.get("phoneNum");
                            u = (String) i1.get("Url");
                            f = (String) i1.get("state_of_origin");
                            g = (String) i1.get("Gender");
                            HostelOccupant sHostelOccupant = new HostelOccupant(n, d, r, u, p,f,g);
                            sHostelOccupants.add(sHostelOccupant);
                            recyclerView.setAdapter(mStudentAdapter);
                        }
                    }
                }
            }
        });

        Loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iloc = new Intent(HostelDetails.this, MapsActivity.class);
                iloc.putExtra("name", e.getName());
                startActivity(iloc);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String no = e.getPhone().toString();
                no = no.substring(7);
                Uri u = Uri.parse("tel:" + no);
                Intent ni = new Intent(Intent.ACTION_DIAL, u);
                try {
                    startActivity(ni);
                } catch (SecurityException ex) {
                    Toast.makeText(HostelDetails.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
