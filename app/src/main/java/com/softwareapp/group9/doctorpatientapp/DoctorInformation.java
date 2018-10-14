package com.softwareapp.group9.doctorpatientapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class DoctorInformation {

    public String docId;
    public String docSurname;
    public String docOtherName;
    public String docDepartment;
    public String docFullName;

    public DoctorInformation() {

    }

    public boolean equals(DoctorInformation other) {
        return this.docId.contentEquals(other.docId) && this.docSurname.contentEquals(other.docSurname) && this.docOtherName.contentEquals(other.docOtherName)
                && this.docDepartment.contentEquals(other.docDepartment);
    }

    public DoctorInformation(String docId, String docSurname, String docOtherName, String docDepartment) {
        this.docId = docId;
        this.docSurname = docSurname;
        this.docOtherName = docOtherName;
        this.docDepartment = docDepartment;
        this.docFullName = docOtherName + " " + docSurname;
    }

    public DoctorInformation(String docSurname, String docOtherName, String docDepartment) {
        this.docSurname = docSurname;
        this.docOtherName = docOtherName;
        this.docDepartment = docDepartment;
        this.docFullName = docOtherName + " " + docSurname;
    }

}


