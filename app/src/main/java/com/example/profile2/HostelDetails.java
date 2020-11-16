package com.example.profile2;

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

import com.bumptech.glide.Glide;
import com.example.profile2.HostelDialogFragment.ViewHostelOccupantDialog;
import com.example.profile2.model.Entry;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HostelDetails extends AppCompatActivity {
    int Pick = 234;
    ImageView imageH;
    private StorageReference storageReference;
    TextView mHostelName, mHostelAddress, mHostelDistance, rentPerPerson, rentPerMonth, mHostelFor, mNumOfbed, mNumOfBathroom, nNumOfPersonOccupants, type;
    Button phone, Loc;
    private Uri filepath;

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
        findViewById(R.id.hostelOccupant).setOnClickListener(view ->{
            Bundle sBundle = new Bundle();
            sBundle.putString("Hostel name",e.getName());
            ViewHostelOccupantDialog sDialogFragment = new ViewHostelOccupantDialog();
            sDialogFragment.setArguments(sBundle);
            sDialogFragment.show(getSupportFragmentManager(),"Fragment");
        });
    }
}
