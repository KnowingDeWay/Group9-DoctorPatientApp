package com.softwareapp.group9.doctorpatientapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softwareapp.group9.doctorpatientapp.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.softwareapp.group9.doctorpatientapp.userprofile.BackDialogActivity;
import com.softwareapp.group9.doctorpatientapp.userprofile.CustomDialogBoxActivity;
import com.softwareapp.group9.doctorpatientapp.userprofile.DoctorBackDialogActivity;

public class DoctorRegister extends AppCompatActivity implements View.OnClickListener {

    private Button doctorRegButton;
    private EditText doctorRegEmail;
    private EditText doctorRegPassword;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);
        mToolbar = (Toolbar) findViewById(R.id.appTb);
        setSupportActionBar(mToolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Doctor Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);

        doctorRegButton = (Button) findViewById(R.id.doctorRegButton);
        doctorRegEmail = (EditText) findViewById(R.id.doctorRegEmail);
        doctorRegPassword = (EditText) findViewById(R.id.doctorRegPassword);

        doctorRegButton.setOnClickListener(this);
    }

    private void registerDoctor(){

        String email = doctorRegEmail.getText().toString().trim();
        String password = doctorRegPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stopping function from executing further
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stopping function from executing further
            return;
        }
        //If validation is ok
        //We will first show a progress bar

        progressDialog.setMessage("Registering Doctor...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            //user is successfully registered and logged in

                            //Toast.makeText(DoctorRegister.this, "Registered Successfully", Toast.LENGTH_SHORT). show();
                            showBackDialog("Notice", "Registration Successful!");
                        }

                        else{
                            //Toast.makeText(PatientRegister.this, "Could not register", Toast.LENGTH_SHORT). show();
                            if(!isConnected()){
                                showDialog("Error", "Please check Internet Connection!");
                            }
                            showDialog("Error", "Registration Failed! The account may already exist.");
                        }
                    }

                });





    }

    @Override
    public void onClick (View view){
        if(view == doctorRegButton){
            registerDoctor();
        }


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
        DoctorBackDialogActivity dialog = new DoctorBackDialogActivity();
        dialog.setCustomTitle(title);
        dialog.setDialogText(message);
        dialog.show(getSupportFragmentManager(), title);
    }





}
