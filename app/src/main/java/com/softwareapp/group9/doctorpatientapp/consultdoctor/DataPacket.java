package com.softwareapp.group9.doctorpatientapp.consultdoctor;

public class DataPacket {
    public String patientId;
    public String doctorId;
    public String condition;
    public String description;
    public String mediaReference;
    public String filesReference;

    public DataPacket(){

    }

    public DataPacket(String patientId, String doctorId, String condition, String description, String mediaReference, String filesReference){
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.condition = condition;
        this.description = description;
        this.mediaReference = mediaReference;
        this.filesReference = filesReference;
    }
}
