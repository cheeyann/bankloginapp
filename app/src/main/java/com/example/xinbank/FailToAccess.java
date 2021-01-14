package com.example.xinbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FailToAccess extends AppCompatActivity {
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail_to_access);

        back = (Button)findViewById(R.id.btn_backWhenFail);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FailToAccess.this, ChoosePassword.class);
                startActivity(intent);
            }
        });
    }
}