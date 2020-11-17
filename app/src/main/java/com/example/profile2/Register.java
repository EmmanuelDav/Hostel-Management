package com.example.profile2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private static final String TAG = Register.class.getSimpleName();
    EditText name, mail, mPhoneNum, mPassword, reTPassword;
    Button signUp;
    Spinner mHostelType;
    FirebaseAuth mFirebaseAuth;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = getDialogProgressBar().create();
        progressDialog.setCanceledOnTouchOutside(false);
        mPhoneNum = findViewById(R.id.mobile);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        mPassword = findViewById(R.id.password);
        reTPassword = findViewById(R.id.rpassword);
        signUp = findViewById(R.id.signup);
        mHostelType = findViewById(R.id.Atype);
        mFirebaseAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String ePhoneNum = mPhoneNum.getText().toString().trim();
                String eMail = mail.getText().toString().trim();
                String ePassword = mPassword.getText().toString().trim();
                String eRetypePassword = reTPassword.getText().toString().trim();
                String eName = name.getText().toString();
                if (ePhoneNum.length() < 11) mPhoneNum.setError("Enter Valid Phone Number");
                progressDialog.dismiss();
                if (eMail.isEmpty()) mail.setError("Email can't be empty");
                progressDialog.dismiss();
                if (eName.isEmpty()) name.setError("Name can't be empty");
                progressDialog.dismiss();
                if (ePassword.isEmpty()) mPassword.setError("Password can't be empty");
                progressDialog.dismiss();
                if (eName.isEmpty()) name.setError("Name can't be empty");
                progressDialog.dismiss();
                if (!(ePassword.equals(eRetypePassword))) {
                    Toast.makeText(Register.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (ePassword.length() != 0 && eMail.length() != 0 && eName.length() != 0 && ePhoneNum.length() == 11) {
                    progressDialog.show();
                    mFirebaseAuth.createUserWithEmailAndPassword(eMail, ePassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String sUser = "User";
                                String ATYPE = mHostelType.getSelectedItem().toString();
                                if (ATYPE.equals("Hostel Owner")) {
                                    sUser = "Owner";
                                }
                                Map<String, String> user = new HashMap<>();
                                user.put("Name", eName);
                                user.put("Username", eMail);
                                user.put("Phone", ePhoneNum);
                                user.put("AccountType", sUser);
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Users").document(eMail).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register.this, "User SignUp Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Register.this, SignInActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register.this, "User SignUp Unsuccessful because + " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception pE) {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "User SignUp Unsuccessful because + " + pE.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public AlertDialog.Builder getDialogProgressBar() {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.dialog);
        }
        return builder;
    }
}
