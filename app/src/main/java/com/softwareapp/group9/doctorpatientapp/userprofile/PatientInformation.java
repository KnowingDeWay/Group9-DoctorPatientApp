package com.softwareapp.group9.doctorpatientapp.userprofile;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientInformation  {

    public String id;
    public String surname;
    public String otherName;
    public String gender;
    public String age;
    public String height;
    public String weight;
    public String address;

    public PatientInformation(){

    }

    public boolean equals(PatientInformation other){
        return this.id.contentEquals(other.id) && this.surname.contentEquals(other.surname) && this.otherName.contentEquals(other.otherName)
                && this.gender.contentEquals(other.gender) && this.age.contentEquals(other.age) && this.height.contentEquals(other.height)
                && this.weight.contentEquals(other.weight) && this.address.contentEquals(other.address);
    }

    public PatientInformation(String id, String surname, String otherName, String gender, String age, String height, String weight, String address) {
        this.id = id;
        this.surname = surname;
        this.otherName = otherName;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.address = address;
    }

    public PatientInformation(String surname, String otherName, String gender, String age, String height, String weight, String address) {
        this.surname = surname;
        this.otherName = otherName;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.address = address;
    }
}
