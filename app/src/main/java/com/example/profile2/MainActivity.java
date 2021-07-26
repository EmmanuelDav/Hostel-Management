package com.example.profile2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile2.Adapter.RecyclerViewAdaptor;
import com.example.profile2.model.Entry;
import com.example.profile2.model.HostelOccupant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.profile2.HostelDetails.sHostelOccupants;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdaptor mRecyclerViewAdaptor;
    FirebaseFirestore mFirebaseFirestore;
    List<Entry> mEntryList;
    ArrayList<String> ListOFHostels, flist;
    FirebaseAuth mAuth;
    Button generatePDFbtn;
    int pageHeight = 1120;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;
    final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        ListOFHostels = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEntryList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewAdaptor = new RecyclerViewAdaptor(MainActivity.this, mEntryList);
        mFirebaseFirestore.collection("hostellist")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
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
                });
        findViewById(R.id.Signout).setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent I = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(I);
            finish();
        });
        findViewById(R.id.AcademicCourses).setOnClickListener(vew -> {
            startActivity(new Intent(this, AcademicCourseActivity.class));
        });
        findViewById(R.id.ViewHostels).setOnClickListener(view -> {
            startActivity(new Intent(this, HostelActivity.class));
        });
        findViewById(R.id.adminstration).setOnClickListener(view -> {
            startActivity(new Intent(this, StudentActivity.class));
        });
        findViewById(R.id.changePassword).setOnClickListener(view -> {
            showRecoverPasswordDialog();
        });
        findViewById(R.id.contactUs).findViewById(R.id.contactUs).setOnClickListener(view ->{
            openChrome("http://rnu.edu.ng/contact_us.php");
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
            case R.id.Signout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent i = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.SRA:
                 loadPDF();
                break;

            default:

                break;
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
        mFirebaseFirestore.collection("hostellist").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
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
        builder.setPositiveButton("Recover", (dialog, which) -> {
            String emaill = emailet.getText().toString().trim();
            beginRecovery(emaill);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
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
        mAuth.sendPasswordResetEmail(emaill).addOnCompleteListener((OnCompleteListener<Void>) task -> {
            loadingBar.dismiss();
            if (task.isSuccessful()) {
                // if isSuccessful then done messgae will be shown
                // and you can change the password
                Toast.makeText(MainActivity.this, "Recovery email sent", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Error Occured", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(MainActivity.this, "Error Failed", Toast.LENGTH_LONG).show();
            }
        });
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

    void loadPDF(){
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo11);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            generatePDF();
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(scaledbmp, 56, 40, paint);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, R.color.dark_blue));
        canvas.drawText("Micheal ", 209, 100, title);
        canvas.drawText("Renaissance University", 209, 80, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.dark_blue));
        title.setTextSize(15);
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Summary of Available Hostels and students.", 396, 560, title);

        int x = 20;
        if (sHostelOccupants != null){
            for (HostelOccupant a : sHostelOccupants)
            {
                canvas.drawText(a.getDepartment() + "\t" + a.getName() + "\t" + a.getLevel(), 10, x, title);
                x+=20;
            }
        }
        pdfDocument.finishPage(myPage);
        File file = new File(Environment.getExternalStorageDirectory(), "Project_Report.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(MainActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }
}
