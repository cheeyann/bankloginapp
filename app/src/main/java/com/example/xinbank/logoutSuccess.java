package com.example.xinbank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class logoutSuccess extends AppCompatActivity {
    private String timestamp;
    private Button homebtn;
    private TextView showtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_success);

        showtime = findViewById(R.id.logoutsTIME);
        homebtn = findViewById(R.id.logouthomebtn);
        getTime();
        showtime.append(timestamp);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homepage();
            }
        });

    }
    private void homepage(){
        startActivity(new Intent(this,firstpage.class));
        finish();
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
                        logoutSuccess.super.onBackPressed();
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