package com.softwareapp.group9.doctorpatientapp.medicalcondition;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.softwareapp.group9.doctorpatientapp.FirebaseSecurity.SecureEncrypter;
import com.softwareapp.group9.doctorpatientapp.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ViewMedicalConditionAdapter extends RecyclerView.Adapter<ViewMedicalConditionAdapter.ViewMedicalConditionViewHolder> {

    private Context context;
    private ArrayList<MedicalCondition> list;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userId;
    private DatabaseReference reference;
    private String referenceString;
    private SecureEncrypter encryptionManager;

    public ViewMedicalConditionAdapter(Context context, ArrayList<MedicalCondition> list){
        this.context = context;
        this.list = list;
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
        referenceString = "Users/Patients/" + userId;
        reference = database.getReference(referenceString);
        encryptionManager = SecureEncrypter.getInstance();
    }

    @Override
    public ViewMedicalConditionAdapter.ViewMedicalConditionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.recycler_view_medical_condition, null);
        return new ViewMedicalConditionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMedicalConditionViewHolder holder, int position) {
        MedicalCondition condition = list.get(position);
        holder.viewConditionName.setText(condition.getConditionTitle());
        holder.viewConditionDesc.setText(condition.getConditionDescription());
    }

    public void refresh(ArrayList<MedicalCondition> list){
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class ViewMedicalConditionViewHolder extends RecyclerView.ViewHolder {
        TextView viewConditionName, viewConditionDesc;
        Button deleteConditionBtn;

        public ViewMedicalConditionViewHolder(View view) {
            super(view);
            viewConditionName = (TextView) view.findViewById(R.id.conditionName);
            viewConditionDesc = (TextView) view.findViewById(R.id.conditionDesc);
            deleteConditionBtn = (Button) view.findViewById(R.id.deleteConditionBtn);
            deleteConditionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MedicalCondition condition = list.get(getAdapterPosition());
                    String encryptedId = encryptionManager.encryptData(condition.getConditionId());
                    Query query = reference.child("MedicalConditions").orderByChild("conditionId").equalTo(encryptedId);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                snapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });
                    list.remove(condition);
                    notifyDataSetChanged();
                }
            });
        }

    }

}
