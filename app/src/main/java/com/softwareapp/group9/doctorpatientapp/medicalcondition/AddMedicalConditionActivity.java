package com.softwareapp.group9.doctorpatientapp.medicalcondition;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softwareapp.group9.doctorpatientapp.R;

public class AddMedicalConditionActivity extends AppCompatActivity {

    private EditText addConditionTitleEt;
    private EditText addConidtionDescEt;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_medical_condition);
        addConditionTitleEt = (EditText) findViewById(R.id.addConditionTitleEt);
        addConidtionDescEt = (EditText) findViewById(R.id.addConditionDescEt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("MedicalConditions");
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
            String id = reference.push().getKey();
            MedicalCondition condition = new MedicalCondition(id, conditionTitle, conditionDescription);
            reference.child(id).setValue(condition);
            finish();
        }
    }

    public void showDialogBox(String message, String title) {
        CustomDialogBoxActivity customDialog = new CustomDialogBoxActivity();
        customDialog.setDialogText(message);
        customDialog.setCustomTitle(title);
        customDialog.show(getSupportFragmentManager(), title);
    }
}
