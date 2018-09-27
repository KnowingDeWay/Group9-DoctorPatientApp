package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.Manifest;

public class Init {
    public Init(){

    }
    public static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    public static final int CAMER_REQUEST_CODE = 5;
    public static final int PICFILE_REQUEST_CODE = 8;

}
