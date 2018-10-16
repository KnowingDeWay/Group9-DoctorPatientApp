package com.softwareapp.group9.doctorpatientapp.doctorviewpatient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.softwareapp.group9.doctorpatientapp.LoginActivity;
import com.softwareapp.group9.doctorpatientapp.R;
import com.softwareapp.group9.doctorpatientapp.recommendationshistory.RecommendationsHistory;
import com.softwareapp.group9.doctorpatientapp.userprofile.DoctorProfileActivity;

public class ViewPatientDataPacketActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView navigationView;

    private static final String TAG = "ViewPatientDataPacketActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_view_patients_datapacket_after_card_view);
        mToolbar = (Toolbar) findViewById(R.id.doctorAppTb);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_doctor);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.doctorNv);
        navigationView.setNavigationItemSelectedListener(this);
        setTitle("View Patients data packet");
        Button btnrec = findViewById(R.id.btnRecommendations);
        btnrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewPatientDataPacketActivity.this, "Recommendations Uploaded", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(TAG, "onCreate: started view patients data packet activity");
        getIncomingIntent();
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
            case R.id.doctor_nav_logout: Intent closingIntent = new Intent(getApplicationContext(), LoginActivity.class); closingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); startActivity(closingIntent); break;
        }
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout_doctor);
        layout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if (getIntent().hasExtra("patient name") && getIntent().hasExtra("medical condition")){
            Log.d(TAG, "getIncomingIntent: found intent extras.");
            String patientName= getIntent().getStringExtra("patient name");
            String medicalCondition = getIntent().getStringExtra("medical condition");
            setPatientName(patientName);
        }
    }
    private void setPatientName(String patientName){
        Log.d(TAG, "setPatientName: setting to image and name to widgets");
        TextView patName= (TextView) findViewById(R.id.tvDoctorVIewPatientsName);
        patName.setText(patientName);
    }

}
