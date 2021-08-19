package com.example.profile2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Hostel_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_info);

        findViewById(R.id.kings_hostel).setOnClickListener(vew -> {
            Intent data = new Intent(this, HostelActivity.class);
            data.putExtra("People","Boys");
            startActivity(data);
        });
        findViewById(R.id.queens_hostel).setOnClickListener(view -> {

            Intent data = new Intent(this, HostelActivity.class);
            data.putExtra("People","Girls");
            startActivity(data);
        });
        findViewById(R.id.available_rooms).setOnClickListener(view -> {
            startActivity(new Intent(this, AvailableRoomsActivity.class));
        });
    }
}