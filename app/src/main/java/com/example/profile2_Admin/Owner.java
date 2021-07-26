package com.example.profile2_Admin;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile2_Admin.Adapter.StudentAdapter;
import com.example.profile2_Admin.model.HostelOccupant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Owner extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button SignOut;
    String Username;
    CardView addHostel;
    FirebaseAuth mAuth;
    LinearLayoutCompat aadminstration;

    Toolbar toolbar;
    NavigationView navigationMenu;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        addHostel = findViewById(R.id.AddHostel);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolBar);
        navigationMenu = findViewById(R.id.nav);
        drawerLayout = findViewById(R.id.drawerL);
        // recyclerView = findViewById(R.id.studentRecyclerView);
        aadminstration = findViewById(R.id.adminstration);
        Intent i = getIntent();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openNav, R.string.closeNav);
        drawerLayout.addDrawerListener(toggle);
        navigationMenu.setNavigationItemSelectedListener(this);
        Username = (String) i.getSerializableExtra("Username");
        addHostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Owner.this, AddHostelActivity.class);
                intent.putExtra("Username", (String) Username);
                startActivity(intent);
            }
        });
        SignOut = findViewById(R.id.Signout);
        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(Owner.this, SignInActivity.class);
                startActivity(I);
                finish();
            }
        });
        findViewById(R.id.AcademicCourses).setOnClickListener(view -> {
            startActivity(new Intent(Owner.this, AcademicCoursesActivity.class));
        });
        aadminstration.setOnClickListener(view -> {
            startActivity(new Intent(Owner.this, StudentActivity.class));
        });
        //loadDate();
        findViewById(R.id.recoverPassword).setOnClickListener(view -> {
            showRecoverPasswordDialog();
        });

    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailet = new EditText(this);

        // write the email using which you registered
        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emaill = emailet.getText().toString().trim();
                beginRecovery(emaill);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    ProgressDialog loadingBar;

    private void beginRecovery(String emaill) {
        loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(emaill).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if (task.isSuccessful()) {
                    // if isSuccessful then done messgae will be shown
                    // and you can change the password
                    Toast.makeText(Owner.this, "Recovery email sent", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Owner.this, SignInActivity.class));
                    finish();
                } else {
                    Toast.makeText(Owner.this, "Error Occured", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(Owner.this, "Error Failed", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Administrator: {
                startActivity(new Intent(Owner.this, StudentActivity.class));
            }
            case R.id.academy : {
                startActivity(new Intent(Owner.this, AcademicCoursesActivity.class));
            }
            case R.id.reset: {
                showRecoverPasswordDialog();
            }
            case R.id.hostel: {
                Intent intent = new Intent(Owner.this, AddHostelActivity.class);
                intent.putExtra("Username", (String) Username);
                startActivity(intent);
            }
            case R.id.contact_us:  {

            }
            case R.id.aboutUs:  {

            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
}

