package com.softwareapp.group9.doctorpatientapp.facilitiesnearme;

public class UserLocation {
    public double latitude;
    public double longitude;

    public UserLocation(){

    }

    public UserLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UserLocation(String latitude, String longitude){
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
    }
}
