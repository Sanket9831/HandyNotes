package com.example.handynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

//import static android.view.View.GONE;
//import static android.view.View.VISIBLE;

public class Verify extends AppCompatActivity {

    EditText inputNumber1, inputNumber2, inputNumber3, inputNumber4, inputNumber5, inputNumber6;
    Button verifyOtpButton;
    TextView userMobileNumber, resendOtp;
    String getBackendOtp;
    ProgressBar verifyProgressBar;
    private  FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        inputNumber1 = findViewById(R.id.inputotp1);
        inputNumber2 = findViewById(R.id.inputotp2);
        inputNumber3 = findViewById(R.id.inputotp3);
        inputNumber4 = findViewById(R.id.inputotp4);
        inputNumber5 = findViewById(R.id.inputotp5);
        inputNumber6 = findViewById(R.id.inputotp6);

        resendOtp = findViewById(R.id.resendOtp);
        verifyOtpButton = findViewById(R.id.verifyOtp);

        verifyProgressBar = findViewById(R.id.progressbar_verify_otp);
        mAuth = FirebaseAuth.getInstance();

        userMobileNumber = findViewById(R.id.userMobileNumber);
        userMobileNumber.setText(String.format("+91-%s", getIntent().getStringExtra("mobile")
        ));

        getBackendOtp = getIntent().getStringExtra("backendOTP");

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputNumber1.getText().toString().isEmpty() && !inputNumber2.getText().toString().isEmpty() && !inputNumber3.getText().toString().isEmpty() && !inputNumber4.getText().toString().isEmpty() && !inputNumber5.getText().toString().isEmpty() && !inputNumber6.getText().toString().isEmpty()) {
                    String userOtp = inputNumber1.getText().toString() + inputNumber2.getText().toString() + inputNumber3.getText().toString() + inputNumber4.getText().toString() + inputNumber5.getText().toString() + inputNumber6.getText().toString();

                    if (userOtp != null) {
                        verifyProgressBar.setVisibility(View.VISIBLE);
                        verifyOtpButton.setVisibility(View.INVISIBLE);
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getBackendOtp, userOtp
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        verifyProgressBar.setVisibility(View.GONE);
                                        verifyOtpButton.setVisibility(View.VISIBLE);

                                        if (task.isSuccessful()) {
                                            Intent toDashBoard = new Intent(getApplicationContext(), Dashboard.class);
                                            toDashBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(toDashBoard);
                                        } else {
                                            Toast.makeText(Verify.this, "Enter correct OTP", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(Verify.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Verify.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        moveOtpCursor();
//        RESEND CODE
        findViewById(R.id.resendOtp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                        Toast.makeText(Verify.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull @NotNull String newBackendOtp, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        getBackendOtp = newBackendOtp;
                        Toast.makeText(Verify.this, "OTP Resent Successfully", Toast.LENGTH_SHORT).show();
                    }
                };
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(String.format("+91%s", getIntent().getStringExtra("mobile")))       // Phone number to verify
                                .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(Verify.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

    }

    private void moveOtpCursor() {

        inputNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputNumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputNumber3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputNumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputNumber4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputNumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputNumber5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputNumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputNumber6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}