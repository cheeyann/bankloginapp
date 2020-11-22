package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signIn extends AppCompatActivity {
    private TextView sname, simei, sphone, semail, scard;
    private Button signinBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sname = findViewById(R.id.signinName);
        simei = findViewById(R.id.signinImei);
        sphone = findViewById(R.id.signinPhone);
        semail = findViewById(R.id.signinEmail);
        scard = findViewById(R.id.signinCard);
        signinBTN = findViewById(R.id.signinbtn);

        signinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });
    }
    private void signin(){
        final String ssname, ssimei, ssphone, ssemail, sscard;

        ssname = sname.getText().toString().trim();
        ssimei = simei.getText().toString().trim();
        ssphone = sphone.getText().toString().trim();
        ssemail = semail.getText().toString().trim();
        sscard = scard.getText().toString().trim();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        userHelperClass usershelper = new userHelperClass(ssname, ssimei, ssphone,ssemail, sscard);
        myRef.child(ssimei).setValue(usershelper,new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getApplicationContext(), "Data could not be saved"+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    //System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    opensigninpassword();
                }
            }
        } );

    }
    private void opensigninpassword(){
        Intent intent = new Intent(this,signinPassword.class);
        startActivity(intent);
    }
}