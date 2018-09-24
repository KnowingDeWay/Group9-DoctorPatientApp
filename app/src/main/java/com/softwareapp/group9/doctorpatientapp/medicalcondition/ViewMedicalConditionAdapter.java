package com.softwareapp.group9.doctorpatientapp.medicalcondition;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softwareapp.group9.doctorpatientapp.R;

import java.util.ArrayList;

public class ViewMedicalConditionAdapter extends RecyclerView.Adapter<ViewMedicalConditionAdapter.ViewMedicalConditionViewHolder> {

    private Context context;
    private ArrayList<MedicalCondition> list;
    private FirebaseDatabase database;

    public ViewMedicalConditionAdapter(Context context, ArrayList<MedicalCondition> list){
        this.context = context;
        this.list = list;
        database = FirebaseDatabase.getInstance();
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
                    DatabaseReference reference = database.getReference("MedicalConditions").orderByKey().equalTo(list.get(getAdapterPosition()).getConditionId()).getRef();
                    reference.push();
                    reference.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                        }
                    });
                    list.remove(list.get(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });
        }

    }

}
