package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xinbank.Database.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class showPhoneno extends AppCompatActivity {

    private TextView pressmsg;
    private EditText mobilenum;
    private Button send;
    private ProgressBar progressBar;
    private String phone;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_phoneno);

        pressmsg = findViewById(R.id.presstext);
        mobilenum = findViewById(R.id.editphone);
        send = findViewById(R.id.phonesend_btn);
        progressBar= findViewById(R.id.progressBar);
        getphoneFromsession();
        mobilenum.setText(phone);

        auth = FirebaseAuth.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String phone = mobilenum.getText().toString();
                if (!phone.isEmpty()){
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phone)
                            .setTimeout(60L , TimeUnit.SECONDS)
                            .setActivity(showPhoneno.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                }else{
                    pressmsg.setText("Please enter phone number");
                    pressmsg.setTextColor(Color.RED);

                }
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                progressBar.setVisibility(View.GONE);
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                pressmsg.setText(e.getMessage());
                pressmsg.setTextColor(Color.RED);
                pressmsg.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCodeSent(@NonNull final String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.GONE);
                Intent otpintent = new Intent(showPhoneno.this, verifyPhoneno.class);
                otpintent.putExtra("auth", s);
                startActivity(otpintent);
                finish();
/*
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent otpintent = new Intent(showPhoneno.this, verifyPhoneno.class);
                        otpintent.putExtra("auth", s);
                        startActivity(otpintent);
                    }
                },10000);

 */
            }
        };


    }
    private void signIn(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendtoSETpw();
                }else{
                    pressmsg.setText(task.getException().getMessage());
                    pressmsg.setTextColor(Color.RED);
                    pressmsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void sendtoSETpw(){
        Intent intent = new Intent(this, signinPassword.class);
        startActivity(intent);
        finish();

    }
    private void getphoneFromsession() {
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> usersDetails = sessionManager.getUserDeatilFromSession();

        phone = usersDetails.get((SessionManager.KEY_PHONE));
    }

}