package com.softwareapp.group9.doctorpatientapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.softwareapp.group9.doctorpatientapp.medicalcondition.ViewMedicalConditionActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_joint_login_home);
        setTitle("User Login");
    }

    public void accessRestOfApp(View view){
        Intent intent = new Intent(this, ViewMedicalConditionActivity.class);
        startActivity(intent);
    }
}
