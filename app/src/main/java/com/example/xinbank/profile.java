package com.example.xinbank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class profile extends AppCompatActivity {
    private String idfromlpw,namefromlpw,acctypefromlpw, accessControlfromlpw;
    private int accnumfromlpw,accbalancefromlpw;
    private TextView paccnum, paccbalance, pname,pacctype;
    private Button logoutBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        paccnum = findViewById(R.id.showotutext);
        paccbalance = findViewById(R.id.showaccbalance);
        pname = findViewById(R.id.showname);
        pacctype = findViewById(R.id.textView2);
        logoutBTN = findViewById(R.id.fingerverify_btn);

        getdatafromloginpw();

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void getdatafromloginpw(){
        Intent intent = getIntent();
        idfromlpw = intent.getStringExtra("id");
        namefromlpw = intent.getStringExtra("name");
        accnumfromlpw = intent.getIntExtra("accNum",0);
        accbalancefromlpw = intent.getIntExtra("accBalance",0);
        acctypefromlpw = intent.getStringExtra("accType");
        accessControlfromlpw = intent.getStringExtra("accessControl");

        pacctype.setText(acctypefromlpw+" account " + accessControlfromlpw);
        paccnum.setText(String.valueOf(accnumfromlpw));
        paccbalance.setText("RM "+ String.valueOf(accbalancefromlpw));
        pname.setText(namefromlpw);
    }
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        profile.super.onBackPressed();
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