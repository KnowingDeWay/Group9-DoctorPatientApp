package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softwareapp.group9.doctorpatientapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TakeImageFromCameraActivity extends AppCompatActivity {
    private static final String TAG = "UploadActivity";
    private ImageView imageTakenFromCamera;
    private EditText imageName;
    private Button btnUpload, btnTakeImage;
    private ProgressDialog mProgressDialog;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference mStorageRef;
    private FirebaseAuth auth;

    public TakeImageFromCameraActivity(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_camera_images);
        imageTakenFromCamera = (ImageView) findViewById(R.id.ImageViewUploadImageFromCamera);
        imageName=(EditText)findViewById(R.id.imageNameFromCamera);
        btnUpload = (Button) findViewById(R.id.btnUploadImageFromCamera);
        btnTakeImage=(Button) findViewById(R.id.btnOpenCamera);
        mProgressDialog = new ProgressDialog(TakeImageFromCameraActivity.this);
        auth = FirebaseAuth.getInstance();

       //Firebase init
        mStorageRef = FirebaseStorage.getInstance().getReference();

        btnTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uplaod();
            }
        });
    }

    public void takeImage(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(cameraIntent, Init.CAMER_REQUEST_CODE);
    }

    public Bitmap compressBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return bitmap;
    }

    public void uplaod(){
        //get the signed in user
        FirebaseUser user = auth.getCurrentUser();
        String userID = user.getUid();

        String name = imageName.getText().toString();

        if (filePath != null){
           // final ProgressDialog progressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Uploading");
            mProgressDialog.show();


            StorageReference ref = mStorageRef.child("users/" + userID.toString() + "/media/image/" + name + ".jpg");
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Toast.makeText(TakeImageFromCameraActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                     }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            Toast.makeText(TakeImageFromCameraActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mProgressDialog.setMessage("Uploaded " + (int)progress + "%");
                }
            });
        }else {
            Toast.makeText(this, "Failed to get filePath", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Init.CAMER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: done taking a picture");

            //get the new image bitmap
            filePath = data.getData();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

           // Log.d(TAG, "onActivityResult: received bitmap: " + bitmap);
            if (bitmap != null){
                //compress the image if you like
                compressBitmap(bitmap,80);
                imageTakenFromCamera.setImageBitmap(bitmap);
            }
        }
    }
}
