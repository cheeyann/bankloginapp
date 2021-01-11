package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class signinPassword extends AppCompatActivity {
    private TextView msg, setpw, setpw2;
    private Button nextBTN;
    String namefromsigninset, idfromsigninset, icardnumromsigninset, usernamefromset, imeifromset, icfromsigninset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_password);
        msg = findViewById(R.id.textViewName);
        setpw = findViewById(R.id.Ssetpw);
        setpw2 = findViewById(R.id.Ssetpw2);
        nextBTN = findViewById(R.id.Snextbtn);
        showfullname();
        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setnamenpwtoDB();
            }
        });
    }

    private void openfirst() {
        Intent intent = new Intent(this, firstpage.class);
        startActivity(intent);
        finish();
    }

    private void showfullname() {
        Intent intent = getIntent();
        namefromsigninset = intent.getStringExtra("name");
        idfromsigninset = intent.getStringExtra("id");
        icardnumromsigninset = intent.getStringExtra("cardnum");
        usernamefromset = intent.getStringExtra("username");
        imeifromset = intent.getStringExtra("imei");
        icfromsigninset = intent.getStringExtra("ic");
        msg.setText(namefromsigninset);
    }

    private void setnamenpwtoDB() {
        String pw1 = setpw.getText().toString().trim();
        String pw2 = setpw2.getText().toString().trim();
        if (isEqual(pw1, pw2)) {
            //equal
            //save to db
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("Customers");

            final userHelperClass helperClass = new userHelperClass();
            helperClass.setIMEI(imeifromset);
            helperClass.setCard_num(icardnumromsigninset);
            helperClass.setIs_login(false);
            helperClass.setOnlinecustomer_id(idfromsigninset);
            helperClass.setPassword(pw2);
            helperClass.setTimestamp();
            helperClass.setUsername(usernamefromset);

            //check ic is exist in imei exist and set imei to imeiexist
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("imeiExist");
            Query checkuser = reference.orderByChild("ic").equalTo(icfromsigninset);
            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        reference.child(icfromsigninset).child("imei").setValue(imeifromset, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(getApplicationContext(), "Data could not be saved in imeiexist" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    //imei saved in imei exist
                                    //save online cust
                                    myRef.child(idfromsigninset).child("OnlineCust").setValue(helperClass, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                Toast.makeText(getApplicationContext(), "Data could not be saved" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                //online cust saved
                                                Toast.makeText(getApplicationContext(), "Register success", Toast.LENGTH_SHORT).show();
                                                //saved data to session and to fisrtpage to login
                                                SessionManager sessionManager = new SessionManager(signinPassword.this);
                                                sessionManager.createLoginSession(idfromsigninset, usernamefromset, icfromsigninset, imeifromset);
                                                openfirst();
                                            }
                                        }
                                    });
                                }
                            }
                        });

                    } else {
                        //ic not exist in imei exist
                        Toast.makeText(getApplicationContext(), "IC not exist in imei exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("loadPost:onCancelled", databaseError.toException());
                    Toast.makeText(getApplicationContext(), "Database error in imeiexists", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            //not equal
            Toast.makeText(getApplicationContext(), "Password not equal", Toast.LENGTH_SHORT).show();
        }
    }

    private static Boolean isEqual(String a, String b) {
        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }
    }
}