package com.example.profile2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.profile2.HostelDialogFragment.AddHostelFragmentDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddHostelActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    Spinner hostelFor, numOfBathrooms, numOfBedrooms, typeOfRoom, numOfPerson;
    EditText hostelName, address, distance, rentPerPerson, rent;
    Button submitButton, selectImage, RecordLocation;
    String Username,URLIMAGE;
    EditText phone;
    TextView SelectedImage;
    int rrq = 123;
    Location currentLocation;
    static int COUNTER = 0;
    LocationManager locationManager;
    Task<Uri> urlTask;
    private Uri filepath;
    double lat, lon;
    Map<String, String> map;
    private ProgressDialog mProgressDialog;
    private static final int LocationPermission = 124;
    private static final String TAG = AddHostelActivity.class.getSimpleName();
    public static String staticHostelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_add);
        checkLocationPermission();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Intent it = getIntent();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        lat = 0.0;
        lon = 0.0;
        RecordLocation = findViewById(R.id.LOcation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Username = (String) it.getSerializableExtra("Username");
        hostelName = findViewById(R.id.NameH);
        address = findViewById(R.id.add);
        distance = findViewById(R.id.dist);
        rentPerPerson = findViewById(R.id.rp);
        rent = findViewById(R.id.rt);
        hostelFor = findViewById(R.id.hf);
        SelectedImage = findViewById(R.id.SelectedImage);
        numOfBathrooms = findViewById(R.id.nb);
        numOfBedrooms = findViewById(R.id.bed);
        numOfPerson = findViewById(R.id.np);
        typeOfRoom = findViewById(R.id.type);
        phone = findViewById(R.id.phone1);
        submitButton = findViewById(R.id.SUBMIT);
        selectImage = findViewById(R.id.choose);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = hostelName.getText().toString();
                String mAddress = address.getText().toString();
                String mDistance = distance.getText().toString();
                String mRentPerPerson = rentPerPerson.getText().toString();
                String mRent = rent.getText().toString();
                String PHone = phone.getText().toString();
                String mSelectedImage = SelectedImage.getText().toString();
                boolean Px = true;
                if (PHone.length() != 11) {
                    Toast.makeText(AddHostelActivity.this, "Phone number should be 10 digits", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty() || mAddress.isEmpty() || mDistance.isEmpty() || mRentPerPerson.isEmpty() || mRent.isEmpty() || mSelectedImage.equals("no image selected")) {
                    Toast.makeText(AddHostelActivity.this, "All fields Must be filled especially The record Location ", Toast.LENGTH_LONG).show();
                } else {
                    if (!internetIsConnected()) {
                        Px = false;
                        Toast.makeText(AddHostelActivity.this, "Internet Not available", Toast.LENGTH_SHORT).show();
                    } else {
                        upload();
                    }
                    if (Px) {
                        if (urlTask != null) {
                            urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        URLIMAGE = downloadUri.toString();
                                        submitData();
                                    } else {
                                        Toast.makeText(AddHostelActivity.this, "Image Uploaded Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AddHostelActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        findViewById(R.id.addHostelOccupant).setOnClickListener(v -> {
            staticHostelName = hostelName.getText().toString();
            AddHostelFragmentDialog sAddHostelFragmentDialog = new AddHostelFragmentDialog();
            sAddHostelFragmentDialog.show(getSupportFragmentManager(), "Fragment");
        });
        RecordLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    OnGPS();
                } else {
                    fetchLastLocation();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == rrq && data.getData() != null) {
                filepath = data.getData();
                SelectedImage.setText(data.getDataString());
            }
        } catch (Exception pE) {
            Log.d(TAG, "onActivityResult: Exception" + pE.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LocationPermission: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

    }

    public void upload() {
        if (filepath != null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Uploading Image..");
            mProgressDialog.show();
            StorageReference sr = FirebaseStorage.getInstance().getReference();
            Random rand = new Random();
            COUNTER = rand.nextInt(1000000);
            String imagePath = "images/" + Username + String.valueOf(COUNTER) + ".jpg";
            COUNTER++;
            final StorageReference imgpoint = sr.child(imagePath);
            UploadTask uploadTask = imgpoint.putFile(filepath);
            urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    } else {
                        mProgressDialog.dismiss();
                    }
                    return imgpoint.getDownloadUrl();
                }
            });
        } else {
            Toast.makeText(this, "Error No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchLastLocation() {
        checkLocationPermission();
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(AddHostelActivity.this, currentLocation.getLatitude() + " " +
                            currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    lon = currentLocation.getLongitude();
                    lat = currentLocation.getLatitude();
                    Toast.makeText(AddHostelActivity.this, "location Successful.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public void submitData() {
        String Name = hostelName.getText().toString();
        String ADDRESS = address.getText().toString();
        String DISTNACE = distance.getText().toString();
        String RENTP = rentPerPerson.getText().toString();
        String RENTT = rent.getText().toString();
        String HF = hostelFor.getSelectedItem().toString();
        String NB = numOfBathrooms.getSelectedItem().toString();
        String BED = numOfBedrooms.getSelectedItem().toString();
        String NP = numOfPerson.getSelectedItem().toString();
        String TY = typeOfRoom.getSelectedItem().toString();
        String PHone = phone.getText().toString();
        String SELECTEDIMAGE = SelectedImage.getText().toString();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        map = new HashMap<>();
        map.put("Name", Name);
        map.put("Address", ADDRESS);
        map.put("Distance", DISTNACE);
        map.put("Rentp", RENTP);
        map.put("Rentt", RENTT);
        map.put("HostelFor", HF);
        map.put("NumberB", NB);
        map.put("NoBedrooms", BED);
        map.put("NumberP", NP);
        map.put("Type", TY);
        map.put("Phone", PHone);
        map.put("Url", URLIMAGE);
        map.put("Latitude", Double.toString(lat));
        map.put("Longitude", Double.toString(lon));
            db.collection("hostellist").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AddHostelActivity.this, "Hostel Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AddHostelActivity.this, Owner.class);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddHostelActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                    map.clear();
                }
            });

    }

    public void openImage() {
        Intent fileImage = new Intent();
        fileImage.setType("image/*");
        fileImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(fileImage, "Select an image"), rrq);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LocationPermission);
            return false;
        } else {
            return true;
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
