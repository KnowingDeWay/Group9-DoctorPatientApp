package com.softwareapp.group9.doctorpatientapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softwareapp.group9.doctorpatientapp.consultdoctor.BackDialogActivity;
import com.softwareapp.group9.doctorpatientapp.userprofile.ContinueToProfileDialogBox;
import com.softwareapp.group9.doctorpatientapp.userprofile.CustomDialogBoxActivity;


public class DoctorDetails extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;


    private DatabaseReference databaseReference;

    private EditText regDocSurname, regDocOtherName, regDocDepartment;
    private Button logoutButton, doctorSaveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, DoctorLogin.class));
        }

        databaseReference= FirebaseDatabase.getInstance().getReference("Users/Doctors");

        regDocSurname = (EditText) findViewById(R.id.regDocSurname);
        regDocOtherName = (EditText) findViewById(R.id.regDocOtherName);
        regDocDepartment = (EditText) findViewById(R.id.regDocDepartment);
        logoutButton = (Button) findViewById(R.id.doctorLogoutButton);
        doctorSaveButton = (Button) findViewById(R.id.doctorSaveButton);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        logoutButton.setOnClickListener(this);
        doctorSaveButton.setOnClickListener(this);
    }

    private void saveDoctorInformation(){

        String surname = regDocSurname.getText().toString().trim();
        String otherName = regDocOtherName.getText().toString().trim();
        String department = regDocDepartment.getText().toString().trim();


        if(TextUtils.isEmpty(surname) || TextUtils.isEmpty(otherName) || TextUtils.isEmpty(department)){
            showDialog("Error", "Please fill out all fields!");
        } else {
            DoctorInformation doctorInformation = new DoctorInformation(surname, otherName, department);

            FirebaseUser user = firebaseAuth.getCurrentUser();
            doctorInformation.docId = user.getUid();
            databaseReference.child(user.getUid()).setValue(doctorInformation);

            //Toast.makeText(this, "Patient details saved...", Toast.LENGTH_LONG).show();
            showContinueDialog("Notice", "Doctor Details Saved!");
            //finish();
        }




    }

    @Override
    public void onClick(View view) {

        if(view == logoutButton){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent( this,DoctorLogin.class));
        }

        if(view == doctorSaveButton){

            saveDoctorInformation();
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