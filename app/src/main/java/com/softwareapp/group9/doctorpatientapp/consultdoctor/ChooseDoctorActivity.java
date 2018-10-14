package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.softwareapp.group9.doctorpatientapp.DoctorInformation;
import com.softwareapp.group9.doctorpatientapp.R;
import com.softwareapp.group9.doctorpatientapp.userprofile.CustomDialogBoxActivity;

import java.util.ArrayList;

public class ChooseDoctorActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ChooseDoctorAdapter adapter;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private String referenceString;
    private ArrayList<DoctorInformation> list;
    private ProgressDialog dialog;
    private String packetId;
    private DataPacket packet;
    private DatabaseReference databaseReference;
    private String packetReferenceString;
    private FirebaseAuth auth;
    private String userId;
    private boolean packetReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_doctor);
        searchView = (SearchView) findViewById(R.id.chooseDoctorSearchView);
        database = FirebaseDatabase.getInstance();
        referenceString = "Users/Doctors";
        reference = database.getReference(referenceString);
        list = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Searching...");
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        packetReferenceString = "Users/Patients/" + userId + "/Packets";
        databaseReference = database.getReference(packetReferenceString);
        packetId = getIntent().getStringExtra("packetId");
        packetReceived = false;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.show();
                if(isConnected()){
                    Query dbQuery = reference.orderByChild("docFullName").startAt(query).endAt(query + "\uf8ff");
                    dbQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list.clear();
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                DoctorInformation information = snapshot.getValue(DoctorInformation.class);
                                list.add(information);
                                adapter.setSearchOperation(list);
                            }
                            if(list.size() == 0){
                                dialog.dismiss();
                                list.clear();
                                adapter.setSearchOperation(list);
                                showDialog("Notice", "No search results found!");
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            dialog.dismiss();
                            showDialog("Error", "Unable to connect to database!");
                        }
                    });
                    return true;
                } else {
                    dialog.dismiss();
                    showDialog("Error", "Unable to connect! there is no internet connection!");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    initialiseData();
                }
                return true;
            }
        });
        initialiseData();
        recyclerView = (RecyclerView) findViewById(R.id.doctorResults);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChooseDoctorAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    public void getPacket(){
        Query query = databaseReference.orderByKey().equalTo(packetId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    packet = snapshot.getValue(DataPacket.class);
                }
                adapter.setAdapterPacket(packet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void initialiseData(){
        Query query = reference.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DoctorInformation information = snapshot.getValue(DoctorInformation.class);
                    list.add(information);
                    getPacket();
                    adapter.setSearchOperation(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void showDialog(String title, String message){
        CustomDialogBoxActivity dialog = new CustomDialogBoxActivity();
        dialog.setCustomTitle(title);
        dialog.setDialogText(message);
        dialog.show(getSupportFragmentManager(), title);
    }
}
