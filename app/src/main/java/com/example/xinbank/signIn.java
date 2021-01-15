package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinbank.Database.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class signIn extends AppCompatActivity {
    private TextView sname, sid, scard, scardvalid, scardcvv, scardpin;
    private Button signinBTN;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sname = findViewById(R.id.signinName);
        sid = findViewById(R.id.signinID);
        scard = findViewById(R.id.signinCardno);
        scardvalid = findViewById(R.id.signinCardValidDate);
        scardcvv = findViewById(R.id.signinCardCVV);
        scardpin = findViewById(R.id.editTextNumberPassword);
        signinBTN = findViewById(R.id.signinbtn);
        progressBar= findViewById(R.id.progressBar);

        signinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                signin();
            }
        });


            }

    private void signin(){
        final String ssname = sname.getText().toString().trim();
        final String ssid = sid.getText().toString().trim(); //IC
        final String sscard = scard.getText().toString().trim();
        final String sscardvalid = scardvalid.getText().toString().trim();
        final String sscardcvv = scardcvv.getText().toString().trim();
        final String sscardpin = scardpin.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ICExists");
        Query checkuser = reference.orderByChild("ic").equalTo(ssid);

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final String idfromdb= dataSnapshot.child(ssid).child("id").getValue(String.class);
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Customers");
                    //get cus data
                    Query checkuser2 = reference2.orderByChild("user_id").equalTo(idfromdb);
                    checkuser2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                final String icfromdb= dataSnapshot.child(idfromdb).child("ic").getValue(String.class);
                                final String namefromdb = dataSnapshot.child(idfromdb).child("fullname").getValue(String.class);
                                final String phonefromdb = dataSnapshot.child(idfromdb).child("phone").getValue(String.class);

                                //get bank acc data
                                DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Customers");
                                reference3.child(idfromdb).child("BankAccountCust").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            final String bankcardfromdb = dataSnapshot.child("account_card_num").getValue(String.class);
                                            final String bankcardvalidfromdb = dataSnapshot.child("account_card_validdate").getValue(String.class);
                                            final String bankcardcvvfromdb = dataSnapshot.child("account_card_cvv").getValue(String.class);
                                            //later change to pin
                                            final String bankcardpinfromdb = dataSnapshot.child("account_card_pin").getValue(String.class);
                                            // check user enter info same with db
                                            if(ssid.equals(icfromdb) && ssname.equals(namefromdb) && sscard.equals(bankcardfromdb) && sscardvalid.equals(bankcardvalidfromdb) && sscardcvv.equals(bankcardcvvfromdb) && sscardpin.equals(bankcardpinfromdb) ){
                                                //yes correct
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "All data correct, Verified", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(),ymain.class);
                                                SessionManager sessionManager = new SessionManager(signIn.this);
                                                sessionManager.createLoginSession(idfromdb, namefromdb, icfromdb, phonefromdb, phonefromdb,"null");
                                                /*
                                                intent.putExtra("name",namefromdb);
                                                intent.putExtra("id",idfromdb);
                                                intent.putExtra("cardnum",bankcardfromdb);
                                                intent.putExtra("ic",icfromdb);
                                                intent.putExtra("phone",phonefromdb);

                                                 */
                                                startActivity(intent);
                                                finish();

                                            }else{
                                                //wrong
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Data enter wrongly", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            //bank acc no user
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "No this user bank account", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.w("loadPost:onCancelled", databaseError.toException());
                                        // bank acc db error
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Database error in user bank account", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                //no user in cus
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "No this user account", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("loadPost:onCancelled", databaseError.toException());
                            //db error in cus
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Database error in user account", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //no user in db
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "No this user", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
                //db error
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}