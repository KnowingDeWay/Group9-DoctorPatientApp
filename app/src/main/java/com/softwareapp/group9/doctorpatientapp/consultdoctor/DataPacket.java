package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.os.Parcel;
import android.os.Parcelable;

import com.softwareapp.group9.doctorpatientapp.facilitiesnearme.UserLocation;

import java.util.ArrayList;

public class DataPacket {

    public String packetId;
    public String patientId;
    public String doctorId;
    public String condition;
    public String description;
    public ArrayList<String> mediaReferences;
    public ArrayList<String> filesReferences;
    public int heartBeat;
    public UserLocation location;
    public String docRecommendation;

    public DataPacket(){

    }

    public DataPacket(String patientId, String doctorId, String condition, String description, ArrayList<String> mediaReferences, ArrayList<String> filesReferences){
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.condition = condition;
        this.description = description;
        this.mediaReferences = mediaReferences;
        this.filesReferences = filesReferences;
    }
}
