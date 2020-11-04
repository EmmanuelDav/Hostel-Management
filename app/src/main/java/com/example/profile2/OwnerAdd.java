package com.example.profile2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

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

public class OwnerAdd extends AppCompatActivity {
    ImageView imageH;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationListener locationListener;
    String Username;
    private StorageReference storageReference;
    EditText hostelName, address, distance, rentPerPerson, rent;
    EditText phone;
    TextView SelectedImage;
    Spinner hostelFor, numOfBathrooms, numOfBedrooms, typeOfRoom, numOfPerson;
    Button submitButton, selectImage, LOC, RecordLocation;
    String URLIMAGE;
    int rrq = 123;
    static int COUNTER = 0;
    Task<Uri> urlTask;
    private Uri filepath;
    double lat, lon;
    Map<String, String> map;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == rrq && data.getData() != null) {
            filepath = data.getData();
            SelectedImage.setText(data.getDataString());
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
                    // Continue with the task to get the download URL
                    return imgpoint.getDownloadUrl();
                }
            });
        } else {
            Toast.makeText(this, "Error No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_add);
        Intent it = getIntent();
        lat = 0.0;
        lon = 0.0;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        RecordLocation = findViewById(R.id.LOcation);
        RecordLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchLastLocation();
                Toast.makeText(OwnerAdd.this, "Current GPS Location recorded as Hostel Location", Toast.LENGTH_SHORT).show();
            }
        });
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
                String mHostelGender = hostelFor.getSelectedItem().toString();
                String mNumOfBathroom = numOfBathrooms.getSelectedItem().toString();
                String numfBeds = numOfBedrooms.getSelectedItem().toString();
                String mNumOfPerson = numOfPerson.getSelectedItem().toString();
                String mTypeOfRoom = typeOfRoom.getSelectedItem().toString();
                String PHone = phone.getText().toString();
                String mSelectedImage = SelectedImage.getText().toString();
                boolean Px = true;
                if (PHone.length() != 10) {
                    Toast.makeText(OwnerAdd.this, "Phone number should be 10 digits", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty() || mAddress.isEmpty() || mDistance.isEmpty() || mRentPerPerson.isEmpty() || mRent.isEmpty() || mSelectedImage.equals("no image selected")) {
                    Toast.makeText(OwnerAdd.this, "All fields Must be filled", Toast.LENGTH_LONG).show();
                } else {
                    if (!internetIsConnected()) {
                        Px = false;
                        Toast.makeText(OwnerAdd.this, "Internet Not available", Toast.LENGTH_SHORT).show();
                    } else {
                        upload();
                    }
                    if (Px) {
                        if (urlTask != null){
                            urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        URLIMAGE = downloadUri.toString();
                                        Toast.makeText(OwnerAdd.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        submitData();
                                    } else {
                                        Toast.makeText(OwnerAdd.this, "Image Uploaded Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(OwnerAdd.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(OwnerAdd.this, currentLocation.getLatitude() + " " +
                            currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    lon = currentLocation.getLongitude();
                    lat = currentLocation.getLatitude();
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
        if (!Double.toString(lat).equals(String.valueOf(0.0))){
            db.collection("hostellist").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(OwnerAdd.this, "Hostel Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(OwnerAdd.this, Owner.class);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(OwnerAdd.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                    map.clear();
                }
            });
        }else {
            mProgressDialog.dismiss();
            Toast.makeText(OwnerAdd.this, "We Need to Get your Location Click Record Hostel Location", Toast.LENGTH_LONG).show();
        }
    }

    public void openImage() {
        Intent fileImage = new Intent();
        fileImage.setType("image/*");
        fileImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(fileImage, "Select an image"), rrq);
    }
}
