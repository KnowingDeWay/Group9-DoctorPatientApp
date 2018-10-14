package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.softwareapp.group9.doctorpatientapp.DoctorInformation;
import com.softwareapp.group9.doctorpatientapp.R;

import java.util.ArrayList;

public class ChooseDoctorAdapter extends RecyclerView.Adapter<ChooseDoctorAdapter.ChooseDoctorViewHolder> {

    private Context context;
    private ArrayList<DoctorInformation> doctorList;
    private DataPacket packet;
    private String packetId;
    private String userId;
    private String referenceString;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

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
        CardView resultCard;

        public ChooseDoctorViewHolder(View view){
            super(view);
            doctorName = (TextView)view.findViewById(R.id.doctorName);
            doctorDepartment = (TextView)view.findViewById(R.id.doctorDepartment);
            resultCard = (CardView)view.findViewById(R.id.resultCard);
            resultCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendPacket(getAdapterPosition());
                }
            });
        }

        public void sendPacket(int adapterPosition){
            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            DoctorInformation information = doctorList.get(adapterPosition);
            ConfirmDoctorDialog dialog = new ConfirmDoctorDialog();
            dialog.setCustomTitle("Notice");
            dialog.setDialogText("Are you sure you want to send your details to " + information.docFullName + "?");
            dialog.setDataPacket(packet);
            dialog.setDoctorId(information.docId);
            dialog.show(manager, "Notice");
        }
    }

    public void setSearchOperation(ArrayList<DoctorInformation> list) {
        this.doctorList = new ArrayList<>();
        this.doctorList.addAll(list);
        notifyDataSetChanged();
    }

    public void setAdapterPacket(DataPacket packet){
        this.packet = packet;
    }
}
