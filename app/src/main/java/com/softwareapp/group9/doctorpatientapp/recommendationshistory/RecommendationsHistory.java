package com.softwareapp.group9.doctorpatientapp.recommendationshistory;

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

import com.softwareapp.group9.doctorpatientapp.LoginActivity;
import com.softwareapp.group9.doctorpatientapp.R;
import com.softwareapp.group9.doctorpatientapp.doctorviewpatient.DoctorViewPatients;
import com.softwareapp.group9.doctorpatientapp.doctorviewpatient.RecyclerViewAdapter;
import com.softwareapp.group9.doctorpatientapp.userprofile.DoctorProfileActivity;

import java.util.ArrayList;

public class RecommendationsHistory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView navigationView;
    //variables
    private ArrayList<String> mPatientNames = new ArrayList<>();
    private ArrayList<String> mRecommendations = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_content_recommendations_history_home);
        mToolbar = (Toolbar) findViewById(R.id.doctorAppTb);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_doctor);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.doctorNv);
        navigationView.setNavigationItemSelectedListener(this);
        setTitle("Recommendations History");

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
            case R.id.doctor_nav_view_patients: Intent intent4 = new Intent(this, DoctorViewPatients.class); startActivity(intent4); break;
            case R.id.doctor_nav_logout: Intent closingIntent = new Intent(getApplicationContext(), LoginActivity.class); closingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); startActivity(closingIntent); break;
        }
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout_doctor);
        layout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void initTest(){

        mPatientNames.add("wefsd");
        mRecommendations.add("Recom1");
        mPatientNames.add("qawesdxz");
        mRecommendations.add("Recom2");
        mPatientNames.add("esvdfgh");
        mRecommendations.add("Recom3");
        mPatientNames.add("dvfrtgh");
        mRecommendations.add("Recom4");

        mPatientNames.add("werthyujy");
        mRecommendations.add("Recom5");
        initRecyclerView();

    }
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recommendationHistoryRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mPatientNames, mRecommendations);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}