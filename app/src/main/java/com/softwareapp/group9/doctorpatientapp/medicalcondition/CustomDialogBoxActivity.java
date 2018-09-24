package com.softwareapp.group9.doctorpatientapp.medicalcondition;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.softwareapp.group9.doctorpatientapp.R;

public class CustomDialogBoxActivity extends AppCompatDialogFragment {

    private TextView customTxt;
    private String customMessage;
    private String customTitle;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_custom_dialog_box, null);
        builder.setView(view).setTitle(customTitle).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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


}
