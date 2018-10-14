package com.softwareapp.group9.doctorpatientapp.medicalcondition;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softwareapp.group9.doctorpatientapp.FirebaseSecurity.SecureEncrypter;
import com.softwareapp.group9.doctorpatientapp.R;
import com.softwareapp.group9.doctorpatientapp.userprofile.LaunchScreen;

import java.util.ArrayList;

public class AddMedicalConditionActivity extends AppCompatActivity {

    private EditText addConditionTitleEt;
    private EditText addConidtionDescEt;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userId;
    private String referenceString;
    private SecureEncrypter encrypter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_medical_condition);
        addConditionTitleEt = (EditText) findViewById(R.id.addConditionTitleEt);
        addConidtionDescEt = (EditText) findViewById(R.id.addConditionDescEt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LaunchScreen.class);
            startActivity(intent);
            showDialogBox("Error", "You are not logged in!");
        }
        userId = user.getUid();
        referenceString = "Users/Patients/" + userId + "/MedicalConditions";
        reference = database.getReference(referenceString);
        encrypter = SecureEncrypter.getInstance();
        setTitle("Add Medical Condition");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cancelAddConditionScreen(View view){
        finish();
    }

    public void addMedicalCondition(View view) {
        String conditionTitle = addConditionTitleEt.getText().toString();
        String conditionDescription = addConidtionDescEt.getText().toString();
        if(TextUtils.isEmpty(conditionTitle) || TextUtils.isEmpty(conditionDescription)){
            if(TextUtils.isEmpty(conditionTitle)){
                showDialogBox("Please enter title of medical condition", "Error");
            } else {
                showDialogBox("Please enter description of medical condition", "Error");
            }
        } else {
            if(isConnected()){
                try{
                    String id = reference.push().getKey();
                    ArrayList<String> stringsToEncrypt = new ArrayList<>();
                    stringsToEncrypt.add(id);
                    stringsToEncrypt.add(conditionTitle);
                    stringsToEncrypt.add(conditionDescription);
                    ArrayList<String> encryptedStrings = encrypter.getEncryptedStrings(stringsToEncrypt);
                    MedicalCondition condition = new MedicalCondition(encryptedStrings.get(0), encryptedStrings.get(1), encryptedStrings.get(2));
                    reference.child(id).setValue(condition);
                    finish();
                } catch(Exception e){
                    showRetryDialogBox("Unable to add medical condition!", "Error");
                }
            } else {
                showRetryDialogBox("No Internet Connection Detected!", "Error");
            }
        }
    }

    public void showDialogBox(String message, String title) {
        CustomDialogBoxActivity customDialog = new CustomDialogBoxActivity();
        customDialog.setDialogText(message);
        customDialog.setCustomTitle(title);
        customDialog.show(getSupportFragmentManager(), title);
    }

    public void showRetryDialogBox(String message, String title) {
        FinishCustomDialogBox customDialog = new FinishCustomDialogBox();
        customDialog.setDialogText(message);
        customDialog.setCustomTitle(title);
        customDialog.setTiedActivity("AddMedicalCondition");
        customDialog.show(getSupportFragmentManager(), title);
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
