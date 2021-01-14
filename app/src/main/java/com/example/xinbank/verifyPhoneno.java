package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinbank.Database.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class verifyPhoneno extends AppCompatActivity {

    private TextView pressmsg;
    private EditText code;
    private Button send;
    private String otp;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phoneno);

        pressmsg = findViewById(R.id.presstext1);
        code = findViewById(R.id.editcode);
        send = findViewById(R.id.phoneverify_btn);
        progressBar= findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        otp = getIntent().getStringExtra("auth");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String verificationcode = code.getText().toString();
                if (!verificationcode.isEmpty()){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp, verificationcode);
                    signIn(credential);
                }else{
                    Toast.makeText(verifyPhoneno.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void signIn(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(verifyPhoneno.this, "Success", Toast.LENGTH_SHORT).show();
                    sendtoSETpw();
                }else{
                    progressBar.setVisibility(View.GONE);
                    pressmsg.setText(task.getException().getMessage());
                    pressmsg.setTextColor(Color.RED);
                    pressmsg.setVisibility(View.VISIBLE);
                    Toast.makeText(verifyPhoneno.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendtoSETpw() {
        startActivity(new Intent(verifyPhoneno.this, ymain.class));
        finish();
    }
}