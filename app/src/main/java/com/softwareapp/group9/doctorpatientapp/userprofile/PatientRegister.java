package com.softwareapp.group9.doctorpatientapp.userprofile;

import android.app.ProgressDialog;
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
import com.softwareapp.group9.doctorpatientapp.userprofile.CustomDialogBoxActivity;

public class PatientRegister extends AppCompatActivity implements View.OnClickListener{

    private Button patientRegButton;
    private EditText regEmail;
    private EditText regPassword;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_register);
        mToolbar = (Toolbar) findViewById(R.id.appTb);
        setSupportActionBar(mToolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Patient Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);

        patientRegButton = (Button) findViewById(R.id.patientRegButton);
        regEmail = (EditText) findViewById(R.id.regEmail);
        regPassword = (EditText) findViewById(R.id.regPassword);

        patientRegButton.setOnClickListener(this);

    }

    private void registerPatient(){

        String email = regEmail.getText().toString().trim();
        String password = regPassword.getText().toString().trim();

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

        progressDialog.setMessage("Registering Patient...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                //user is successfully registered and logged in

                                //Toast.makeText(PatientRegister.this, "Registered Successfully", Toast.LENGTH_SHORT). show();
                                showBackDialog("Notice", "Registration Successful!");
                            }

                            else{
                                //Toast.makeText(PatientRegister.this, "Could not register", Toast.LENGTH_SHORT). show();
                                showDialog("Error", "Registration Failed! The account may already exist");
                            }
                        }

                });


    }

    @Override
    public void onClick (View view){
        if(view == patientRegButton){
            registerPatient();
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
}
