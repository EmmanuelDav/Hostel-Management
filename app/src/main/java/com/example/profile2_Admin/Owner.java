package com.example.profile2_Admin;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
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
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile2_Admin.Adapter.RecyclerViewAdaptor;
import com.example.profile2_Admin.Adapter.StudentAdapter;
import com.example.profile2_Admin.model.HostelOccupant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    RecyclerViewAdaptor mRecyclerviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        addHostel = findViewById(R.id.AddHostel);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolBar);
        navigationMenu = findViewById(R.id.nav);
        drawerLayout = findViewById(R.id.drawerL);
        aadminstration = findViewById(R.id.adminstration);
        Intent i = getIntent();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openNav, R.string.closeNav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
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
        mRecyclerviewAdapter = new RecyclerViewAdaptor(this,null);
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
                openChrome("http://rnu.edu.ng/contact_us.php");
            }
            case R.id.aboutUs:  {
                startActivity(new Intent(this, AboutUs.class));
            }
            case R.id.pdf:  {
                generatePDF();
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
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

    public boolean onOptionsItemSelected(final MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void generatePDF() {
        if(RecyclerViewAdaptor.mPrintView.isEmpty()){
            Toast.makeText(this, "cant create PDF, hostels might be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("Creating Pdf....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        ArrayList<View> viewArrayList = mRecyclerviewAdapter.getPrintView(); // A function from Adapter class which returns ArrayList of VIEWS
        Document document = new Document(PageSize.A4);
        final File extStore = new File((Environment.getExternalStorageDirectory() + File.separator + "Hostel_Management"), "Hostels_Pdf");

        if (!extStore.exists()) {
            extStore.mkdirs();
        }
        String path = extStore.getAbsolutePath() + "/" + System.currentTimeMillis() + ".pdf";
        Log.d("TAG", "Save to: " + path);
        loadingBar.dismiss();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int im = 0; im < viewArrayList.size(); im++) {
            // Iterate till the last of the array list and add each view individually to the document.
            try {
                viewArrayList.get(im).buildDrawingCache();         //Adding the content to the document
                Bitmap bmp = viewArrayList.get(im).getDrawingCache();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.scalePercent(70);
                image.setAlignment(Image.MIDDLE);
                if (!document.isOpen()) {
                    document.open();
                }
                document.add(image);

            } catch (Exception ex) {
                Log.e("TAG-ORDER PRINT ERROR", ex.getMessage());
            }
        }

        if (document.isOpen()) {
            document.close();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Owner.this);
        builder.setTitle("Success")
                .setMessage("PDF File Generated Successfully. file name is Hostel_Management")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    Toast.makeText(this, "PdF printed successfully, you can find it in your external storage", Toast.LENGTH_SHORT).show();
                }).show();

    }
}

