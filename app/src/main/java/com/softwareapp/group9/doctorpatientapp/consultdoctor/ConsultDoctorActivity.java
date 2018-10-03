package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.softwareapp.group9.doctorpatientapp.R;
import com.softwareapp.group9.doctorpatientapp.doctorfeedback.DoctorFeedbackActivity;
import com.softwareapp.group9.doctorpatientapp.facilitiesnearme.FacilitiesNearMeActivity;
import com.softwareapp.group9.doctorpatientapp.medicalcondition.ViewMedicalConditionActivity;
import com.softwareapp.group9.doctorpatientapp.userprofile.PatientProfileActivity;

import java.io.ByteArrayOutputStream;

public class ConsultDoctorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_patient_consult_doctor_home);
        mToolbar = (Toolbar) findViewById(R.id.appTb);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_patient);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.patientNv);
        navigationView.setNavigationItemSelectedListener(this);
        setTitle("Consult Doctor");


        final Button uploadImage = (Button) findViewById(R.id.button4);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePhotoDialog dialog = new ChangePhotoDialog();
                dialog.show(getFragmentManager(), getString(R.string.change_photo_dialog));
//                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
//                startActivity(intent);
            }
        });
    }

    /**
     * Compress a bitmap by the @param "quality"
     * Quality can be anywhere from 1 -100 : 100 being the highest quality.
     * @param bitmap
     * @param quality
     * @return
     */
    public Bitmap compressBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return bitmap;
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
            case R.id.nav_profile: Intent intent = new Intent(this, PatientProfileActivity.class); startActivity(intent); finish(); break;
            case R.id.nav_condition: Intent intent2 = new Intent(this, ViewMedicalConditionActivity.class); startActivity(intent2); finish(); break;
            case R.id.nav_facilities: Intent intent4 = new Intent(this, FacilitiesNearMeActivity.class); startActivity(intent4); finish(); break;
            case R.id.nav_feedback: Intent intent5 = new Intent(this, DoctorFeedbackActivity.class); startActivity(intent5); finish(); break;
            case R.id.nav_logout: System.exit(0);
        }
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout_patient);
        layout.closeDrawer(GravityCompat.START);
        return true;
    }
}
