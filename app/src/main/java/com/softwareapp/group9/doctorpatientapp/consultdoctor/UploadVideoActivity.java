package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softwareapp.group9.doctorpatientapp.LoginActivity;
import com.softwareapp.group9.doctorpatientapp.R;

import java.io.File;
import java.util.List;

public class UploadVideoActivity extends AppCompatActivity {
    private Uri videoUri;
    private StorageReference videoRef;
    private FirebaseAuth auth;
    private static final int REQUEST_CODE = 101;

    private Button record;
    private Button upload;
    private Button download;
    private EditText videoName;


    public UploadVideoActivity(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_video_layout);

        auth = FirebaseAuth.getInstance();

        String uid = auth.getCurrentUser().getUid();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        videoName = (EditText) findViewById(R.id.EditTextViewForUploadedVideo);
        String name = videoName.getText().toString();
        videoRef = storageRef.child("users/" + uid.toString() + "/media/video/" +  name +".3gp");//"/patientRecord"+

        record = findViewById(R.id.btnRecordVideo);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });

        upload = findViewById(R.id.btnUploadVideo);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo();
            }
        });

        download = findViewById(R.id.btnDownloadVideo);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
    }

    public void uploadVideo(){
        if (videoUri != null){
            UploadTask uploadTask = videoRef.putFile(videoUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadVideoActivity.this, "Uplaod failed: " +e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UploadVideoActivity.this, "Uplaod complete", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    updateProgress(taskSnapshot);
                }
            });
        }else{
            Toast.makeText(UploadVideoActivity.this, "Nothing to uplaod", Toast.LENGTH_LONG).show();
        }
    }

    public void updateProgress(UploadTask.TaskSnapshot taskSnapshot){
        long fileSize = taskSnapshot.getTotalByteCount();
        long uploadBytes = taskSnapshot.getBytesTransferred();
        long progress = (100 * uploadBytes) / fileSize;
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress((int) progress);
    }

    public void record(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void download(){
        try{
             final File localFile = File.createTempFile("patientRecord", "3gp");

            videoRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UploadVideoActivity.this, "Download complete", Toast.LENGTH_LONG).show();
                    final VideoView videoView = (VideoView) findViewById(R.id.videoView);
                    videoView.setVideoURI(Uri.fromFile(localFile));
                    videoView.start();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadVideoActivity.this, "Download failed: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(UploadVideoActivity.this, "Failed to create temp file: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
            videoUri = data.getData();
            if (requestCode == REQUEST_CODE){
                if (requestCode == REQUEST_CODE){
                    if (requestCode == RESULT_OK){
                        Toast.makeText(this,"Video saved to:\n" + videoUri, Toast.LENGTH_LONG).show();
                    }else if (requestCode == RESULT_CANCELED){
                        Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(this, "Video saved to:\n" + videoUri, Toast.LENGTH_LONG).show();
                    }
                }

            }
    }
}
