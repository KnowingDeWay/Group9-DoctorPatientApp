package com.softwareapp.group9.doctorpatientapp.doctorviewpatient;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.softwareapp.group9.doctorpatientapp.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapterRecommendations";

    private ArrayList<String> mPatientNames;
    private ArrayList<String> mMedicalCondition;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mPatientNames, ArrayList<String> mMedicalCondition) {
        this.mPatientNames = mPatientNames;
        this.mMedicalCondition = mMedicalCondition;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doctor_view_patient_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Log.d(TAG, "onBindViewHolder: called.");

        viewHolder.patientName.setText(mPatientNames.get(i));
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + mPatientNames.get(i));
                Toast.makeText(mContext, mPatientNames.get(i), Toast.LENGTH_SHORT).show();

            }
        });
        viewHolder.medicalCondition.setText(mMedicalCondition.get(i));
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+ mMedicalCondition.get(i));
                Toast.makeText(mContext, mPatientNames.get(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ViewPatientDataPacketActivity.class);
                intent.putExtra("patient name", mPatientNames.get(i));
                intent.putExtra("medical condition", mMedicalCondition.get(i));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPatientNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView patientName;
        TextView medicalCondition;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.tvPatientNameInDoctorEnd);
            medicalCondition = itemView.findViewById(R.id.tvMedicalConditionInDoctorEnd);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
