package com.softwareapp.group9.doctorpatientapp.facilitiesnearme;

public class UserInformation {

    public String surname;
    public double latitude;
    public double longitude;

    public UserInformation(){

    }

    public UserInformation(String surname, double latitude, double longitude){
        this.surname = surname;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
