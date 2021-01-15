package com.example.xinbank;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class UploadPasswords extends AppCompatActivity {
    private StorageReference mStorage;
    private DatabaseReference myRef;

    private static final int RESULT_LOAD_IMAGE = 1;
    private Button btnUploadPW;
    private Button btnDone;
    private Button btnBack;
    private RecyclerView mUploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private UploadPictureForPassword uploadPicture;

    String encryptedFilename;

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
        setContentView(R.layout.activity_upload_passwords);

        mStorage = FirebaseStorage.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference("Datas");

        btnUploadPW = (Button) findViewById(R.id.button_uploadpw);
        btnDone = (Button)findViewById(R.id.btnNext2);
        btnBack = (Button)findViewById(R.id.button_backToUpload);
        mUploadList = (RecyclerView) findViewById(R.id.recyclerView2);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        uploadPicture = new UploadPictureForPassword(fileNameList, fileDoneList);

        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadPicture);

        btnUploadPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileDoneList.size() != 0) {
                    Toast.makeText(UploadPasswords.this, "You have been registered successfully!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UploadPasswords.this, afterSetPassword.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(UploadPasswords.this, "Please upload at least 1 photo.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadPasswords.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ImageInDatabase saveImageData = null;

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {
                int totalItemSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemSelected; i++) {
                    final Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    final String fileName = getFileName(fileUri);
                    //String fileNameOnly = fileName.replaceFirst("[.][^.]+$", "");

                    fileNameList.add(fileName);
                    fileDoneList.add("Uploading");
                    uploadPicture.notifyDataSetChanged();



                    final StorageReference imageToUpload = mStorage.child("Images").child(fileName);
                    final DatabaseReference fileToUpload = myRef.child("Datas");
                    final DatabaseReference pwToUpload = myRef.child("Passwords");

                    final int finalI = i;

                    imageToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(UploadPasswords.this, "Done", Toast.LENGTH_SHORT).show();

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "Done");

                            uploadPicture.notifyDataSetChanged();



                            Task<Uri> downloadUrl = imageToUpload.getDownloadUrl();
                            downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadedUrl = uri.toString();
                                    ImageInDatabase save = new ImageInDatabase();
                                    save.setImageUrl(downloadedUrl);
                                    save.setFilename(getFileName(fileUri));
                                    fileToUpload.push().setValue(save);

                                    try {
                                        encryptedFilename = encrypt(fileName);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    PasswordList pwlist = new PasswordList();
                                    pwlist.setPassword(encryptedFilename);
                                    pwToUpload.push().setValue(pwlist);
                                }
                            });
                        }
                    });
                }
            } else if (data.getData() != null) {
                Toast.makeText(UploadPasswords.this, "Selected Single Files", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try{
                if (cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }

        if (result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String encrypt (String textToEncrypt) throws Exception{
        SecretKeySpec skeySpec = new SecretKeySpec(getRaw(plainText, AESSalt), "AES");
        Cipher cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
        byte[] encypyted = cipher.doFinal(textToEncrypt.getBytes());
        return Base64.encodeToString(encypyted, Base64.DEFAULT);

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