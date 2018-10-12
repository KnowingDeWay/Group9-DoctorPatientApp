package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softwareapp.group9.doctorpatientapp.DoctorInformation;
import com.softwareapp.group9.doctorpatientapp.R;

import java.util.ArrayList;

public class ChooseDoctorAdapter extends RecyclerView.Adapter<ChooseDoctorAdapter.ChooseDoctorViewHolder> {

    private Context context;
    private ArrayList<DoctorInformation> doctorList;

    public ChooseDoctorAdapter(Context context, ArrayList<DoctorInformation> doctorList){
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public ChooseDoctorAdapter.ChooseDoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.recycler_choose_doctor, null);
        return new ChooseDoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseDoctorAdapter.ChooseDoctorViewHolder holder, int position) {
        DoctorInformation information = doctorList.get(position);
        String name = information.docOtherName + ", " + information.docSurname;
        holder.doctorName.setText(name);
        holder.doctorDepartment.setText(information.docDepartment);
    }

    @Override
    public int getItemCount() {
        return this.doctorList.size();
    }

    class ChooseDoctorViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName, doctorDepartment;

        public ChooseDoctorViewHolder(View view){
            super(view);
            doctorName = (TextView)view.findViewById(R.id.doctorName);
            doctorDepartment = (TextView)view.findViewById(R.id.doctorDepartment);
        }
    }

    public void setSearchOperation(ArrayList<DoctorInformation> list) {
        this.doctorList = new ArrayList<>();
        this.doctorList.addAll(list);
        notifyDataSetChanged();
    }
}
