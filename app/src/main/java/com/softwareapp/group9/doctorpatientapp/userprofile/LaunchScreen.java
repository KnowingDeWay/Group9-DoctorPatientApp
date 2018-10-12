package com.softwareapp.group9.doctorpatientapp.userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.softwareapp.group9.doctorpatientapp.DoctorLogin;
import com.softwareapp.group9.doctorpatientapp.R;

public class LaunchScreen extends AppCompatActivity {

    private Button button3;
    private Button choicePatientButton;
    private Button choiceDoctorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);

        getSupportActionBar().setTitle("Studio1B");



        button3= (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChoiceRegister();
            }
        });

        choicePatientButton= (Button) findViewById(R.id.choicePatientButton);
        choicePatientButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openPatientLogin();
            }

        });

        choiceDoctorButton= (Button) findViewById(R.id.choiceDoctorButton);
        choiceDoctorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openDoctorLogin();
            }

        });
    }

    public void openChoiceRegister(){

        Intent intent = new Intent(this, ChoiceRegister.class);
        startActivity(intent);
    }

    public void openPatientLogin(){

        Intent intent = new Intent(this, PatientLogin.class);
        startActivity(intent);
    }

    public void openDoctorLogin(){

        Intent intent = new Intent(this, DoctorLogin.class);
        startActivity(intent);
    }
}
