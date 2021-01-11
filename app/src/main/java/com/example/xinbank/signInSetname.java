package com.example.xinbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class signInSetname extends AppCompatActivity {
    private TextView msg, setusername, setimei;
    private Button nextBTN;
    String namefromsignin, idfromsignin,cardnumfromsignin,icfromsignin, phonefromsignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_setname);
        msg = findViewById(R.id.textViewName);
        setusername = findViewById(R.id.Ssetname);
        setimei = findViewById(R.id.SImei);
        nextBTN = findViewById(R.id.signinbtn);

        showfullname();
        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setname();
            }
        });


    }
    private void showfullname(){
        Intent intent = getIntent();
        namefromsignin = intent.getStringExtra("name");
        idfromsignin = intent.getStringExtra("id");
        cardnumfromsignin = intent.getStringExtra("cardnum");
        icfromsignin = intent.getStringExtra("ic");
        phonefromsignin = intent.getStringExtra("phone");
        msg.setText(namefromsignin);
    }
    private void setname(){
        final String usernameinset = setusername.getText().toString().trim();
        final String imeiinset = setimei.getText().toString().trim();

        Intent intent = new Intent(getApplicationContext(),signinPassword.class);
        intent.putExtra("name",namefromsignin);
        intent.putExtra("id",idfromsignin);
        intent.putExtra("cardnum",cardnumfromsignin);
        intent.putExtra("username",usernameinset);
        intent.putExtra("imei",imeiinset);
        intent.putExtra("ic",icfromsignin);
        Toast.makeText(getApplicationContext(), "Data captured", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }
}