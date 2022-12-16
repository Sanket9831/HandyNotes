package com.example.handynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static android.view.View.*;

public class LogIn extends AppCompatActivity {

    EditText enterNumber;
    Button getOtpButton;
    ProgressBar sendOtpProgress;
    String phoneNumber;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent i = new Intent(getApplicationContext(), Dashboard.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        enterNumber = findViewById(R.id.mobileNumber);
        getOtpButton = findViewById(R.id.getOtpButton);
        sendOtpProgress = findViewById(R.id.progressbar_sending_otp);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = enterNumber.getText().toString();
                if(!enterNumber.getText().toString().trim().isEmpty()){
                    if((enterNumber.getText().toString().trim()).length() == 10){

                        sendOtpProgress.setVisibility(VISIBLE);
                        getOtpButton.setVisibility(INVISIBLE);


                        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
                        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                sendOtpProgress.setVisibility(GONE);
                                getOtpButton.setVisibility(VISIBLE);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                sendOtpProgress.setVisibility(GONE);
                                getOtpButton.setVisibility(VISIBLE);
                                Toast.makeText(LogIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull @NotNull String backendOtp, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                Toast.makeText(LogIn.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                                sendOtpProgress.setVisibility(GONE);
                                getOtpButton.setVisibility(VISIBLE);
                                Intent intent =  new Intent(getApplicationContext(), Verify.class);
                                intent.putExtra("mobile", enterNumber.getText().toString());
                                intent.putExtra("backendOTP", backendOtp);
                                startActivity(intent);
                            }
                        };
                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(mAuth)
                                        .setPhoneNumber(String.format("+91%s", phoneNumber))       // Phone number to verify
                                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                                        .setActivity(LogIn.this)                 // Activity (for callback binding)
                                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);
                    }else{
                        Toast.makeText(LogIn.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LogIn.this, "Enter Mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}