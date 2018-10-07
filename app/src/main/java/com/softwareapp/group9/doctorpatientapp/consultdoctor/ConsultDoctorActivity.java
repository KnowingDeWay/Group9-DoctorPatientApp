package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softwareapp.group9.doctorpatientapp.R;
//import com.softwareapp.group9.doctorpatientapp.UploadDocument;
import com.softwareapp.group9.doctorpatientapp.doctorfeedback.DoctorFeedbackActivity;
import com.softwareapp.group9.doctorpatientapp.facilitiesnearme.FacilitiesNearMeActivity;
import com.softwareapp.group9.doctorpatientapp.medicalcondition.ViewMedicalConditionActivity;
import com.softwareapp.group9.doctorpatientapp.userprofile.PatientProfileActivity;

import java.io.ByteArrayOutputStream;
import android.app.ProgressDialog;

public class ConsultDoctorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView navigationView;

    private Button selectFile;
    private Button uploadImage;

    Button selectFile1, upload;
    TextView notification;
    Uri pdfUri; //urls meant for local storage
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_patient_consult_doctor_home);
        mToolbar = (Toolbar) findViewById(R.id.appTb);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_patient);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.patientNv);
        navigationView.setNavigationItemSelectedListener(this);
        setTitle("Consult Doctor");


        final Button uploadImage = (Button) findViewById(R.id.button4);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePhotoDialog dialog = new ChangePhotoDialog();
                dialog.show(getFragmentManager(), getString(R.string.change_photo_dialog));
//                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
//                startActivity(intent);
            }
        });

//   selectFile = (Button) findViewById(R.id.selectfile);
//        selectFile.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//            openUploadDocument();
//            }
//        });


        //doc all file here
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        final ProgressDialog progressDialog;

        selectFile1 = findViewById(R.id.selectfile1);
        upload = findViewById(R.id.upload);
        notification = findViewById(R.id.notification);

        selectFile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(ConsultDoctorActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                } else
                    ActivityCompat.requestPermissions(ConsultDoctorActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfUri != null)
                    uploadFile(pdfUri);
                else
                    Toast.makeText(ConsultDoctorActivity.this, "Select File", Toast.LENGTH_SHORT).show();
            }
        });
    }

            private void uploadFile(Uri pdfUri) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("Uploading File..");
                progressDialog.setProgress(0);
                progressDialog.show();


                final String fileName=System.currentTimeMillis()+"";
                StorageReference storageReference=storage.getReference(); //Returns root path
                storageReference.child("Uploads").child(fileName).putFile(pdfUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String url=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();//returns the uploaded file..
                                // store url in realtime database
                                DatabaseReference reference=database.getReference(); //returns the path to root

                                reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                            Toast.makeText(ConsultDoctorActivity.this,"File Uploaded",Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(ConsultDoctorActivity.this,"File Not Uploaded",Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ConsultDoctorActivity.this,"Fill Not Uploaded",Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //track the progress of upload...
                        int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressDialog.setProgress(currentProgress);


                    }
                });
            }
//        });
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Check req code is '9' or not

        if(requestCode==9 && grantResults[0]== PackageManager.PERMISSION_GRANTED )
        {
            selectPdf();
        }
        else
            Toast.makeText(ConsultDoctorActivity.this,"please provide permission..",Toast.LENGTH_SHORT).show();
    }

    private void selectPdf() {

        //to offer using file maneger
        //will be using intent
        Intent intent= new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);//to fetch files
        startActivityForResult(intent,  88);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //check file selected or not
        if(requestCode==88 && resultCode==RESULT_OK && data!=null)
        {
            pdfUri=data.getData();//return for selected file
            notification.setText("A File is Selected"+ data.getData().getLastPathSegment());
        }
        else{
            Toast.makeText(ConsultDoctorActivity.this,"Select a file",Toast.LENGTH_SHORT).show();        }

    }

    /**
     * Compress a bitmap by the @param "quality"
     * Quality can be anywhere from 1 -100 : 100 being the highest quality.
     * @param bitmap
     * @param quality
     * @return
     */
    public Bitmap compressBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return bitmap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        if(mToggle.onOptionsItemSelected(menuItem)){
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();
        //Plan here is to have all the home screens of all the other features in one project so that our navigation bar can access them
        switch(id) {
            case R.id.nav_profile: Intent intent = new Intent(this, PatientProfileActivity.class); startActivity(intent); finish(); break;
            case R.id.nav_condition: Intent intent2 = new Intent(this, ViewMedicalConditionActivity.class); startActivity(intent2); finish(); break;
            case R.id.nav_facilities: Intent intent4 = new Intent(this, FacilitiesNearMeActivity.class); startActivity(intent4); finish(); break;
            case R.id.nav_feedback: Intent intent5 = new Intent(this, DoctorFeedbackActivity.class); startActivity(intent5); finish(); break;
            case R.id.nav_logout: System.exit(0);
        }
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout_patient);
        layout.closeDrawer(GravityCompat.START);
        return true;
    }


    //  protected void OnCreate(Bundle savedInstanceState){

    //    selectFile = (Button) findViewById(R.id.selectfile);
    //  selectFile.setOnClickListener((view)→{ openUploadDocument(); });
    //}

//    public void openUploadDocument(){
//
//        Intent intent = new Intent (this, UploadDocument.class);
//        startActivity(intent);
//    }

}
