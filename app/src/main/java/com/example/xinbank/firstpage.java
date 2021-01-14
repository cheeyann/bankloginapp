package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class firstpage extends AppCompatActivity {
    private Button fsigninbtn, floginbtn;
    private TextView msgtxt, msgRegister;
    private String device_id, userid;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        msgtxt = findViewById(R.id.logoutsTIME);
        msgRegister = findViewById(R.id.msgregister);
        fsigninbtn = findViewById(R.id.logouthomebtn);
        floginbtn = findViewById(R.id.firstlogin_btn);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        getdeviceid();
        checkDeviceexist();
        fsigninbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Register", Toast.LENGTH_SHORT).show();
                openFinger();
            }
        });
        floginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();
                openmain();
            }
        });
    }

    private void checkDeviceexist() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DeviceExists");
        Query checkuser = reference.orderByChild("deviceid").equalTo(device_id);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userid = dataSnapshot.child(device_id).child("id").getValue(String.class);
                    progressBar.setVisibility(View.GONE);
                    msgtxt.setText("Registered, please click LOGIN button to proceed.");
                    //check database
                    fsigninbtn.setVisibility(View.GONE);
                } else {
                    msgtxt.setText("Haven't register, please click REGISTER button to proceed.");
                    String msgg = "We will use this device to identify you, if your phone were lost or you want to " +
                            "change to new phone, please remove your online bank account from the old device via our website";
                    msgRegister.setText(msgg);
                    progressBar.setVisibility(View.GONE);
                    floginbtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Error :" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getdeviceid() {
        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void openFinger() {
        Intent intent = new Intent(this, fingerPrintcheck.class);
        startActivity(intent);
        finish();
    }

    private void openmain() {
        Intent intentmain = new Intent(this, MainActivity.class);
        intentmain.putExtra("id", userid);
        startActivity(intentmain);
        finish();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firstpage.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();

    }
}