package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softwareapp.group9.doctorpatientapp.R;

import java.io.File;

public class ChangePhotoDialog extends DialogFragment {
    private static final String TAG = "ChangePhotoDialog";


    public interface OnPhotoReceivedListener{
        public void getBitmapImage(Bitmap bitmap);

    }

    OnPhotoReceivedListener mOnPhotoReceived;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_changephoto, container, false);

        //initalize the textview for starting the camera
        TextView takePhoto = (TextView) view.findViewById(R.id.dialogTakePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: starting camera.");
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, Init.CAMER_REQUEST_CODE);
                Intent intent = new Intent(getContext(), TakeImageFromCameraActivity.class);
                startActivity(intent);
            }
        });

        //initalize the textview for choosing an image from memory
        TextView selectPhoto = (TextView) view.findViewById(R.id.dialogChoosePhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: accessing phones memory.");
                Intent intent = new Intent(getContext(), UploadActivity.class);
                startActivity(intent);
            }
        });

        //initialize the textview for uploading the video
        TextView uploadVideo = (TextView) view.findViewById(R.id.dialogUplaodVideo);
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: select or record video");
                Intent intent = new Intent(getContext(), UploadVideoActivity.class);
                startActivity(intent);
            }
        });

        //Cancel button for closing the dialog
        TextView cancelDialog = (TextView) view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: closing dialog.");
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

            mOnPhotoReceived = (OnPhotoReceivedListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * results when taking a new image with camera
         */
        if(requestCode == Init.CAMER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: done taking a picture");

            //get the new image bitmap
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Log.d(TAG, "onActivityResult: received bitmap: " + bitmap);

            //send the bitmap and fragment to the interface
            mOnPhotoReceived.getBitmapImage(bitmap);
            getDialog().dismiss();
        }


    }
}
