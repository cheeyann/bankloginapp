package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinbank.Database.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class firstpage extends AppCompatActivity {
    private Button fsigninbtn,floginbtn;
    private TextView msgtxt;
    private EditText codeenter;
    String imei, device_id, phone = "+15555215554", codebysystem;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        msgtxt = findViewById(R.id.textView2);
        fsigninbtn = findViewById(R.id.firstregister_btn);
        floginbtn = findViewById(R.id.firstlogin_btn);
        codeenter = findViewById(R.id.editphonecode);
        /*

        //checkimeiexistfromdb();
       // getdeviceid();
        //checksession();
        msgtxt.append("\n"+ device_id);
        fsigninbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeenter.getText().toString();
                if(!code.isEmpty()){
                    verifyCode(code);
                }
                Toast.makeText(getApplicationContext(), "register", Toast.LENGTH_SHORT).show();
                //openFinger();
            }
        });
        floginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getimeifromDB();
                sendcodetoPhone(phone);
                Toast.makeText(getApplicationContext(), "login", Toast.LENGTH_SHORT).show();
                //openmain();
            }
        });
    }

    private void sendcodetoPhone(String _phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                _phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codebysystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if(code != null){
                        codeenter.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(firstpage.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codebysystem,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(firstpage.this, "Verification success",Toast.LENGTH_SHORT).show();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w( "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(firstpage.this, "Verification not complete",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void getdeviceid() {
        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void setimei(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("imeiExist");
        imeiexist setimei = new imeiexist("222222222222222","-MQX_9RIwLBSwCY0r4de","750825038888");
        myRef.child("750825038888").setValue(setimei,new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getApplicationContext(), "Data could not be saved"+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    //System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                }
            }
        } );
    }
    private void getimeifromDB() {
        final String[] idfromdb = {""};
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("imeiExist");
        Query checkuser = reference.orderByChild("ic").equalTo("750825038888");

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Customers");
        Query checkuser2 = reference2.orderByChild("ic").equalTo("750825038888");

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //String namefromdb = dataSnapshot.child("000000000000000").child("signinName").getValue(String.class);
                    idfromdb[0] = dataSnapshot.child("750825038888").child("_id").getValue(String.class);
                    //String profilename = namefromdb;
                    //msgtxt.setText("Device registered, user: " + dataSnapshot.getChildren());
                    //fsigninbtn.setVisibility(View.GONE);
//                    char profileicon = (char)profilename.toString().toUpperCase().charAt(0);
//                    msgname.setText(profileicon);
//                    msgprofile.setText(profileicon);
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                } else {
                    msgtxt.setText("haven't register");
                    //floginbtn.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        });
        checkuser2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String namefromdb = dataSnapshot.child(idfromdb[0]).child("fullname").getValue(String.class);
                    msgtxt.setText("name from db: "+ namefromdb);
                }else{
                    msgtxt.setText("haven't register here");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        });
    }
    private void checkimeiexistfromdb(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("imeiExist");
        Query checkuser = reference.orderByChild("ic").equalTo("750825038888");
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    msgtxt.setText("Registered, please click LOGIN button to proceed.");
                    //fsigninbtn.setVisibility(View.GONE);

                } else {
                    msgtxt.setText("Haven't register, please click REGISTER button to proceed.");
                    //floginbtn.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        });
    }
    private void openFinger(){
        Intent intent = new Intent(this,fingerPrintcheck.class);
        startActivity(intent);
        finish();
    }
    private void openmain(){
        Intent intentmain = new Intent(this,MainActivity.class);
        startActivity(intentmain);
        finish();
    }
    private void checksession(){
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> usersDetails = sessionManager.getUserDeatilFromSession();

        String id = usersDetails.get(SessionManager.KEY_ID);
        imei = usersDetails.get((SessionManager.KEY_IMEI));
        if (!(imei == null || imei.length() == 0)) {
            msgtxt.setText("Registered, please click LOGIN button to proceed.");
            //check database
            fsigninbtn.setVisibility(View.GONE);
        } else {
            msgtxt.setText("Haven't register, please click REGISTER button to proceed.");
            floginbtn.setVisibility(View.GONE);
        }
    }

         */
    }
}