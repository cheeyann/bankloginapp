package com.example.xinbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class fingerPrintcheck extends AppCompatActivity {
    private TextView msgtxt;
    private Button fverifybtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_printcheck);

        msgtxt = findViewById(R.id.showotutext);
        fverifybtn = findViewById(R.id.fingerverify_btn);

        BiometricManager biometricManager = BiometricManager.from(this);
        switch(biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgtxt.setText("\nFingerprint verification is needed before requesting, please press button below");
                msgtxt.setTextColor(Color.parseColor("#000000"));
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtxt.setText("\nNo Sensor");
                fverifybtn.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtxt.setText("\nSensor Unavailable");
                fverifybtn.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtxt.setText("\nNo fingerprint set, please check security setting to set your fingerprint else you cannot proceed to request your one time username");
                fverifybtn.setVisibility(View.GONE);
                break;
        }
        Executor executor = ContextCompat.getMainExecutor(this);

        //give us result
        final BiometricPrompt biometricPrompt = new BiometricPrompt(fingerPrintcheck.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Verified", Toast.LENGTH_SHORT).show();
                opensignin();
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

        fverifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

    }
    private void opensignin(){
        Intent intentsignin = new Intent(this,signIn.class);
        startActivity(intentsignin);
        finish();
    }

}