package com.finalyear.microproject.fragment;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.finalyear.microproject.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginTabFragment extends Fragment {

    EditText mEmail, mPass;
    TextView forgetPassword;
    Button loginButton;
    FirebaseAuth fAuth;
    float v=0;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);

        fAuth = FirebaseAuth.getInstance();

        mEmail = view.findViewById(R.id.email);
        mPass = view.findViewById(R.id.pass);
        forgetPassword = view.findViewById(R.id.forpass);
        loginButton = view.findViewById(R.id.button);
        progressBar = view.findViewById(R.id.login_progress);

        mEmail.setTranslationX(800);
        mPass.setTranslationX(800);
        forgetPassword.setTranslationX(800);
        loginButton.setTranslationX(800);

        mEmail.setAlpha(v);
        mPass.setAlpha(v);
        forgetPassword.setAlpha(v);
        loginButton.setAlpha(v);

        mEmail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        mPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgetPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginButton.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPass.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                }
                if(TextUtils.isEmpty(password)){
                    mPass.setError("Password is Required.");
                }
                if(password.length() < 6){
                    mPass.setError("Password Must be >= 6 Characters");
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(email,password);
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle(R.string.loginActivity_1);
                passwordResetDialog.setMessage(R.string.loginActivity_2);
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton(R.string.loginActivity_5, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), R.string.loginActivity_3, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), getString(R.string.loginActivity_4) + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                passwordResetDialog.setNegativeButton(R.string.loginActivity_6, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });
                passwordResetDialog.create().show();
            }
        });

        return view;
    }

    private void loginUser(String email, String password) {

        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(requireActivity(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
