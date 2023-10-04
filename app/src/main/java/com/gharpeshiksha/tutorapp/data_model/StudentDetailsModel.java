package com.gharpeshiksha.tutorapp.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentDetailsModel {
    @SerializedName("profileURL")
    @Expose
    private String profileURL;
    @SerializedName("sub")
    @Expose
    private String sub;
    @SerializedName("studentUUID")
    @Expose
    private String studentUUID;
    @SerializedName("lastActiveTime")
    @Expose
    private Integer lastActiveTime;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("tutorUUID")
    @Expose
    private String tutorUUID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("cls")
    @Expose
    private String cls;
    @SerializedName("lat")
    @Expose
    private Double lat;

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getStudentUUID() {
        return studentUUID;
    }

    public void setStudentUUID(String studentUUID) {
        this.studentUUID = studentUUID;
    }

    public Integer getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Integer lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTutorUUID() {
        return tutorUUID;
    }

    public void setTutorUUID(String tutorUUID) {
        this.tutorUUID = tutorUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "StudentDetailsModel{" +
                "profileURL='" + profileURL + '\'' +
                ", sub='" + sub + '\'' +
                ", studentUUID='" + studentUUID + '\'' +
                ", lastActiveTime=" + lastActiveTime +
                ", address='" + address + '\'' +
                ", tutorUUID='" + tutorUUID + '\'' +
                ", name='" + name + '\'' +
                ", lon=" + lon +
                ", cls='" + cls + '\'' +
                ", lat=" + lat +
                '}';
    }
}
