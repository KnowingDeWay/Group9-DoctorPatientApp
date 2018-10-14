package com.softwareapp.group9.doctorpatientapp.medicalcondition;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softwareapp.group9.doctorpatientapp.R;

public class LoadingDataActivity extends AppCompatDialogFragment {

    private TextView customTxt;
    private String customMessage;
    private String customTitle;
    private ProgressBar spinner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_load_data_activity, null);
        builder.setView(view).setTitle(customTitle);
        spinner = (ProgressBar)view.findViewById(R.id.dataLoadPb);
        spinner.setVisibility(View.VISIBLE);
        customTxt = (TextView) view.findViewById(R.id.customLoadTxt);
        customTxt.setText(customMessage);

        return builder.create();
    }

    public void setDialogText(String text) {
        customMessage = text;
    }

    public void setCustomTitle(String text) { customTitle = text; }

    public ProgressBar getProgressBar() {
        return this.spinner;
    }
}
