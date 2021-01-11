package com.example.profile2.HostelDialogFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.example.profile2.AddHostelActivity;
import com.example.profile2.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Random;

import static com.example.profile2.AddHostelActivity.staticHostelName;

public class AddHostelFragmentDialog extends DialogFragment {
    private static final String TAG = AddHostelFragmentDialog.class.getSimpleName();
    private  String URLIMAGE;
    String name, level, department, phoneNum;
    EditText mSerialN, mName, mLevel, mPhoneNum, mDepartment;
    CardView mSubmitButton;
    AlertDialog progressDialog;
    ImageView mCircleImageView;
    private ProgressDialog mProgressDialog;
    AlertDialog.Builder builder;
    Task<Uri> urlTask;
    private static final int rrq = 111;
    public AddHostelFragmentDialog(){}
    int mInt = 0;
    Uri filepath;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_hostel_occupant, null);
        mName = dialogView.findViewById(R.id.name);
        mLevel = dialogView.findViewById(R.id.level);
        mPhoneNum = dialogView.findViewById(R.id.phoneNum);
        mCircleImageView = dialogView.findViewById(R.id.profilePic);
        mDepartment = dialogView.findViewById(R.id.department);
        mName.requestFocus();
        mSubmitButton = dialogView.findViewById(R.id.SUBMIT);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(dialogView);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                upload();
                if (urlTask != null) {
                    urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                URLIMAGE = downloadUri.toString();
                                SaveData();
                            } else {
                                Toast.makeText(getContext(), "Image Uploaded Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                openImage();
            }
        });
        return alertDialogBuilder.create();
    }

    private void SaveData() {
        progressDialog.show();
        name = mName.getText().toString();
        level = mLevel.getText().toString();
        phoneNum = mPhoneNum.getText().toString();
        department = mDepartment.getText().toString();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (name.isEmpty() || level.isEmpty() || phoneNum.isEmpty() || department.isEmpty()|| staticHostelName.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Empty blank found Add Hostel Name", Toast.LENGTH_LONG).show();
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("level", level);
            map.put("department", department);
            map.put("phoneNum", phoneNum);
            map.put("Url",URLIMAGE);
            map.put("hostel name",staticHostelName);
            db.collection("HostelOccupant").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> pTask) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
            });

        }
    }

    public void openImage() {
        Intent fileImage = new Intent();
        fileImage.setType("image/*");
        fileImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(fileImage, "Select an image"), rrq);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = getDialogProgressBar().create();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public AlertDialog.Builder getDialogProgressBar() {
        if (builder == null) {
            builder = new AlertDialog.Builder(getContext());
            builder.setView(R.layout.dialog);
        }
        return builder;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap sBitmap = null;
        try {
            if (requestCode == rrq && data.getData() != null) {
                filepath = data.getData();
                sBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filepath);
                mCircleImageView.setImageBitmap(sBitmap);
            }
        } catch (Exception pE) {
            Log.d(TAG, "onActivityResult: " + pE.getMessage());

        }
    }

    public void upload() {
        if (filepath != null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setTitle("Uploading Image...");
            mProgressDialog.show();
            StorageReference sr = FirebaseStorage.getInstance().getReference();
            Random rand = new Random();
            mInt = rand.nextInt(1000000);
            String imagePath = "images/" + staticHostelName + String.valueOf(mInt) + ".jpg";
            mInt++;
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
            Toast.makeText(getContext(), "Error Please Provide the Persons Picture", Toast.LENGTH_LONG).show();
        }
    }

}
