package com.finalyear.microproject.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.finalyear.microproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class PhoneLoginActivity extends AppCompatActivity {
    String phoneNumber;

    EditText mPhoneNumber;
    Button mGenerateBtn;
    CountryCodePicker countryCodePicker;
    ProgressBar mProgress, mProgress1;
    Boolean verificationOnProgress = false;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone_login);
        fAuth = FirebaseAuth.getInstance();

        countryCodePicker = findViewById(R.id.ccp);
        mPhoneNumber = findViewById(R.id.edittext_mobile_number);

        mGenerateBtn = findViewById(R.id.button_verify_number);
        mProgress = findViewById(R.id.progressBar);

        mGenerateBtn.setOnClickListener(v -> {
            phoneNumber = mPhoneNumber.getText().toString().trim();
            if(!phoneNumber.isEmpty() && phoneNumber.length() == 10) {
                    if(!verificationOnProgress){
                        mGenerateBtn.setEnabled(false);
                        mProgress.setVisibility(View.VISIBLE);
                        String phoneNum = "+" + countryCodePicker.getSelectedCountryCode() + " " + phoneNumber;
                        phoneNumber = phoneNum;
                        Log.d("phone", "Phone No.: " + phoneNum);
                        startPhoneNumberVerification(phoneNum);
                    }else {
                        mGenerateBtn.setEnabled(false);
                        mProgress.setVisibility(View.INVISIBLE);
                    }
                }
                else {
                    mPhoneNumber.setError("Valid Phone Required");
                }
            });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                mProgress.setVisibility(View.INVISIBLE);
                startOtpDialog(phoneNumber);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


            }
        };
    }


    private void startPhoneNumberVerification(String phoneNum) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(fAuth)
                        .setPhoneNumber(phoneNum)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startOtpDialog(String phoneNumber) {
        LayoutInflater inflater = LayoutInflater.from(PhoneLoginActivity.this);
        View subView = inflater.inflate(R.layout.dialog_verify_otp, null);

        Dialog dialog = new Dialog(PhoneLoginActivity.this);
        dialog.setContentView(subView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;


        PinView otp = subView.findViewById(R.id.text_enter_otp);
        TextView resendOtp = subView.findViewById(R.id.text_resend_otp);
        Button verifyOtp = subView.findViewById(R.id.button_verify);
        mProgress1 = subView.findViewById(R.id.progressBar1);

        resendOtp.setOnClickListener(v -> {
                resendVerificationCode(phoneNumber, mResendToken);
        });

        verifyOtp.setOnClickListener(v -> {
            String otpCode = Objects.requireNonNull(otp.getText()).toString().trim();
            if(TextUtils.isEmpty(otpCode)){
                Toast.makeText(PhoneLoginActivity.this,"Please enter OTP...",Toast.LENGTH_LONG).show();
            }
            else{
                mProgress1.setVisibility(View.VISIBLE);
                verifyPhoneNumberWithCode(mVerificationId, otpCode);
            }
        });
        dialog.create();
        dialog.show();
    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String otpCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken mResendToken) {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(fAuth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(this)
                            .setCallbacks(mCallBacks)
                            .setForceResendingToken(mResendToken)
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    String phone = Objects.requireNonNull(fAuth.getCurrentUser()).getPhoneNumber();
                    Toast.makeText(PhoneLoginActivity.this,"Logged in "+phone, Toast.LENGTH_SHORT).show();
                    mProgress1.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(PhoneLoginActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PhoneLoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgress1.setVisibility(View.GONE);
                });
    }
}

