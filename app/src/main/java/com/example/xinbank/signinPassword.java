package com.example.xinbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class signinPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_password);

        Button gotorequestbtn = findViewById(R.id.requestmainpagebtn);

        gotorequestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openmain();
            }
        });
    }
    private void openmain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}