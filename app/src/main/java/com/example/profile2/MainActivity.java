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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText emailEditText;
    private EditText passEditText;
    TextView newUserSignIn;
    Button signIn;
    FirebaseAuth fb;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        if (fb.getCurrentUser() != null) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fb = FirebaseAuth.getInstance();
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
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (email.length() == 0 || password.length() == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Login fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    fb.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                                                Intent i1 = new Intent(MainActivity.this, Mainpage.class);
                                                startActivity(i1);
                                            } else {

                                                Intent i = new Intent(MainActivity.this, Owner.class);
                                                i.putExtra("Username", email);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    }
                                });

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                              //  Log.d(TAG,"Login Failed because "+task.getResult().toString());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception pE) {
                            Log.d(TAG,"Login Failed because "+pE.getMessage());
                        }
                    });

                }
            }
        });
        newUserSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });
        emailEditText = (EditText) findViewById(R.id.mail);
        passEditText = (EditText) findViewById(R.id.password);
    }

    public AlertDialog.Builder getDialogProgressBar() {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.dialog);
        }
        return builder;
    }

}
