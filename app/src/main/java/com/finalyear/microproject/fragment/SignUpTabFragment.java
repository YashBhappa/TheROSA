package com.finalyear.microproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalyear.microproject.R;
import com.finalyear.microproject.activities.LoginActivity;
import com.finalyear.microproject.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class SignUpTabFragment extends Fragment {

    EditText mName, mUserName, mEmail, mPhoneNumber, mPassword;
    Button mSignUpButton;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseFirestore fStore;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);

        mName = root.findViewById(R.id.name);
        mUserName = root.findViewById(R.id.username);
        mEmail = root.findViewById(R.id.reg_email);
        mPhoneNumber = root.findViewById(R.id.mobile);
        mPassword = root.findViewById(R.id.conpass);

        mSignUpButton = root.findViewById(R.id.sign_up_btn);
        progressBar = root.findViewById(R.id.sign_up_progress);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserInfo");

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = mName.getText().toString().trim();
                final String registerUserName = mUserName.getText().toString().trim();
                final String email = mEmail.getText().toString().trim();
                final String phoneNo = mPhoneNumber.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(fullName)){
                    Toast.makeText(getActivity(),"Please enter your name...",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(registerUserName)){
                    Toast.makeText(getActivity(),"Please enter username...",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(),"Please enter your email...",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(phoneNo)){
                    Toast.makeText(getActivity(),"Please enter phone number...",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(getActivity(),"Please enter password...",Toast.LENGTH_SHORT).show();
                }
                else{
                    registerUser(email, password);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

        });
        return root;
    }

    private void registerUser(String email, String password) {
        String fullName = mName.getText().toString();
        String phone = "+91" + mPhoneNumber.getText().toString();
        String registerUserName = mUserName.getText().toString().trim();

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // send verification link
                    FirebaseUser fUser = fAuth.getCurrentUser();
                    assert fUser != null;
                    fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                        }
                    });

                    user = fAuth.getCurrentUser();
                    assert user != null;
                    String uid = user.getUid();
                    HashMap<String, Object> user = new HashMap<>();
                    user.put("name", fullName);
                    user.put("email", email);
                    user.put("phoneNo", phone);
                    user.put("username", registerUserName);
                    user.put("password", password);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference("UserInfo");
                    reference.child(registerUserName).setValue(user);
                    Toast.makeText(getActivity(), "User Registered.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Registration failed.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
