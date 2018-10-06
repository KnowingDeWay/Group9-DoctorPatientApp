package com.softwareapp.group9.doctorpatientapp.userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softwareapp.group9.doctorpatientapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softwareapp.group9.doctorpatientapp.userprofile.CustomDialogBoxActivity;

public class PatientDetails extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;



    private DatabaseReference databaseReference;

    private EditText regSurname, regOtherName, regGender, regAge, regHeight, regWeight, regAddress;
    private Button logoutButton, patientSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, PatientLogin.class));
        }

        databaseReference= FirebaseDatabase.getInstance().getReference("Users/Patients");

        regSurname = (EditText) findViewById(R.id.regSurname);
        regOtherName = (EditText) findViewById(R.id.regOtherName);
        regGender = (EditText) findViewById(R.id.regGender);
        regAge = (EditText) findViewById(R.id.regAge);
        regHeight = (EditText) findViewById(R.id.regHeight);
        regWeight = (EditText) findViewById(R.id.regWeight);
        regAddress = (EditText) findViewById(R.id.regAddress);
        patientSaveButton = (Button) findViewById(R.id.patientSaveButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        logoutButton.setOnClickListener(this);
        patientSaveButton.setOnClickListener(this);
    }

    private void savePatientInformation(){

        String surname = regSurname.getText().toString().trim();
        String otherName = regOtherName.getText().toString().trim();
        String gender = regGender.getText().toString().trim();
        String age = regAge.getText().toString().trim();
        String height = regHeight.getText().toString().trim();
        String weight = regWeight.getText().toString().trim();
        String address = regAddress.getText().toString().trim();

        if(TextUtils.isEmpty(surname) || TextUtils.isEmpty(otherName) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(age) || TextUtils.isEmpty(height)
                || TextUtils.isEmpty(weight) || TextUtils.isEmpty(address)){
            showDialog("Error", "Please fill out all fields!");
        } else {
            PatientInformation patientInformation = new PatientInformation(surname,otherName,gender,age,height,weight,address);

            FirebaseUser user = firebaseAuth.getCurrentUser();
            patientInformation.id = user.getUid();
            databaseReference.child(user.getUid()).setValue(patientInformation);

            //Toast.makeText(this, "Patient details saved...", Toast.LENGTH_LONG).show();
            showContinueDialog("Notice", "Patient Details Saved!");
            finish();
        }
    }

    @Override
    public void onClick(View view) {

        if(view == logoutButton){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent( this, PatientLogin.class));
        }

        if(view == patientSaveButton){

            savePatientInformation();
        }


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

    public void showContinueDialog(String title, String message){
        ContinueToProfileDialogBox dialog = new ContinueToProfileDialogBox();
        dialog.setCustomTitle(title);
        dialog.setDialogText(message);
        dialog.show(getSupportFragmentManager(), title);
    }
}
