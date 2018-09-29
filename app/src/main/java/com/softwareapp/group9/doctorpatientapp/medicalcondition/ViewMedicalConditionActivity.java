package com.softwareapp.group9.doctorpatientapp.medicalcondition;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softwareapp.group9.doctorpatientapp.FirebaseSecurity.SecureEncrypter;
import com.softwareapp.group9.doctorpatientapp.LoginActivity;
import com.softwareapp.group9.doctorpatientapp.R;
import com.softwareapp.group9.doctorpatientapp.consultdoctor.ConsultDoctorActivity;
import com.softwareapp.group9.doctorpatientapp.doctorfeedback.DoctorFeedbackActivity;
import com.softwareapp.group9.doctorpatientapp.facilitiesnearme.FacilitiesNearMeActivity;
import com.softwareapp.group9.doctorpatientapp.userprofile.PatientProfileActivity;

import java.util.ArrayList;

public class ViewMedicalConditionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<MedicalCondition> list;
    private RecyclerView recyclerView;
    private ViewMedicalConditionAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView navigationView;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseOptions options;
    private SecureEncrypter encryptionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_patient_view_medical_condition_home);
        mToolbar = (Toolbar) findViewById(R.id.appTb);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_patient);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        readDatabase();
        recyclerView = (RecyclerView) findViewById(R.id.conditionRv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ViewMedicalConditionAdapter(this, list);
        recyclerView.setAdapter(adapter);
        navigationView = (NavigationView) findViewById(R.id.patientNv);
        navigationView.setNavigationItemSelectedListener(this);
        encryptionManager = SecureEncrypter.getInstance();
        System.out.println("List size is :" + list.size());
        setTitle("View Medical Conditions");
    }

    @Override
    protected void onStart(){
        super.onStart();
        readDatabase();
        recyclerView = (RecyclerView) findViewById(R.id.conditionRv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ViewMedicalConditionAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    public void readDatabase(){
        DatabaseReference reference = database.getReference("MedicalConditions");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MedicalCondition conditionToDecrypt = snapshot.getValue(MedicalCondition.class);
                    String id = conditionToDecrypt.getConditionId();
                    String conditionName = conditionToDecrypt.getConditionTitle();
                    String conditionDescription = conditionToDecrypt.getConditionDescription();
                    ArrayList<String> stringsToDescrypt = new ArrayList<>();
                    stringsToDescrypt.add(id);
                    stringsToDescrypt.add(conditionName);
                    stringsToDescrypt.add(conditionDescription);
                    ArrayList<String> decryptedStrings = encryptionManager.getDecryptedStrings(stringsToDescrypt);
                    MedicalCondition condition = new MedicalCondition(decryptedStrings.get(0), decryptedStrings.get(1), decryptedStrings.get(2));
                    list.add(condition);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    public void loadAddConditionScreen(View view){
        Intent intent = new Intent(ViewMedicalConditionActivity.this, AddMedicalConditionActivity.class);
        startActivity(intent);
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
            case R.id.nav_profile: Intent intent = new Intent(this, PatientProfileActivity.class); startActivity(intent); break;
            case R.id.nav_consult: Intent intent3 = new Intent(this, ConsultDoctorActivity.class); startActivity(intent3); break;
            case R.id.nav_facilities: Intent intent4 = new Intent(this, FacilitiesNearMeActivity.class); startActivity(intent4); break;
            case R.id.nav_feedback: Intent intent5 = new Intent(this, DoctorFeedbackActivity.class); startActivity(intent5); break;
            case R.id.nav_logout: Intent closingIntent = new Intent(getApplicationContext(), LoginActivity.class); closingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); startActivity(closingIntent); break;
        }
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout_patient);
        layout.closeDrawer(GravityCompat.START);
        return true;
    }
}
