package com.example.xinbank;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ChoosePassword extends AppCompatActivity {
    private List<ShowImage> imageList;
    private List<String> encryptedPWList;
    private List<String> decryptedPWList;
    DatabaseReference myRef;
    DatabaseReference myPW;

    private ImageButton first;
    private ImageButton second;
    private ImageButton third;
    private ImageButton forth;
    private Button back;
    private TextView text;
    private Random rnd = new Random();
    private String encryptedPW;
    private String imageURL;
    private String file;

    //For encryption and decryption
    private static final int passwordIterations = 10;
    private static final int keySize = 128;
    private static final String cypherInstance = "AES/CBC/PKCS5Padding";
    private static final String secretKeyInstance = "PBKDF2WithHmacSHA1";
    private static final String plainText = "sampleText";
    private static final String AESSalt = "exampleSalt";
    private static final String initializationVector = "8119745113154120";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_password);

        first = (ImageButton) findViewById(R.id.imageBtn1);
        second = (ImageButton) findViewById(R.id.imageBtn2);
        third = (ImageButton) findViewById(R.id.imageBtn3);
        forth = (ImageButton) findViewById(R.id.imageBtn4);
        text = (TextView)findViewById(R.id.textView9);

                first.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "PIC 1", Toast.LENGTH_SHORT).show();
                    }
                });
                second.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "PIC 2", Toast.LENGTH_SHORT).show();
                        toafterset();
                    }
                });
                third.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "PIC 3", Toast.LENGTH_SHORT).show();
                    }
                });
                forth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "PIC 4", Toast.LENGTH_SHORT).show();
                    }
                });
            }

    private void toafterset() {
        startActivity(new Intent(this,profile.class));
        finish();
    }

}