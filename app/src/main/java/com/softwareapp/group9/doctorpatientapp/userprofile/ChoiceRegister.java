package com.softwareapp.group9.doctorpatientapp.userprofile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.View;
import android.widget.Button;

import com.softwareapp.group9.doctorpatientapp.DoctorRegister;
import com.softwareapp.group9.doctorpatientapp.R;

public class ChoiceRegister extends AppCompatActivity {
    private Button button4;
    private Button button5;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_register);
        mToolbar = (Toolbar)findViewById(R.id.appTb);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Studio 1B");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button4= (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPatientRegister();
            }
        });


        button5= (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDoctorRegister();
            }
        });



    }

    public void openPatientRegister(){

        Intent intent = new Intent(this, PatientRegister.class);
        startActivity(intent);
    }

    public void openDoctorRegister(){

        Intent intent = new Intent(this, DoctorRegister.class);
        startActivity(intent);
    }

}
