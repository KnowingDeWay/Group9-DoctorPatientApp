package com.softwareapp.group9.doctorpatientapp.userprofile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.softwareapp.group9.doctorpatientapp.R;
import com.softwareapp.group9.doctorpatientapp.consultdoctor.ConsultDoctorActivity;
import com.softwareapp.group9.doctorpatientapp.doctorfeedback.DoctorFeedbackActivity;
import com.softwareapp.group9.doctorpatientapp.facilitiesnearme.FacilitiesNearMeActivity;
import com.softwareapp.group9.doctorpatientapp.medicalcondition.ViewMedicalConditionActivity;

public class PatientProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView navigationView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private EditText profileSurnameEt;
    private EditText profileOtherNameEt;
    private EditText profileGenderEt;
    private EditText profileAgeEt;
    private EditText profileHeightEt;
    private EditText profileWeightEt;
    private EditText profileAddressEt;
    private String userId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_patient_profile_home);
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
        if(user == null){
            finish();
            startActivity(new Intent(this, PatientLogin.class));
        }
        userId = user.getUid();
        profileSurnameEt = (EditText)findViewById(R.id.profileSurnameEt);
        profileOtherNameEt = (EditText) findViewById(R.id.profileOtherNameEt);
        profileGenderEt = (EditText) findViewById(R.id.profileGenderEt);
        profileAgeEt = (EditText) findViewById(R.id.profileAgeEt);
        profileHeightEt = (EditText) findViewById(R.id.profileHeightEt);
        profileWeightEt = (EditText) findViewById(R.id.profileWeightEt);
        profileAddressEt = (EditText) findViewById(R.id.profileAddressEt);
        database = FirebaseDatabase.getInstance();
        retreiveUserDetails();
        setTitle("Patient Profile");
    }

    public void updateUserProfile(View view){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating User Profile...");
        progressDialog.show();
        final DatabaseReference reference = database.getReference("Users/Patients");
        final String surname = profileSurnameEt.getText().toString().trim();
        final String otherName = profileOtherNameEt.getText().toString().trim();
        final String gender = profileGenderEt.getText().toString().trim();
        final String age = profileAgeEt.getText().toString().trim();
        final String height = profileHeightEt.getText().toString().trim();
        final String weight = profileWeightEt.getText().toString().trim();
        final String address = profileAddressEt.getText().toString().trim();
        if(isConnected()){
            Query query = reference.orderByKey().equalTo(userId).limitToFirst(1);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    PatientInformation currentInformation = null;
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        currentInformation = snapshot.getValue(PatientInformation.class);
                    }
                    PatientInformation information = new PatientInformation(userId, surname, otherName, gender, age, height, weight, address);
                    if(currentInformation != null){
                        if(currentInformation.equals(information)){
                            progressDialog.dismiss();
                            showDialog("Error", "No change was made to data!");
                        } else {
                            reference.child(userId).setValue(information);
                            progressDialog.dismiss();
                            showDialog("Notice", "Profile Updated!");
                        }
                    } else {
                        progressDialog.dismiss();
                        if(user != null){
                            auth.signOut();
                        }
                        showDialog("Error", "Unknown error has occurred!");
                        Intent intent = new Intent(getApplicationContext(), PatientLogin.class);
                        finish();
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    showDialog("Error", "Unable to connect to the database!");
                }
            });
        } else {
            progressDialog.dismiss();
            showDialog("Error", "Unable to connect to the internet!");
        }
    }

    public void retreiveUserDetails(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading User Profile...");
        progressDialog.show();
        DatabaseReference reference = database.getReference("Users/Patients");
        Query query = reference.orderByKey().equalTo(userId).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    PatientInformation information = snapshot.getValue(PatientInformation.class);
                    profileSurnameEt.setText(information.surname);
                    profileOtherNameEt.setText(information.otherName);
                    profileGenderEt.setText(information.gender);
                    profileAgeEt.setText(information.age);
                    profileHeightEt.setText(information.height);
                    profileWeightEt.setText(information.weight);
                    profileAddressEt.setText(information.address);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                showDialog("Error", "Unable to connect to database!");
            }
        });
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

    public void showBackDialog(String title, String message){
        BackDialogActivity dialog = new BackDialogActivity();
        dialog.setCustomTitle(title);
        dialog.setDialogText(message);
        dialog.show(getSupportFragmentManager(), title);
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
            case R.id.nav_condition: Intent intent2 = new Intent(this, ViewMedicalConditionActivity.class); startActivity(intent2); break;
            case R.id.nav_consult: Intent intent3 = new Intent(this, ConsultDoctorActivity.class); startActivity(intent3); break;
            case R.id.nav_facilities: Intent intent4 = new Intent(this, FacilitiesNearMeActivity.class); startActivity(intent4); break;
            case R.id.nav_feedback: Intent intent5 = new Intent(this, DoctorFeedbackActivity.class); startActivity(intent5); break;
            case R.id.nav_logout:
                Intent closingIntent = new Intent(getApplicationContext(), LaunchScreen.class);
                closingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                auth.signOut();
                startActivity(closingIntent);
                break;
        }
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout_patient);
        layout.closeDrawer(GravityCompat.START);
        return true;
    }
}
