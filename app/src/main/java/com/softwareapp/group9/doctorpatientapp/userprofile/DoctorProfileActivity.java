package com.softwareapp.group9.doctorpatientapp.userprofile;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.softwareapp.group9.doctorpatientapp.R;

public class DoctorProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_doctor_profile_home);
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        setTitle("Doctor Profile");
    }
}
