package com.example.xinbank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xinbank.Database.SessionManager;

import java.util.Date;

public class afterSetPassword extends AppCompatActivity {
    private String timestamp;
    private Button login, exit;
    private TextView showtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_set_password);

        showtime = findViewById(R.id.logoutsTIME);
        login = findViewById(R.id.asetpwlogin);
        exit = findViewById(R.id.asetpwexit);
        getTime();
        showtime.append(timestamp);

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
                        afterSetPassword.super.onBackPressed();
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
    private void logout() {
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.logoutUserFromSession();
    }


}