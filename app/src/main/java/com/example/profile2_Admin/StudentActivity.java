package com.example.profile2_Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.profile2_Admin.Adapter.StudentAdapter;
import com.example.profile2_Admin.model.HostelOccupant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    StudentAdapter mStudentAdapter;
    ArrayList<HostelOccupant> sHostelOccupants;
    FirebaseFirestore mFirebaseFirestore;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        sHostelOccupants = new ArrayList<>();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.studentRecyclerview);
        loadDate();
    }

    void loadDate() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mStudentAdapter = new StudentAdapter(this, sHostelOccupants);
        mFirebaseFirestore.collection("HostelOccupant").get().addOnSuccessListener(pQueryDocumentSnapshots -> {
            List<DocumentSnapshot> li;
            li = pQueryDocumentSnapshots.getDocuments();
            if (!pQueryDocumentSnapshots.isEmpty()) {
                String n = "", p = "", r = "", d = "", u = "";
                for (DocumentSnapshot i1 : li) {
                    if (i1.exists()) {
                        String hsname = (String) i1.get("hostel name");
                        n = (String) i1.get("name");
                        r = (String) i1.get("level");
                        d = (String) i1.get("department");
                        p = (String) i1.get("phoneNum");
                        u = (String) i1.get("Url");
                        HostelOccupant sHostelOccupant = new HostelOccupant(n, d, r, u, p);
                        sHostelOccupants.add(sHostelOccupant);
                        recyclerView.setAdapter(mStudentAdapter);

                    }
                }
            }
        });
    }
}