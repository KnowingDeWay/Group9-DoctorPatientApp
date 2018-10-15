package com.softwareapp.group9.doctorpatientapp.consultdoctor;



import android.app.ProgressDialog;

import android.Manifest;

import android.app.ProgressDialog;

import android.content.Context;

import android.content.Intent;

import android.content.pm.PackageManager;

import android.graphics.Bitmap;

import android.location.Location;
import android.net.ConnectivityManager;

import android.net.Uri;

import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;

import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;

import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.support.v7.widget.Toolbar;

import android.text.TextUtils;

import android.util.Log;
import android.view.MenuItem;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.ImageView;

import android.widget.Toast;

import android.widget.TextView;

import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.Query;

import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.OnProgressListener;

import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.UploadTask;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

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

import com.softwareapp.group9.doctorpatientapp.facilitiesnearme.UserLocation;
import com.softwareapp.group9.doctorpatientapp.medicalcondition.ViewMedicalConditionActivity;

import com.softwareapp.group9.doctorpatientapp.userprofile.CustomDialogBoxActivity;

import com.softwareapp.group9.doctorpatientapp.userprofile.PatientProfileActivity;



import java.io.ByteArrayOutputStream;

import java.util.ArrayList;



import android.app.ProgressDialog;



public class ConsultDoctorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    private static final String TAG = ConsultDoctorActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolbar;

    private NavigationView navigationView;

    private ImageView imageViewPatient;

    private String mSelectedImagePath;

    private Button btnConsultDoctor;

    private DataPacket packet;

    private static final String TAG2 = "CurrentLocationApp";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLocationDatabaseReference;
    Button saveLocationToFirebase;
    String value_lat = null;
    String value_lng = null;

    private StorageReference mStorageRef;

    private FirebaseAuth auth;



    private Byte filePath;
    private Button selectFile;

    private Button uploadImage;
    private Button heartBTn;
    private boolean camPermission;
    Button selectFile1, upload;
    TextView notification;
    Uri pdfUri; //urls meant for local storage
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;



    private DatabaseReference databaseReference;

    private FirebaseDatabase packetDb;

    private String dbReference;

    private FirebaseUser user;

    private String userId;

    private String reference;

    private String mapReference;

    private String packetId;

    private EditText conditionEt;

    private EditText descriptionEt;

    private TextView heartBeatTv;

    private String heartRate;



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

        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();

        if(user != null) {

            userId = user.getUid();

            reference = "users/" + userId + "/files";

        }

        packetDb = FirebaseDatabase.getInstance();

        dbReference = "Users/Patients/" + userId + "/Packets";

        databaseReference = packetDb.getReference(dbReference);

        packetId = databaseReference.push().getKey();

        packet = createPacket();

        packet.packetId = packetId;

        setPacket();

        mapReference = "Users/Patients/" + userId + "/Packets/" + packetId + "/patientLocation";

        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mLocationDatabaseReference = mFirebaseDatabase.getReference(mapReference);
        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));
        saveLocationToFirebase = (Button) findViewById(R.id.getLocation);
        buildGoogleApiClient();

        conditionEt = (EditText) findViewById(R.id.editText4);

        descriptionEt = (EditText) findViewById(R.id.editText3);

        heartBeatTv = findViewById(R.id.heartBeatTv);

        heartRate = getIntent().getStringExtra("HeartBeat");

        heartBeatTv.setText(heartRate);

        setTitle("Consult Doctor");

        imageViewPatient = (ImageView) findViewById(R.id.imageViewPatient);

        //   btnConsultDoctor = (Button) findViewById(R.id.btnConsultDoctor);

        final Button uploadImage = (Button) findViewById(R.id.button4);



        uploadImage.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {



                /*

                make sure all permissions have been verified before opening the dialog

                 */



                ChangePhotoDialog dialog = new ChangePhotoDialog();

                dialog.packetId = packetId;

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

        upload = findViewById(R.id.btnConsultDoctor);

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

        heartBTn = findViewById(R.id.heartBtn);







        heartBTn.setOnClickListener(new View.OnClickListener() {



            @Override



            public void onClick(View v) {



                if(ContextCompat.checkSelfPermission(ConsultDoctorActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)



                {



                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);







                }



                else



                {



                    Log.i("TEST","Granted");



                    Intent intent = new Intent(ConsultDoctorActivity.this, HeartRate.class);



                    startActivity(intent);





                }



                Log.d("hello", "" +camPermission);



















            }



        });

        upload.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                if (pdfUri != null){

                    uploadFile(pdfUri);

                }

                String condition = conditionEt.getText().toString();

                String description = descriptionEt.getText().toString();

                String heartBeat = heartBeatTv.getText().toString();

                if(TextUtils.isEmpty(condition)){

                    showDialog("Error", "Please enter condition.");

                } else {

                    packet.patientId = user.getUid();

                    packet.condition = condition;

                    packet.description = description;

                    packet.heartBeat = heartBeat;

                    databaseReference.child(packetId).setValue(packet);

                    Intent intent = new Intent(getApplicationContext(), ChooseDoctorActivity.class);

                    intent.putExtra("packetId", packetId);

                    startActivity(intent);

                    finish();

                }

                /**

                 else

                 Toast.makeText(ConsultDoctorActivity.this, "Select File", Toast.LENGTH_SHORT).show();

                 **/

            }



        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    public void setPacket(){

        databaseReference.child(packetId).setValue(packet);

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

            value_lat= String.valueOf(mLastLocation.getLatitude());
            value_lng =String.valueOf(mLastLocation.getLongitude());
            mLatitudeText.setText(value_lat);
            mLongitudeText.setText(value_lng);



            saveLocationToFirebase.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    UserLocation location = new UserLocation(value_lat, value_lng);
                    packet.location = location;
                    setPacket();
                    Toast.makeText(ConsultDoctorActivity.this ,"Location saved", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }



    @Override
    protected void onStart() {
        super.onStart();
        getPacket();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }



    public DataPacket createPacket(){

        DataPacket dataPacket = new DataPacket();

        databaseReference.child(packetId).setValue(dataPacket);

        return dataPacket;

    }



    public void addToPacketFileReference(String referencePath){

        if(packet.filesReferences == null){

            packet.filesReferences = new ArrayList<>();

        }

        packet.filesReferences.add(referencePath);

        databaseReference.child(packetId).setValue(packet);

    }



    private void uploadFile(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.setTitle("Uploading File..");

        progressDialog.setProgress(0);

        progressDialog.show();





        final String fileName=System.currentTimeMillis()+"";

        StorageReference storageReference=storage.getReference(reference); //Returns root path

        addToPacketFileReference(reference + "/" + fileName + ".pdf");

        storageReference.child(fileName).putFile(pdfUri)

                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override

                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String url=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();//returns the uploaded file..

                        // store url in realtime database

                        final DatabaseReference reference=database.getReference(); //returns the path to root



                        reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override

                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()) {

                                    Toast.makeText(ConsultDoctorActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();

                                } else

                                    Toast.makeText(ConsultDoctorActivity.this,"File Not Uploaded",Toast.LENGTH_SHORT).show();



                            }

                        });





                    }

                }).addOnFailureListener(new OnFailureListener() {

            @Override

            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ConsultDoctorActivity.this,"File Not Uploaded",Toast.LENGTH_SHORT).show();



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





    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Check req code is '9' or not



        if(requestCode==9 && grantResults[0]== PackageManager.PERMISSION_GRANTED )

        {

            selectPdf();

        }

        else if(requestCode==9 && grantResults[0]!= PackageManager.PERMISSION_GRANTED)

        { Toast.makeText(ConsultDoctorActivity.this,"please provide permission..",Toast.LENGTH_SHORT).show();}

        else if(requestCode == 2 && grantResults[0] != PackageManager.PERMISSION_GRANTED)



        {



            Log.d("HeartRate", "Permission Denied");



        }



        else if (requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED)



        {



            Log.d("HeartRate", "Permission Allowed");



            camPermission = true;



            Intent intent = new Intent(ConsultDoctorActivity.this, HeartRate.class);



            startActivity(intent);



        }

    }



    public void openTestWindow(View view){

        Intent intent = new Intent(this, ChooseDoctorActivity.class);

        startActivity(intent);

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



    public boolean isConnected(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }



    public void showDialog(String title, String message){

        CustomDialogBoxActivity dialog = new CustomDialogBoxActivity();

        dialog.setCustomTitle(title);

        dialog.setDialogText(message);

        dialog.show(getSupportFragmentManager(), title);

    }



    public boolean showConfirmRollbackDialog(String title, String message){

        ConfirmRollbackDialog dialog = new ConfirmRollbackDialog();

        dialog.setCustomTitle(title);

        dialog.setDialogText(message);

        dialog.setDataPacket(packet);

        dialog.show(getSupportFragmentManager(), title);

        return dialog.continueBack;

    }



    @Override

    public void onBackPressed() {

        super.onBackPressed();

        databaseReference.child(packetId).removeValue();

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



    //ChangePhotoDialog

    public void getBitmapImage(Bitmap bitmap) {

        //get the bitmap from changephotodialog

        if (bitmap != null){

            //compress the image if you like



            compressBitmap(bitmap,70);

            imageViewPatient.setImageBitmap(bitmap);



// uplaod the image taken from camera to firebase;

            //  uplaodImageFromCamera(bitmap);



        }

    }



    //uploadActivity

    public void getImagePath(Bitmap imagePath) {



        imageViewPatient.setImageBitmap(imagePath);



    }

//

//    ///////////////////

//    public static byte[] getBytesFromBitmap(Bitmap bm, int quality){

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();

//        bm.compress(Bitmap.CompressFormat.JPEG, quality, stream);

//        return stream.toByteArray();

//    }

////////////////////////



    //  protected void OnCreate(Bundle savedInstanceState){



    //    selectFile = (Button) findViewById(R.id.selectfile);

    //  selectFile.setOnClickListener((view)→{ openUploadDocument(); });

    //}



//    public void openUploadDocument(){

//

//        Intent intent = new Intent (this, UploadDocument.class);

//        startActivity(intent);

//    }



//    public void uplaodImageFromCamera(Bitmap bitmap){

//        //////////////////////////////

//        FirebaseUser user = auth.getCurrentUser();

//        String userID = user.getUid();

//

//

//        if (bitmap != null) {

//            final ProgressDialog progressDialog = new ProgressDialog(this);

//            progressDialog.setTitle("Uploading");

//            progressDialog.show();

//

//

//            byte[] bytes = getBytesFromBitmap(bitmap, 70);

//            StorageReference ref = mStorageRef.child("users/" + userID.toString() + "/media/image/" + ".jpg");

//            ref.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

//                @Override

//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                    progressDialog.dismiss();

//                    Toast.makeText(ConsultDoctorActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

//                }

//            })

//                    .addOnFailureListener(new OnFailureListener() {

//                        @Override

//                        public void onFailure(@NonNull Exception e) {

//                            progressDialog.dismiss();

//                            Toast.makeText(ConsultDoctorActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();

//                        }

//                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

//                @Override

//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

//                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

//                    progressDialog.setMessage("Uploaded " + (int) progress + "%");

//                }

//            });

//        }

//        ////////////////////////////////////

//    }

}