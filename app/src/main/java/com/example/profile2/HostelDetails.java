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
import com.example.profile2.model.Entry;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HostelDetails extends AppCompatActivity {
    int Pick = 234;
    ImageView imageH;
    private StorageReference storageReference;
    TextView name, add, dist, rp, rt, hf, bed, nb, np, type;
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
        name = findViewById(R.id.NameH);
        add = findViewById(R.id.add);
        dist = findViewById(R.id.dist);
        rp = findViewById(R.id.rp);
        rt = findViewById(R.id.rt);
        hf = findViewById(R.id.hf);
        nb = findViewById(R.id.nb);
        bed = findViewById(R.id.bed);
        np = findViewById(R.id.np);
        type = findViewById(R.id.type);
        phone = findViewById(R.id.phone);
        final Entry e;
        Intent i = getIntent();
        e = (Entry) i.getSerializableExtra("Entry");
        name.setText(e.getName().toString());
        add.setText(e.getAdd().toString());
        String z = e.getDistance().toString();
        Glide.with(getApplicationContext()).load(e.getUrl()).placeholder(R.drawable.splash).into(imageH);
        dist.setText(z.substring(9));
        rp.setText(e.getRp().toString());
        rt.setText(e.getRt().toString());
        hf.setText(e.getHf().toString());
        nb.setText(e.getNb().toString());
        bed.setText(e.getBed().toString());
        np.setText(e.getNp().toString());
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
            DialogFragment sDialogFragment = new DialogFragment();
            sDialogFragment.show(getSupportFragmentManager(),"Fragment");
        });

    }

}
