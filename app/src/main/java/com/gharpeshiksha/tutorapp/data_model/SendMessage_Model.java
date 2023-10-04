package com.gharpeshiksha.tutorapp.data_model;

import com.google.gson.annotations.SerializedName;

public class SendMessage_Model {


    @SerializedName("studentUUID")
    private String studentUUID;
    @SerializedName("tutorUUID")
    private String tutorUUID;
    @SerializedName("send")
    private String send;
    @SerializedName("timestamp")
    private String timestamp;

    public SendMessage_Model(String studentUUID, String tutorUUID, String send, String timestamp) {
        this.studentUUID = studentUUID;
        this.tutorUUID = tutorUUID;
        this.send = send;
        this.timestamp = timestamp;
    }

    public String getStudentUUID() {
        return studentUUID;
    }

    public void setStudentUUID(String studentUUID) {
        this.studentUUID = studentUUID;
    }

    public String getTutorUUID() {
        return tutorUUID;
    }

    public void setTutorUUID(String tutorUUID) {
        this.tutorUUID = tutorUUID;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SendMessage_Model{" +
                "studentUUID='" + studentUUID + '\'' +
                ", tutorUUID='" + tutorUUID + '\'' +
                ", send='" + send + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
