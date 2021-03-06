package com.example.xinbank;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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

import java.util.ArrayList;
import java.util.List;

public class ymain extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private Button btnUpload;
    private Button btnNext;
    private RecyclerView mUploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private UploadPicture uploadPicture;

    private StorageReference mStorage;
    private DatabaseReference myRef;

    public ymain() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ymain);

        mStorage = FirebaseStorage.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference("Datas");

        btnUpload = (Button) findViewById(R.id.button_upload);
        btnNext = (Button)findViewById(R.id.btnNext);
        mUploadList = (RecyclerView) findViewById(R.id.recyclerView);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        uploadPicture = new UploadPicture(fileNameList, fileDoneList);

        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadPicture);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ymain.this, UploadPasswords.class);
                startActivity(intent);
            }
        });

        /*showImage = findViewById(R.id.imageView);
        showImage.setHasFixedSize(true);
        showImage.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration deco = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        showImage.addItemDecoration(deco);
        imageList = new ArrayList<>();
        myRef = FirebaseDatabase.getInstance().getReference("Images");
        getImageData();*/
    }

    /*private void getImageData() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot di:dataSnapshot.getChildren()){
                    ShowImage showImageList = di.getValue(ShowImage.class);
                    imageList.add(showImageList);
                }
                imageAdapter adapter = new imageAdapter(imageList, getApplicationContext());
                showImage.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ImageInDatabase saveImageData = null;

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if(data.getClipData() != null){
                int totalItemSelected = data.getClipData().getItemCount();

                for(int i=0; i<totalItemSelected; i++){
                    final Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("Uploading");
                    uploadPicture.notifyDataSetChanged();

                    final StorageReference imageToUpload = mStorage.child("Images").child(fileName);
                    final DatabaseReference fileToUpload = myRef.child("Datas");


                    final int finalI = i;

                    imageToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();


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
                                }
                            });
                        }
                    });


                    //update with generated key(new)
                    //String key = fileToUpload.getKey();
                    //saveImageData.setKey(key);
                }

                //Toast.makeText(MainActivity.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();

            }else if (data.getData() != null){
                Toast.makeText(ymain.this, "Selected Single Files", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /*private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        downloadManager.enqueue(request);
    }*/

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
}