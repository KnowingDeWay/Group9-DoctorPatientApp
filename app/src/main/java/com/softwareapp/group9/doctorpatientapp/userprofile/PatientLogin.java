package com.softwareapp.group9.doctorpatientapp.userprofile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.softwareapp.group9.doctorpatientapp.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PatientLogin extends AppCompatActivity implements View.OnClickListener {

    private Button patientLoginButton;
    private EditText patientLoginEmail;
    private EditText patientLoginPassword;
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;
    private boolean flag; //True -> goes to next screen, False -> Details Screen

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);
        mToolbar = (Toolbar)findViewById(R.id.appTb);
        setSupportActionBar(mToolbar);
        flag = false;
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){

            //profile activity here
            //Simplified coding, part 2, around 11 minutes
            finish();
            startActivity(new Intent(getApplicationContext(),PatientDetails.class));
        }

        patientLoginEmail = (EditText) findViewById(R.id.patientLoginEmail);
        patientLoginPassword = (EditText) findViewById(R.id.patientLoginPassword);
        patientLoginButton = (Button) findViewById(R.id.patientLoginButton);

        patientLoginButton.setOnClickListener(this);



        progressDialog = new ProgressDialog(this);


        getSupportActionBar().setTitle("Patient Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void patientLogin(){

        String email = patientLoginEmail.getText().toString().trim();
        String password = patientLoginPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            //Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            showDialog("Error", "Please enter email!");
            //stopping function from executing further
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            //Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            showDialog("Error", "Please enter password!");
            //stopping function from executing further
            return;
        }

        progressDialog.setMessage("Logging in");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                           //start the profile activity
                            //finish();
                            FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = currUser.getUid();
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users/Patients");
                            Query query = reference.orderByChild("id").equalTo(userId).limitToFirst(1);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        flag = true;
                                    }
                                    if(flag){
                                        Intent intent = new Intent(getApplicationContext(), PatientProfileActivity.class);
                                        startActivity(intent);
                                    } else{
                                        startActivity(new Intent(getApplicationContext(),PatientDetails.class));
                                    }
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    flag = false;
                                    showDialog("Error", "There was an error connecting to the database! Please try again later");
                                }
                            });
                        } else {
                            if(!isConnected()){
                                showDialog("Error", "Unable to login! You are not connected!");
                            } else {
                                showDialog("Error", "Unable to login! Please check your credentials and try again");
                            }
                            progressDialog.dismiss();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view){
        if(view == patientLoginButton){
            patientLogin();

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
}