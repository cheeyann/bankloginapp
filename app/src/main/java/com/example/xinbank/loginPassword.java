package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class loginPassword extends AppCompatActivity {
    private String idfromshow, namefromdb, pwfromdb, pwfromuser, otufromshowotu;
    private TextView msg, setpw;
    private Button loginBTN;
    private String acontrolfromdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);
        msg = findViewById(R.id.textViewName);
        setpw = findViewById(R.id.Lsetpw);
        loginBTN = findViewById(R.id.Lnextbtn);

        getotufromshowotu();
        getAControlFromDb();
        getpwfromdb();

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwfromuser = setpw.getText().toString().trim();
                if (pwfromuser.equals(pwfromdb)) {
                    Toast.makeText(getApplicationContext(), "Password correct, Verified", Toast.LENGTH_SHORT).show();
                    getuserdata();
                } else {
                    Toast.makeText(getApplicationContext(), "Password wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getpwfromdb() {
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Customers");
        reference3.child(idfromshow).child("OnlineCust").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    pwfromdb = dataSnapshot.child("password").getValue(String.class);
                    namefromdb = dataSnapshot.child("username").getValue(String.class);
                    msg.append(pwfromdb);
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

    private void getuserdata() {
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Customers");
        reference3.child(idfromshow).child("BankAccountCust").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int accnumfromdb = dataSnapshot.child("accountnum").getValue(int.class);
                    int accbalancefromdb = dataSnapshot.child("account_balance").getValue(int.class);
                    String acctypefromdb = dataSnapshot.child("account_type").getValue(String.class);
                    /*
                    Intent intent = new Intent(getApplicationContext(), profile.class);
                    intent.putExtra("id", idfromshow);
                    intent.putExtra("name", namefromdb);
                    intent.putExtra("accNum", accnumfromdb);
                    intent.putExtra("accBalance", accbalancefromdb);
                    intent.putExtra("accType", acctypefromdb);
                    intent.putExtra("accessControl", acontrolfromdb);
                    startActivity(intent);
                    finish();
                    */
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

    private void getAControlFromDb() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("message");
        Query checkuserotu = reference.orderByChild("otu").equalTo(otufromshowotu);

        checkuserotu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    acontrolfromdb = dataSnapshot.child(otufromshowotu).child("accessControl").getValue(String.class);
                } else {
                    Toast.makeText(getApplicationContext(), "No data in access control", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Database error in access control", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getotufromshowotu() {
        Intent intent = getIntent();
        otufromshowotu = intent.getStringExtra("otu");
        idfromshow = intent.getStringExtra("id");
    }
}