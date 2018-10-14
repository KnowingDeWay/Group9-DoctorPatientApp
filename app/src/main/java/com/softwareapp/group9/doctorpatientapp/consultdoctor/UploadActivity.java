package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softwareapp.group9.doctorpatientapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class UploadActivity extends AppCompatActivity{

    private static final String TAG = "UploadActivity";

    public interface OnPhotoReceivedListener{

        public void getImagePath(Bitmap imagePath);
    }
    OnPhotoReceivedListener mOnPhotoReceived;


    //declare variables
    private ImageView image;
    private EditText imageName;
    private Button btnUpload, btnChooseImage;
    private ProgressDialog mProgressDialog;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference mStorageRef;
    private FirebaseAuth auth;
    private String packetId;
    private DataPacket packet;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private String referenceString;
    private String userId;

    public UploadActivity(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_layout);
        image = (ImageView) findViewById(R.id.uploadImage);
        btnChooseImage = (Button) findViewById(R.id.btnChooseImage);
        btnUpload = (Button) findViewById(R.id.btnUploadImage);
        imageName = (EditText) findViewById(R.id.imageName);

        mProgressDialog = new ProgressDialog(UploadActivity.this);
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        packetId = getIntent().getStringExtra("packet");
        database = FirebaseDatabase.getInstance();
        referenceString = "Users/Patients/" + userId + "/Packets";
        databaseReference = database.getReference(referenceString);
        //Firebase init
        mStorageRef = FirebaseStorage.getInstance().getReference();


        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "önClick: Uploading Image.");

                uploadImage();
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPacket();
    }

    public void getPacket(){
        Query query = databaseReference.orderByKey().equalTo(packetId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    packet = snapshot.getValue(DataPacket.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addToMediaFileReference(String referencePath){
        if(packet.mediaReferences == null){
            packet.mediaReferences = new ArrayList<>();
        }
        packet.mediaReferences.add(referencePath);
        databaseReference.child(packetId).setValue(packet);
    }

    private void uploadImage() {
        //get the signed in user
        FirebaseUser user = auth.getCurrentUser();
        String userID = user.getUid();

        String name = imageName.getText().toString();

        if (filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();


            StorageReference ref = mStorageRef.child("users/" + userID.toString() + "/media/image/" + name + ".jpg");
            final String successUid = user.getUid();
            final String successName = name;
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    addToMediaFileReference("users/" + successUid + "/media/image/"  + successName + ".jpg");
                    Toast.makeText(UploadActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            })
           .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                }
            });
        }

    }

//    private void checkFilePermissions(){
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
//            int permissionCheck = UploadActivity.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
//            permissionCheck += UploadActivity.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
//            if(permissionCheck != 0){
//                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);//any number
//            }
//        }else {
//            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
//        }
//    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                image.setImageBitmap(bitmap);
//                mOnPhotoReceived.getImagePath(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
