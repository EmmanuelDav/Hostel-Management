package com.example.profile2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();
    private EditText emailEditText;
    private EditText passEditText;
    TextView newUserSignIn;
    Button signIn;
    FirebaseAuth mFirebaseAuth;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        progressDialog = getDialogProgressBar().create();
        progressDialog.setCanceledOnTouchOutside(false);
        newUserSignIn = (TextView) findViewById(R.id.register);
        signIn = findViewById(R.id.button);
        emailEditText = findViewById(R.id.mail);
        passEditText = findViewById(R.id.password);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                final String email, password;
                email = emailEditText.getText().toString().trim();
                password = passEditText.getText().toString().trim();
                final FirebaseFirestore sFirebaseFirestore = FirebaseFirestore.getInstance();
                if (email.length() == 0 || password.length() == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Login fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sFirebaseFirestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<DocumentSnapshot> li;
                                        li = queryDocumentSnapshots.getDocuments();
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            int x = 0;
                                            for (DocumentSnapshot i : li) {
                                                String AA = (String) i.get("AccountType");
                                                String UU = (String) i.get("Username");
                                                if (UU.equals(email)) {
                                                    if (AA.equals("Owner")) {
                                                        x = 1;
                                                    }
                                                    break;
                                                }
                                            }
                                            progressDialog.dismiss();
                                            if (x == 0) {
                                                Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                Intent i1 = new Intent(SignInActivity.this, MainActivity.class);
                                                startActivity(i1);
                                            } else {
                                                Toast.makeText(SignInActivity.this, "You are an Admin You Need to Create A User Account", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                });

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SignInActivity.this, "Failed Low OR No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception pE) {
                            progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Failed Low OR No Internet Connection", Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"Login Failed because "+pE.getMessage());
                        }
                    });
                }
            }
        });
        newUserSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, Register.class);
                startActivity(i);
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
