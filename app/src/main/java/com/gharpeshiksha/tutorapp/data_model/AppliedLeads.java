package com.gharpeshiksha.tutorapp.data_model;

public class AppliedLeads {
    private String textAppName;
    private String textAppPosted;
    private String textAppViewed;
    private String textAppBudget;
    private String textAppLoc;
    private String textAppDis;
    private String textAppTutorReq;
    private String textAppParMes;
    private String textAppMyFeed;
    private String textAppGPSMes;
    private String textEnq_id;
    private String feedback_given;
    private String contact;
    private String otp_given, studentUUID, tutorUUID, picUrl;
    /*private String gender;*/

    public AppliedLeads(String textAppName, String textAppPosted, String textAppBudget,
                        String textAppViewed, String textAppLoc, String textAppDis, String textAppTutorReq,
                        String textAppParMes, String textAppMyFeed, String textAppGPSMes, String textEnq_id,
                        String feedback_given, String contact, String otp_given/*, String gender*/) {
        this.textAppName = textAppName;
        this.textAppPosted = textAppPosted;
        this.textAppBudget = textAppBudget;
        this.textAppViewed = textAppViewed;
        this.textAppLoc = textAppLoc;
        this.textAppDis = textAppDis;
        this.textAppTutorReq = textAppTutorReq;
        this.textAppParMes = textAppParMes;
        this.textAppMyFeed = textAppMyFeed;
        this.textAppGPSMes = textAppGPSMes;
        this.textEnq_id = textEnq_id;
        this.feedback_given = feedback_given;
        this.contact = contact;
        this.otp_given = otp_given;
        /*this.gender = gender;*/
    }

    public AppliedLeads(String textAppName, String textAppPosted, String textAppBudget,
                        String textAppViewed, String textAppLoc, String textAppDis, String textAppTutorReq,
                        String textAppParMes, String textAppMyFeed, String textAppGPSMes, String textEnq_id,
                        String feedback_given, String contact, String otp_given/*, String gender*/, String studentUUID,
                        String tutorUUID, String picUrl) {
        this.textAppName = textAppName;
        this.textAppPosted = textAppPosted;
        this.textAppBudget = textAppBudget;
        this.textAppViewed = textAppViewed;
        this.textAppLoc = textAppLoc;
        this.textAppDis = textAppDis;
        this.textAppTutorReq = textAppTutorReq;
        this.textAppParMes = textAppParMes;
        this.textAppMyFeed = textAppMyFeed;
        this.textAppGPSMes = textAppGPSMes;
        this.textEnq_id = textEnq_id;
        this.feedback_given = feedback_given;
        this.contact = contact;
        this.otp_given = otp_given;
        this.studentUUID = studentUUID;
        this.tutorUUID = tutorUUID;
        this.picUrl = picUrl;
        /*this.gender = gender;*/
    }

   /* public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }*/

    @Override
    public String toString() {
        return "AppliedLeads{" +
                "textAppName='" + textAppName + '\'' +
                ", textAppPosted='" + textAppPosted + '\'' +
                ", textAppViewed='" + textAppViewed + '\'' +
                ", textAppBudget='" + textAppBudget + '\'' +
                ", textAppLoc='" + textAppLoc + '\'' +
                ", textAppDis='" + textAppDis + '\'' +
                ", textAppTutorReq='" + textAppTutorReq + '\'' +
                ", textAppParMes='" + textAppParMes + '\'' +
                ", textAppMyFeed='" + textAppMyFeed + '\'' +
                ", textAppGPSMes='" + textAppGPSMes + '\'' +
                ", textEnq_id='" + textEnq_id + '\'' +
                ", feedback_given='" + feedback_given + '\'' +
                ", contact='" + contact + '\'' +
                ", otp_given='" + otp_given + '\'' +
                ", studentUUID='" + studentUUID + '\'' +
                ", tutorUUID='" + tutorUUID + '\'' +
                ", picUrl='" + picUrl + '\'' +
                '}';
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

    public String getTutorUUID() {
        return tutorUUID;
    }

    public void setTutorUUID(String tutorUUID) {
        this.tutorUUID = tutorUUID;
    }

    public String getTextAppName() {
        return textAppName;
    }

    public void setTextAppName(String textAppName) {
        this.textAppName = textAppName;
    }

    public String getTextAppPosted() {
        return textAppPosted;
    }

    public void setTextAppPosted(String textAppPosted) {
        this.textAppPosted = textAppPosted;
    }

    public String getTextAppBudget() {
        return textAppBudget;
    }

    public void setTextAppBudget(String textAppBudget) {
        this.textAppBudget = textAppBudget;
    }

    public String getTextAppViewed() {
        return textAppViewed;
    }

    public String getTextAppTutorReq() {
        return textAppTutorReq;
    }

    public void setTextAppTutorReq(String textAppTutorReq) {
        this.textAppTutorReq = textAppTutorReq;
    }

    public void setTextAppViewed(String textAppViewed) {
        this.textAppViewed = textAppViewed;
    }

    public String getTextAppLoc() {
        return textAppLoc;
    }

    public void setTextAppLoc(String textAppLoc) {
        this.textAppLoc = textAppLoc;
    }

    public String getTextAppDis() {
        return textAppDis;
    }

    public void setTextAppDis(String textAppDis) {
        this.textAppDis = textAppDis;
    }

    public String getTextAppParMes() {
        return textAppParMes;
    }

    public void setTextAppParMes(String textAppParMes) {
        this.textAppParMes = textAppParMes;
    }

    public String getTextAppMyFeed() {
        return textAppMyFeed;
    }

    public void setTextAppMyFeed(String textAppMyFeed) {
        this.textAppMyFeed = textAppMyFeed;
    }

    public String getTextAppGPSMes() {
        return textAppGPSMes;
    }

    public void setTextAppGPSMes(String textAppGPSMes) {
        this.textAppGPSMes = textAppGPSMes;
    }

    public String getTextEnq_id() {
        return textEnq_id;
    }

    public void setTextEnq_id(String textEnq_id) {
        this.textEnq_id = textEnq_id;
    }

    public String getFeedback_given() {
        return feedback_given;
    }

    public void setFeedback_given(String feedback_given) {
        this.feedback_given = feedback_given;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOtp_given() {
        return otp_given;
    }

    public void setOtp_given(String otp_given) {
        this.otp_given = otp_given;
    }
}
