package com.example.xinbank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.xinbank.Database.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private String idfromsession,namefromsession, imeidevicefromsession;
    private TextView msgtxt, msgname;
    private Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msgtxt = findViewById(R.id.homehint);
        msgname = findViewById(R.id.homename);
        loginbtn = findViewById(R.id.firstregister_btn);
        getusername();
        msgname.setText(namefromsession);

        BiometricManager biometricManager = BiometricManager.from(this);
        switch(biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgtxt.setText("Fingerprint verification is needed before requesting, please press button below");
                msgtxt.setTextColor(Color.parseColor("#000000"));
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtxt.setText("no sensor");
                loginbtn.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtxt.setText("sensor unavailable");
                loginbtn.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtxt.setText("No fingerprint set, please check security setting to set your fingerprint else you cannot proceed to request your one time username");
                loginbtn.setVisibility(View.GONE);
                break;
        }
        Executor executor = ContextCompat.getMainExecutor(this);

        //give us result
        final BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "verified", Toast.LENGTH_SHORT).show();
                openAccessControl();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

        });
        //create biometric dialog
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint verification")
                .setDescription("Use your fingerprint to get into the app")
                .setNegativeButtonText("Cancel")
                .build();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    private void getusername(){
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> usersDetails = sessionManager.getUserDeatilFromSession();
        namefromsession = usersDetails.get(SessionManager.KEY_USERNAME);
    }
    private void openAccessControl(){
        Intent intent = new Intent(getApplicationContext(),access_control.class);
        startActivity(intent);
        finish();
    }
}