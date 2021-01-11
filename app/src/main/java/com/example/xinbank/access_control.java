package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinbank.Database.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

public class access_control extends AppCompatActivity {

    TextView actextt;
    RadioButton acActivebtnn, acPassivebtnn;
    Button acRequestbtnn;
    String active;
    String otname="" , result="", idfromsession,imeifromsession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_control);
        actextt = findViewById(R.id.actext);
        acActivebtnn = findViewById(R.id.acActivebtn);
        acPassivebtnn = findViewById(R.id.acPassivebtn);
        acRequestbtnn = findViewById(R.id.acRequestbtn);
        getdatafromsession();
        actextt.append(idfromsession);
        acRequestbtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = "Final selected mode : ";
                result += (acActivebtnn.isChecked())?"Active":
                        (acPassivebtnn.isChecked())?"Passive":"";
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                requestsend();
            }
        });


    }
    public  void getdatafromsession(){
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> usersDetails = sessionManager.getUserDeatilFromSession();
        idfromsession = usersDetails.get(SessionManager.KEY_ID);
        imeifromsession = usersDetails.get(SessionManager.KEY_IMEI);
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();
        String str="";
        switch (view.getId()){
            case R.id.acActivebtn:
                if (checked){
                    str="Active mode selected";
                    active = "true";
                    break;
                }
            case R.id.acPassivebtn:
                if (checked){
                    str="Passive mode selected";
                    active = "false";
                    break;
                }
        }
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }
    private void requestsend(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        final messageHelperClass helperClass = new messageHelperClass();
        OTUgenerator otu = new OTUgenerator();
        otname=otu.getOTU();
        String otnametodata =sha.otuhashing(otname);
        helperClass.setOtu(otnametodata);
        helperClass.setAccessControl(active);
        helperClass.setImei(imeifromsession);
        helperClass.setTimestamp();
        helperClass.setId(idfromsession);

        myRef.child(otnametodata).setValue(helperClass, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getApplicationContext(), "Request could not be saved"+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), result +"Request success", Toast.LENGTH_SHORT).show();
                    openshowotu();
                }
            }
        });
    }
    private void openshowotu(){
        Intent intent = new Intent(getApplicationContext(),show_otu.class);
        intent.putExtra("otu", otname);
        startActivity(intent);
        finish();
    }

}
