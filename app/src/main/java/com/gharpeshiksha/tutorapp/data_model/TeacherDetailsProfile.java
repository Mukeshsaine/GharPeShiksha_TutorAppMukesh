package com.gharpeshiksha.tutorapp.data_model;

import androidx.annotation.StringRes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TeacherDetailsProfile {

   /* @SerializedName("date")
    private String date;*/


    @SerializedName("closingTime")
    private String closingTime;
    @SerializedName("openingTime")
    private String openingTime;
    @SerializedName("day")
    private String day;

    @SerializedName("distance")
    private String distance;
    @SerializedName("profilepic")
    private String profilepic;
    @SerializedName("subject")
    public ArrayList<String> subject;
    @SerializedName("classes")
    private String classes;
    @SerializedName("rating")
    private double rating;
    @SerializedName("lon")
    private double lon;
    @SerializedName("experience")
    private String experience;
    @SerializedName("mode")
    public ArrayList<String> mode;
    @SerializedName("qualification")
    private String qualification;
    @SerializedName("feeSession")
    private String feeSession;
    @SerializedName("tutorName")
    private String tutorName;
    @SerializedName("age")
    private int age;
    @SerializedName("tutId")
    private int tutId;
    @SerializedName("about")
    private String about;
    @SerializedName("lat")
    private double lat;


    public TeacherDetailsProfile() {
    }

    public TeacherDetailsProfile(String closingTime, String openingTime, String day, String distance, String profilepic, ArrayList<String> subject, String classes, double rating, double lon, String experience, ArrayList<String> mode, String qualification, String feeSession, String tutorName, int age, int tutId, String about, double lat) {
        this.closingTime = closingTime;
        this.openingTime = openingTime;
        this.day = day;
        this.distance = distance;
        this.profilepic = profilepic;
        this.subject = subject;
        this.classes = classes;
        this.rating = rating;
        this.lon = lon;
        this.experience = experience;
        this.mode = mode;
        this.qualification = qualification;
        this.feeSession = feeSession;
        this.tutorName = tutorName;
        this.age = age;
        this.tutId = tutId;
        this.about = about;
        this.lat = lat;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public ArrayList<String> getSubject() {
        return subject;
    }

    public void setSubject(ArrayList<String> subject) {
        this.subject = subject;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public ArrayList<String> getMode() {
        return mode;
    }

    public void setMode(ArrayList<String> mode) {
        this.mode = mode;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getFeeSession() {
        return feeSession;
    }

    public void setFeeSession(String feeSession) {
        this.feeSession = feeSession;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTutId() {
        return tutId;
    }

    public void setTutId(int tutId) {
        this.tutId = tutId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "TeacherDetailsProfile{" +
                "closingTime='" + closingTime + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", day='" + day + '\'' +
                ", distance='" + distance + '\'' +
                ", profilepic='" + profilepic + '\'' +
                ", subject=" + subject +
                ", classes='" + classes + '\'' +
                ", rating=" + rating +
                ", lon=" + lon +
                ", experience='" + experience + '\'' +
                ", mode=" + mode +
                ", qualification='" + qualification + '\'' +
                ", feeSession='" + feeSession + '\'' +
                ", tutorName='" + tutorName + '\'' +
                ", age=" + age +
                ", tutId=" + tutId +
                ", about='" + about + '\'' +
                ", lat=" + lat +
                '}';
    }
}
