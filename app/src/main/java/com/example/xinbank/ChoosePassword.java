package com.example.xinbank;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private List<PasswordList> encryptedPWList;
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
        imageList = new ArrayList<>();
        encryptedPWList = new ArrayList<>();
        decryptedPWList = new ArrayList<>();
        final ArrayList<ImageButton> btnList = new ArrayList<>();
        btnList.add(first);
        btnList.add(second);
        btnList.add(third);
        btnList.add(forth);
/*
        back = (Button)findViewById(R.id.btn_backToUploadPW);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoosePassword.this, UploadPasswords.class);
                startActivity(intent);
            }
        });

 */

        myRef = FirebaseDatabase.getInstance().getReference("Datas").child("Datas");
        myPW = FirebaseDatabase.getInstance().getReference("Datas").child("Passwords");

        //get passwords from database and add to list
        myPW.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot di : dataSnapshot.getChildren()) {
                    PasswordList getEncryptedPW = di.getValue(PasswordList.class);
                    encryptedPWList.add(getEncryptedPW);
                }
                for(int i=0;i<encryptedPWList.size();i++) {
                    try {
                        PasswordList getEncryptedPW = encryptedPWList.get(i);
                        String toDecrypt = getEncryptedPW.getPassword();
                        String decryptedPW = decrypt(toDecrypt);
                        decryptedPWList.add(decryptedPW);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get data from database and add to list
        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot di : dataSnapshot.getChildren()) {
                    ShowImage showImageList = di.getValue(ShowImage.class);
                    imageList.add(showImageList);
                }

                int random = rnd.nextInt(imageList.size());
                ArrayList<String> URLlist = new ArrayList<String>();
                ArrayList<String> filename = new ArrayList<>();
                ArrayList<String> password = new ArrayList<>();
                ArrayList<String> passwordFilename = new ArrayList<>();

                for (int i = 0; i < imageList.size(); i++) {
                    ShowImage getData = imageList.get(random);
                    imageURL = getData.getImageUrl();
                    file = getData.getFilename();

                    while (URLlist.contains(imageURL)) {
                        random = rnd.nextInt(imageList.size());
                        getData = imageList.get(random);
                        imageURL = getData.getImageUrl();
                        file = getData.getFilename();
                    }
                    URLlist.add(imageURL);
                    filename.add(file);
                }

                int i = 0;
                password.clear();
                //1th is password
                if (decryptedPWList.contains(filename.get(i))) {
                    password.add(URLlist.get(i));
                    passwordFilename.add(filename.get(i));
                    URLlist.remove(i);
                    filename.remove(i);
                    while (decryptedPWList.contains(filename.get(i)) && password.size()!=4) {
                        i++;
                        while ((!decryptedPWList.contains(filename.get(i))) && password.size()!=4) {
                            password.add(URLlist.get(i));
                            passwordFilename.add(filename.get(i));
                            URLlist.remove(i);
                            filename.remove(i);
                        }
                    }
                    while ((!decryptedPWList.contains(filename.get(i))) && password.size()!=4) {
                        password.add(URLlist.get(i));
                        passwordFilename.add(filename.get(i));
                        URLlist.remove(i);
                        filename.remove(i);
                        while (decryptedPWList.contains(filename.get(i)) && password.size()!=4) {
                            i++;
                        }
                    }
                }

                //1st not password
                else{
                    password.add(URLlist.get(i));
                    passwordFilename.add(filename.get(i));
                    URLlist.remove(i);
                    filename.remove(i);
                    //2nd is password
                    if (decryptedPWList.contains(filename.get(i))){
                        password.add(URLlist.get(i));
                        passwordFilename.add(filename.get(i));
                        URLlist.remove(i);
                        filename.remove(i);
                        while (decryptedPWList.contains(filename.get(i)) && password.size()!=4){
                            i++;
                            while ((!decryptedPWList.contains(filename.get(i))) && password.size()!=4){
                                password.add(URLlist.get(i));
                                passwordFilename.add(filename.get(i));
                                URLlist.remove(i);
                                filename.remove(i);
                            }
                        }

                        while ((!decryptedPWList.contains(filename.get(i))) && password.size()!=4) {
                            password.add(URLlist.get(i));
                            passwordFilename.add(filename.get(i));
                            URLlist.remove(i);
                            filename.remove(i);
                            while (decryptedPWList.contains(filename.get(i)) && password.size()!=4) {
                                i++;
                            }
                        }
                    }
                    //2nd not password
                    else{
                        password.add(URLlist.get(i));
                        passwordFilename.add(filename.get(i));
                        URLlist.remove(i);
                        filename.remove(i);
                        //3rd is pw
                        if (decryptedPWList.contains(filename.get(i))){
                            password.add(URLlist.get(i));
                            passwordFilename.add(filename.get(i));
                            URLlist.remove(i);
                            filename.remove(i);
                            while (decryptedPWList.contains(filename.get(i)) && password.size() != 4) {
                                i++;
                                while ((!decryptedPWList.contains(filename.get(i))) && password.size() != 4) {
                                    password.add(URLlist.get(i));
                                    passwordFilename.add(filename.get(i));
                                    URLlist.remove(i);
                                    filename.remove(i);
                                }
                            }
                            while ((!decryptedPWList.contains(filename.get(i))) && password.size()!=4) {
                                password.add(URLlist.get(i));
                                passwordFilename.add(filename.get(i));
                                URLlist.remove(i);
                                filename.remove(i);
                                while (decryptedPWList.contains(filename.get(i)) && password.size()!=4) {
                                    i++;
                                }
                            }
                        }
                        //3rd not pw
                        else{
                            password.add(URLlist.get(i));
                            passwordFilename.add(filename.get(i));
                            URLlist.remove(i);
                            filename.remove(i);
                            //4th is pw
                            if (decryptedPWList.contains(filename.get(i))){
                                password.add(URLlist.get(i));
                                passwordFilename.add(filename.get(i));
                                URLlist.remove(i);
                                filename.remove(i);
                            }else{
                                while((!decryptedPWList.contains(filename.get(i))) && password.size() != 4){
                                    i++;
                                    if (decryptedPWList.contains(filename.get(i))){
                                        password.add(URLlist.get(i));
                                        passwordFilename.add(filename.get(i));
                                        URLlist.remove(i);
                                        filename.remove(i);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }


                ArrayList<String> newPW = new ArrayList<>();
                final ArrayList<String> newPWFilename = new ArrayList<>();
                if (password.size() == 4){
                    for (int j=0;j<password.size();j++){
                        int nRandom = rnd.nextInt(password.size());
                        String a = password.get(nRandom);
                        String b = passwordFilename.get(nRandom);

                        while (newPW.contains(a)) {
                            nRandom = rnd.nextInt(password.size());
                            a = password.get(nRandom);
                            b = passwordFilename.get(nRandom);
                        }
                        newPW.add(a);
                        newPWFilename.add(b);
                    }

                    for (int j=0;j<newPW.size();j++){
                        Glide.with(ChoosePassword.this).load(newPW.get(j)).into(btnList.get(j));
                    }
                }else{
                    Intent refresh = new Intent(ChoosePassword.this, ChoosePassword.class);
                    startActivity(refresh);
                }
                password.clear();

                first.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (decryptedPWList.contains(newPWFilename.get(0))){
                            Intent intent = new Intent(ChoosePassword.this, profile.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(ChoosePassword.this, FailToAccess.class);
                            startActivity(intent);
                        }
                    }
                });
                second.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (decryptedPWList.contains(newPWFilename.get(1))){
                            Intent intent = new Intent(ChoosePassword.this, profile.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(ChoosePassword.this, FailToAccess.class);
                            startActivity(intent);
                        }
                    }
                });
                third.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (decryptedPWList.contains(newPWFilename.get(2))){
                            Intent intent = new Intent(ChoosePassword.this, profile.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(ChoosePassword.this, FailToAccess.class);
                            startActivity(intent);
                        }
                    }
                });
                forth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (decryptedPWList.contains(newPWFilename.get(3))){
                            Intent intent = new Intent(ChoosePassword.this, profile.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(ChoosePassword.this, FailToAccess.class);
                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static String decrypt (String textToDecrypt) throws Exception{
        byte[] encrypted_bytes = Base64.decode(textToDecrypt, Base64.DEFAULT);
        SecretKeySpec skeySpec = new SecretKeySpec(getRaw(plainText, AESSalt), "AES");
        Cipher cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
        byte[] decrypted = cipher.doFinal(encrypted_bytes);
        return new String(decrypted, "UTF-8");

    }

    private static byte[] getRaw (String plainText, String salt){
        try{
            SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyInstance);
            KeySpec spec = new PBEKeySpec(plainText.toCharArray(), salt.getBytes(), passwordIterations, keySize);
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return new byte[0];

    }
}