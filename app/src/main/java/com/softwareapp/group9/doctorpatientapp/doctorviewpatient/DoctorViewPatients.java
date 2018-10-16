package com.softwareapp.group9.doctorpatientapp.doctorviewpatient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.softwareapp.group9.doctorpatientapp.LoginActivity;
import com.softwareapp.group9.doctorpatientapp.R;
import com.softwareapp.group9.doctorpatientapp.recommendationshistory.RecommendationsHistory;
import com.softwareapp.group9.doctorpatientapp.userprofile.DoctorProfileActivity;
import com.softwareapp.group9.doctorpatientapp.userprofile.LaunchScreen;

import java.util.ArrayList;

public class DoctorViewPatients extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView navigationView;
    private FirebaseAuth auth;
    private static final String TAG = "DoctorViewPatients";
    //variables
    private ArrayList<String> mPatientNames = new ArrayList<>();
    private ArrayList<String> mMedicalCondition = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_content_view_patient_home);
        mToolbar = (Toolbar) findViewById(R.id.doctorAppTb);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_doctor);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.doctorNv);
        navigationView.setNavigationItemSelectedListener(this);
        auth = FirebaseAuth.getInstance();
        setTitle("View Patients");

        Log.d(TAG, "onCreate: started view patients");
        initTest();
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
            case R.id.doctor_nav_profile: Intent intent3 = new Intent(this, DoctorProfileActivity.class); startActivity(intent3); break;
            case R.id.doctor_nav_recommendations_history: Intent intent4 = new Intent(this, RecommendationsHistory.class); startActivity(intent4); break;
            case R.id.doctor_nav_logout:
                Intent closingIntent = new Intent(getApplicationContext(), LaunchScreen.class);
                closingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                auth.signOut();
                startActivity(closingIntent);
                break;
        }
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout_doctor);
        layout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initTest(){
        Log.d(TAG, "initTest: preparing test strings for recycler view.");
        mPatientNames.add("John");
        mMedicalCondition.add("headache");
        mPatientNames.add("Sara");
        mMedicalCondition.add("stomache");
        mPatientNames.add("Mark");
        mMedicalCondition.add("vomiting");
        mPatientNames.add("Mike");
        mMedicalCondition.add("headache");
        mPatientNames.add("Andre");
        mMedicalCondition.add("eye");
        mPatientNames.add("Nameer");
        mMedicalCondition.add("arm"); mPatientNames.add("Wenrui");
        mMedicalCondition.add("leg"); mPatientNames.add("Kenny");
        mMedicalCondition.add("flu"); mPatientNames.add("Adib");
        mMedicalCondition.add("fever"); mPatientNames.add("Ross");
        mMedicalCondition.add("bone"); mPatientNames.add("Rachel");
        mMedicalCondition.add("foot"); mPatientNames.add("Monica");
        mMedicalCondition.add("fever"); mPatientNames.add("Angela");
        mMedicalCondition.add("flu"); mPatientNames.add("Ten");
        mMedicalCondition.add("headache"); mPatientNames.add("Rain");
        mMedicalCondition.add("arm");
        initRecyclerView();

    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.viewPatientRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mPatientNames, mMedicalCondition);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
