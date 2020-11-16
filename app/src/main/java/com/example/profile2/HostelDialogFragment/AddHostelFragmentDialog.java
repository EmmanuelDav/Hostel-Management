package com.example.profile2.HostelDialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.example.profile2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static com.example.profile2.OwnerAdd.staticHostelName;

public class AddHostelFragmentDialog extends DialogFragment {
    String name, level, department, phoneNum;
    EditText mSerialN, mName, mLevel, mPhoneNum, mDepartment;
    CardView mSubmitButton;
    AlertDialog progressDialog;
    AlertDialog.Builder builder;
    public AddHostelFragmentDialog() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_hostel_occupant, null);
        mName = dialogView.findViewById(R.id.name);
        mLevel = dialogView.findViewById(R.id.level);
        mPhoneNum = dialogView.findViewById(R.id.phoneNum);
        mDepartment = dialogView.findViewById(R.id.department);
        mName.requestFocus();
        mSubmitButton = dialogView.findViewById(R.id.SUBMIT);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(dialogView);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
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
        });
        return alertDialogBuilder.create();
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
}
