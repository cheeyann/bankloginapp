package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Random;

public class show_otu extends AppCompatActivity {
    private TextView showotu;
    private static TextView showtime;
    private Button syesbtn, snobtn;
    String user_otu, otufromaccesscontrol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_otu);

        showotu = findViewById(R.id.showotutext);
        showtime = findViewById(R.id.showotuCountdowntxt);
        syesbtn = findViewById(R.id.showyesbtn);
        snobtn = findViewById(R.id.shownobtn);
        showotu.setText(showotumethod());
        countdown.starttimer();
        syesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdata();
            }
        });

        snobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    private boolean witnin5minute(Long timefromdb){
        Date date = new Date();
        long timemilli = date.getTime();
        long time1 = timefromdb ;
        long result = timemilli - time1;
        long sec = result / 1000 % 60;
        long minu = result / (60 * 1000) % 60;
        long hour = result / (60 * 60 * 1000);
        long day = result / (1000 * 60 * 60 * 24);
        if (day == 0){if(hour==0){if(minu == 0){if(sec < 30){return true;}}}}
        return false;
    }
    private void checkdata(){
        otufromaccesscontrol = sha.otuhashing(showotumethod());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("message");
        Query checkuserotu = reference.orderByChild("otu").equalTo(otufromaccesscontrol);

        checkuserotu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long timefromdb = dataSnapshot.child(otufromaccesscontrol).child("timestamp").getValue(Long.class);
                    if(witnin5minute(timefromdb)){
                        Toast.makeText(getApplicationContext(), "success, verify, within 30 sec", Toast.LENGTH_SHORT).show();
                        countdown.canceltimer();
                        openloginpassword();
                    }else{
                        Toast.makeText(getApplicationContext(), "otu expired, more than 30 sec", Toast.LENGTH_SHORT).show();
                        countdown.canceltimer();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Haven't Request, no OTU recorded"+ otufromaccesscontrol, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

    }
    private String showotumethod(){
        Intent intent = getIntent();
        user_otu = intent.getStringExtra("otu");
        return user_otu;
    }
    private void openloginpassword(){
        Intent intent = new Intent(getApplicationContext(),loginPassword.class);
        intent.putExtra("otu",otufromaccesscontrol);
        startActivity(intent);
        finish();
    }

    private static class countdown {
            static CountDownTimer countdowntimer = new CountDownTimer(30000, 1000) {
                public void onTick(long millisUntilFinished) {
                    showtime.setText("remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    showtime.setText("OTU Expired!");
                }
            };
        public static void starttimer(){
            countdowntimer.start();
        }
        public static void canceltimer(){
            countdowntimer.cancel();
        }
    }



    /*
    public void starttimer(){
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                textview_timer.setText("OTU valid time remaining: " + (millisUntilFinished / 60000)%60 + ":" + (millisUntilFinished / 1000)%60);
            }
            public void onFinish() {
                textview_timer.setText("OTU expired !");
            }
        }.start();
    }

     */
}

