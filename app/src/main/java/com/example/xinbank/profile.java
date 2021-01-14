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

import com.example.xinbank.Database.SessionManager;

public class profile extends AppCompatActivity {
    private String idfromlpw,namefromlpw,acctypefromlpw, accessControlfromlpw;
    private int accnumfromlpw,accbalancefromlpw;
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

        getdatafromloginpw();
        if(accessControlfromlpw.equals("false")){
            tranferbtn.setVisibility(View.GONE);
        }

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

        pacctype.append(" "+ acctypefromlpw);
        paccnum.setText(String.valueOf(accnumfromlpw));
        paccbalance.setText("RM "+ String.valueOf(accbalancefromlpw));
        pname.setText(namefromlpw);
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
}