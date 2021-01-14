package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinbank.Database.SessionManager;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

public class afterSetPassword extends AppCompatActivity {
    private String timestamp;
    private Button login, exit;
    private TextView showtime;
    private ProgressBar progressBar;
    private String idFromsession, deviceid, devicename, nameFromsession, icFromsession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_set_password);

        showtime = findViewById(R.id.logoutsTIME);
        login = findViewById(R.id.asetpwlogin);
        exit = findViewById(R.id.asetpwexit);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        getdeviceid();
        devicename = getDevicename();
        getdataFromsession();
        getTime();
        showtime.append(timestamp);
        setnamenpwtoDB();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tofirst();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    private void getdataFromsession() {
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> usersDetails = sessionManager.getUserDeatilFromSession();

        idFromsession = usersDetails.get((SessionManager.KEY_ID));
        nameFromsession = usersDetails.get((SessionManager.KEY_USERNAME));
        icFromsession = usersDetails.get((SessionManager.KEY_IC));
    }
    private void setnamenpwtoDB() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference("Customers");

    final userHelperClass helperClass = new userHelperClass();
                helperClass.setIMEI(deviceid);
                helperClass.setDeviceName(devicename);
                helperClass.setOnlinecustomer_id(idFromsession);
                helperClass.setPassword("done");
                helperClass.setTimestamp();
                helperClass.setUsername(nameFromsession);

    //check ic is exist in imei exist and set imei to imeiexist
        myRef.child(idFromsession).child("OnlineCust").setValue(helperClass, new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
            if (databaseError != null) {
                Toast.makeText(getApplicationContext(), "Data could not be saved" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                //online cust saved
                //create device exist
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference Ref = database.getReference("DeviceExists");
                DeviceExist deviceExist = new DeviceExist(deviceid,idFromsession);
                Ref.child(deviceid).setValue(deviceExist, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Data could not be saved in deviceExist" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_SHORT).show();
                            tofirst();
                        }}
                });
                //saved user data to session and to fisrtpage to login
                progressBar.setVisibility(View.GONE);
            }
            progressBar.setVisibility(View.GONE);
        }
    });
}
    private static String getDevicename() {
        String infomation = Build.MANUFACTURER + " " + Build.MODEL ;
        return infomation;
    }
    private void getdeviceid() {
        deviceid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    private void getTime() {
        timestamp = java.text.DateFormat.getDateTimeInstance().format(new Date());
    }
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        afterSetPassword.super.onBackPressed();
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
    private void tofirst(){
        new AlertDialog.Builder(this)
                .setMessage("Do you want to login now?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        firstpage();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        dialog.cancel();
                    }
                })
                .show();

    }
    private void firstpage(){
        startActivity(new Intent(this,firstpage.class));
        finish();
    }
    private void logout() {
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.logoutUserFromSession();
    }


}