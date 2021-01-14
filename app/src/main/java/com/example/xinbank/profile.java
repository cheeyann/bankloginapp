package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinbank.Database.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class profile extends AppCompatActivity {
    private String accnumfromdb,acctypefromdb;
    private String idfromsession, accontrolfromsession, namefromdb;
    private int accbalancefromdb;
    private TextView paccnum, paccbalance, pname,pacctype;
    private Button logoutBTN, tranferbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        paccnum = findViewById(R.id.showotutext);
        paccbalance = findViewById(R.id.showaccbalance);
        pname = findViewById(R.id.showname);
        pacctype = findViewById(R.id.logoutsTIME);
        logoutBTN = findViewById(R.id.fingerverify_btn);
        tranferbtn = findViewById(R.id.buttontransfer);

        getidFromsession();
        getusernamefromdb();
        getuserbankaccfromdb();
        String ac = "false";

        if(accontrolfromsession.equals(ac)) {
            tranferbtn.setVisibility(View.GONE);
        }

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void getuserbankaccfromdb() {
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Customers");
        reference3.child(idfromsession).child("BankAccountCust").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    accnumfromdb = dataSnapshot.child("accountnum").getValue(String.class);
                    accbalancefromdb = dataSnapshot.child("account_balance").getValue(int.class);
                    acctypefromdb = dataSnapshot.child("account_type").getValue(String.class);
                    pacctype.append(" "+ acctypefromdb);
                    paccnum.setText(String.valueOf(accnumfromdb));
                    paccbalance.setText("RM "+ String.valueOf(accbalancefromdb));
                } else {
                    Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getusernamefromdb() {
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Customers");
        reference3.child(idfromsession).child("OnlineCust").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    namefromdb = dataSnapshot.child("username").getValue(String.class);
                    pname.setText(namefromdb);
                } else {
                    Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        tologout();
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
    private void tologout(){
        startActivity(new Intent(this,logoutSuccess.class));
        finish();
    }
    private void logout() {
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.logoutUserFromSession();
    }

    private void getidFromsession() {
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> usersDetails = sessionManager.getUserDeatilFromSession();

        idfromsession = usersDetails.get(SessionManager.KEY_ID);
        accontrolfromsession = usersDetails.get(SessionManager.KEY_ACCONTROL);
    }
}