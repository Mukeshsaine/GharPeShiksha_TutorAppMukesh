package com.gharpeshiksha.tutorapp.data_model;

import com.google.gson.annotations.SerializedName;

public class Model_Chats {
    @SerializedName("picUrl")
    private String picUrl;
    @SerializedName("studentUUID")
    private String studentUUID;
    @SerializedName("studentname")
    private String studentname;
    @SerializedName("tutorUUID")
    private String tutorUUID;
    @SerializedName("message")
    private String message;
    @SerializedName("day")
    private String day;
    @SerializedName("timestampMilliSec")
    private String timestampInMilliSec;
    @SerializedName("enqId")
    private String enqId;
    @SerializedName("isApproved")
    private String isApproved;
    @SerializedName("sendBy")
    private String sendBy;
    private String chatStatus;

    public Model_Chats(String picUrl, String studentUUID, String studentname, String tutorUUID, String message, String day, String timestampInMilliSec, String enqId, String isApproved, String sendBy, String chatStatus) {
        this.picUrl = picUrl;
        this.studentUUID = studentUUID;
        this.studentname = studentname;
        this.tutorUUID = tutorUUID;
        this.message = message;
        this.day = day;
        this.timestampInMilliSec = timestampInMilliSec;
        this.enqId = enqId;
        this.isApproved = isApproved;
        this.sendBy = sendBy;
        this.chatStatus = chatStatus;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getStudentUUID() {
        return studentUUID;
    }

    public void setStudentUUID(String studentUUID) {
        this.studentUUID = studentUUID;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getTutorUUID() {
        return tutorUUID;
    }

    public void setTutorUUID(String tutorUUID) {
        this.tutorUUID = tutorUUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimestampInMilliSec() {
        return timestampInMilliSec;
    }

    public void setTimestampInMilliSec(String timestampInMilliSec) {
        this.timestampInMilliSec = timestampInMilliSec;
    }

    public String getEnqId() {
        return enqId;
    }

    public void setEnqId(String enqId) {
        this.enqId = enqId;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }

    /* public Model_Chats(String picUrl, String studentUUID, String studentname, String tutorUUID, String message, String day, String timestampInMilliSec, String enqId, String isApproved, String sendBy) {
        this.picUrl = picUrl;
        this.studentUUID = studentUUID;
        this.studentname = studentname;
        this.tutorUUID = tutorUUID;
        this.message = message;
        this.day = day;
        this.timestampInMilliSec = timestampInMilliSec;
        this.enqId = enqId;
        this.isApproved = isApproved;
        this.sendBy = sendBy;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getEnqId() {
        return enqId;
    }

    public void setEnqId(String enqId) {
        this.enqId = enqId;
    }

    public String getTimestampInMilliSec() {
        return timestampInMilliSec;
    }

    public void setTimestampInMilliSec(String timestampInMilliSec) {
        this.timestampInMilliSec = timestampInMilliSec;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getStudentUUID() {
        return studentUUID;
    }

    public void setStudentUUID(String studentUUID) {
        this.studentUUID = studentUUID;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getTutorUUID() {
        return tutorUUID;
    }

    public void setTutorUUID(String tutorUUID) {
        this.tutorUUID = tutorUUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return day;
    }

    public void setTimestamp(String timestamp) {
        this.day = timestamp;
    }

    @Override
    public String toString() {
        return "Model_Chats{" +
                "picUrl='" + picUrl + '\'' +
                ", studentUUID='" + studentUUID + '\'' +
                ", studentname='" + studentname + '\'' +
                ", tutorUUID='" + tutorUUID + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + day + '\'' +
                '}';
    }*/
}
