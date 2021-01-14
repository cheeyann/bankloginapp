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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinbank.Database.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class signinPassword extends AppCompatActivity {
    private TextView msg, setpw, setpw2;
    private Button nextBTN;
    private String idFromsession, deviceid, nameFromsession, icFromsession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_password);
        msg = findViewById(R.id.textViewName);
        setpw = findViewById(R.id.Ssetpw);
        setpw2 = findViewById(R.id.Ssetpw2);
        nextBTN = findViewById(R.id.Snextbtn);
        getdataFromsession();
        msg.setText(nameFromsession);
        getdeviceid();
        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setnamenpwtoDB();
            }
        });
    }

    private void setnamenpwtoDB() {
        String pw1 = setpw.getText().toString().trim();
        String pw2 = setpw2.getText().toString().trim();
        if ((!pw1.isEmpty()) && (!pw2.isEmpty()) ) {
            if (isEqual(pw1, pw2)) {
                //equal
                //save to db
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("Customers");

                final userHelperClass helperClass = new userHelperClass();
                helperClass.setIMEI(deviceid);
                helperClass.setDeviceName(getDevice());
                helperClass.setOnlinecustomer_id(idFromsession);
                helperClass.setPassword(pw2);
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
                                        Toast.makeText(getApplicationContext(), "Data could not be saved in deviceExist" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_SHORT).show();
                                        tofirst();
                                    }}
                            });
                            //saved user data to session and to fisrtpage to login
                        }
                    }
                });
            } else {
                //not equal
                Toast.makeText(getApplicationContext(), "Password not equal", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
        }
    }

    private static Boolean isEqual(String a, String b) {
        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }
    }
    private void getdataFromsession() {
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> usersDetails = sessionManager.getUserDeatilFromSession();

        idFromsession = usersDetails.get((SessionManager.KEY_ID));
        nameFromsession = usersDetails.get((SessionManager.KEY_USERNAME));
        icFromsession = usersDetails.get((SessionManager.KEY_IC));
    }
    private void logout() {
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.logoutUserFromSession();
    }
    private void getdeviceid() {
        deviceid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signinPassword.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        tofirst();
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
                        onBackPressed();
                    }
                })
                .show();

    }
    private void firstpage(){
        startActivity(new Intent(this,firstpage.class));
        finish();
    }

    private static String getDevice() {
        String infomation = Build.MANUFACTURER + " " + Build.MODEL ;
        return infomation;
    }
}