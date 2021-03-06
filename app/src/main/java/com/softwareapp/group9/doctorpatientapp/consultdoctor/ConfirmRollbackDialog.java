package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softwareapp.group9.doctorpatientapp.R;
import com.softwareapp.group9.doctorpatientapp.userprofile.CustomDialogBoxActivity;

public class ConfirmRollbackDialog extends AppCompatDialogFragment {
    private TextView customTxt;
    private String customMessage;
    private String customTitle;
    private DataPacket packet;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    public boolean continueBack;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_custom_dialog_box, null);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        String referenceString = "Users/Patients/" + userId + "/Packets";
        reference = database.getReference(referenceString);
        continueBack = false;
        builder.setView(view).setTitle(customTitle).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reference.child(packet.packetId).removeValue();
                continueBack = true;
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        customTxt = (TextView) view.findViewById(R.id.customTxt);
        customTxt.setText(customMessage);

        return builder.create();
    }

    public void setDialogText(String text) {
        customMessage = text;
    }

    public void setCustomTitle(String text) { customTitle = text; }

    public void setDataPacket(DataPacket packet){
        this.packet = packet;
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void showDialog(String title, String message){
        CustomDialogBoxActivity dialog = new CustomDialogBoxActivity();
        dialog.setCustomTitle(title);
        dialog.setDialogText(message);
        dialog.show(getActivity().getSupportFragmentManager(), title);
    }
}
