package com.gharpeshiksha.tutorapp.data_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Plan_Models {

    @SerializedName("Status")
    public String status;
    public int contact_viewed;
    public String tutorAbout;
    public String gender;
    @SerializedName("Message")
    public String message;
    @SerializedName("Pic_url")
    public String pic_url;
    public String expiry_date;
    public String vehicle;
    public String qualification;
    @SerializedName("Experience")
    public int experience;
    public String planDetail;
    @SerializedName("Expected_fees")
    public int expected_fees;
    public String dob;
    public String name;
    public String course;
    public ArrayList<Integer> classes_sent;
    @SerializedName("Alt_contact")
    public String alt_contact;
    public String email;
    @SerializedName("Permanent_add")
    public String permanent_add;
    @SerializedName("Current_add")
    public String current_add;
    public ArrayList<String> teaching_mode;

    public Plan_Models() {
    }

    public Plan_Models(String status, int contact_viewed, String tutorAbout, String gender, String message, String pic_url, String expiry_date, String vehicle, String qualification, int experience, String planDetail, int expected_fees, String dob, String name, String course, ArrayList<Integer> classes_sent, String alt_contact, String email, String permanent_add, String current_add, ArrayList<String> teaching_mode) {
        this.status = status;
        this.contact_viewed = contact_viewed;
        this.tutorAbout = tutorAbout;
        this.gender = gender;
        this.message = message;
        this.pic_url = pic_url;
        this.expiry_date = expiry_date;
        this.vehicle = vehicle;
        this.qualification = qualification;
        this.experience = experience;
        this.planDetail = planDetail;
        this.expected_fees = expected_fees;
        this.dob = dob;
        this.name = name;
        this.course = course;
        this.classes_sent = classes_sent;
        this.alt_contact = alt_contact;
        this.email = email;
        this.permanent_add = permanent_add;
        this.current_add = current_add;
        this.teaching_mode = teaching_mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getContact_viewed() {
        return contact_viewed;
    }

    public void setContact_viewed(int contact_viewed) {
        this.contact_viewed = contact_viewed;
    }

    public String getTutorAbout() {
        return tutorAbout;
    }

    public void setTutorAbout(String tutorAbout) {
        this.tutorAbout = tutorAbout;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getPlanDetail() {
        return planDetail;
    }

    public void setPlanDetail(String planDetail) {
        this.planDetail = planDetail;
    }

    public int getExpected_fees() {
        return expected_fees;
    }

    public void setExpected_fees(int expected_fees) {
        this.expected_fees = expected_fees;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public ArrayList<Integer> getClasses_sent() {
        return classes_sent;
    }

    public void setClasses_sent(ArrayList<Integer> classes_sent) {
        this.classes_sent = classes_sent;
    }

    public String getAlt_contact() {
        return alt_contact;
    }

    public void setAlt_contact(String alt_contact) {
        this.alt_contact = alt_contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPermanent_add() {
        return permanent_add;
    }

    public void setPermanent_add(String permanent_add) {
        this.permanent_add = permanent_add;
    }

    public String getCurrent_add() {
        return current_add;
    }

    public void setCurrent_add(String current_add) {
        this.current_add = current_add;
    }

    public ArrayList<String> getTeaching_mode() {
        return teaching_mode;
    }

    public void setTeaching_mode(ArrayList<String> teaching_mode) {
        this.teaching_mode = teaching_mode;
    }
}