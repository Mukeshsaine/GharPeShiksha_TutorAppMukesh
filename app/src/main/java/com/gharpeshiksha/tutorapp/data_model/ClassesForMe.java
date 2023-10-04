package com.gharpeshiksha.tutorapp.data_model;

public class ClassesForMe {
    private String textMins, textViews, textName, textTutorReq, textBudget, textLoc, favorite, status, paymentstatus, highComp, freeClass
            , studentUUId, tutorUUId, picUrl;
    long textEnq_id;
    double textDis;
    private Boolean enq_viewed, feedback;

    public ClassesForMe() {}
    public ClassesForMe(long textEnq_id, String textMins, String textViews, String textName, String textTutorReq, String textBudget,
                        String textLoc, String favorite, double textDis, String status, String paymentstatus, Boolean enq_viewed,
                        Boolean feedback, String highComp, String freeClass, String studentUUId, String tutorUUId, String picUrl) {
        this.textMins = textMins;
        this.textViews = textViews;
        this.textName = textName;
        this.textTutorReq = textTutorReq;
        this.textBudget = textBudget;
        this.textEnq_id = textEnq_id;
        this.textLoc = textLoc;
        this.textDis = textDis;
        this.favorite = favorite;
        this.status = status;
        this.paymentstatus = paymentstatus;
        this.enq_viewed = enq_viewed;
        this.feedback = feedback;
        this.highComp =highComp;
        this.freeClass =freeClass;
        this.studentUUId = studentUUId;
        this.tutorUUId = tutorUUId;
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getStudentUUId() {
        return studentUUId;
    }

    public void setStudentUUId(String studentUUId) {
        this.studentUUId = studentUUId;
    }

    public String getTutorUUId() {
        return tutorUUId;
    }

    public void setTutorUUId(String tutorUUId) {
        this.tutorUUId = tutorUUId;
    }

    public Boolean getFeedback() {
        return feedback;
    }

    public void setFeedback(Boolean feedback) {
        this.feedback = feedback;
    }

    public Boolean getEnq_viewed() {
        return enq_viewed;
    }

    public void setEnq_viewed(Boolean enq_viewed) {
        this.enq_viewed = enq_viewed;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getTextMins() {
        return textMins;
    }

    public void setTextMins(String textMins) {
        this.textMins = textMins;
    }

    public String getTextViews() {
        return textViews;
    }

    public void setTextViews(String textViews) {
        this.textViews = textViews;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public String getTextTutorReq() {
        return textTutorReq;
    }

    public void setTextTutorReq(String textTutorReq) {
        this.textTutorReq = textTutorReq;
    }

    public String getTextBudget() {
        return textBudget;
    }

    public void setTextBudget(String textBudget) {
        this.textBudget = textBudget;
    }

    public long getTextEnq_id() {
        return textEnq_id;
    }

    public void setTextEnq_id(long textEnq_id) {
        this.textEnq_id = textEnq_id;
    }

    public String getTextLoc() {
        return textLoc;
    }

    public void setTextLoc(String textLoc) {
        this.textLoc = textLoc;
    }

    public double getTextDis() {
        return textDis;
    }

    public void setTextDis(double textDis) {
        this.textDis = textDis;
    }

    public String getHighComp() {
        return highComp;
    }

    public void setHighComp(String highComp) {
        this.highComp = highComp;
    }

    public String getFreeClass() {
        return freeClass;
    }

    public void setFreeClass(String freeClass) {
        this.freeClass = freeClass;
    }

    @Override
    public String toString() {
        return "ClassesForMe{" +
                "textMins='" + textMins + '\'' +
                ", textViews='" + textViews + '\'' +
                ", textName='" + textName + '\'' +
                ", textTutorReq='" + textTutorReq + '\'' +
                ", textBudget='" + textBudget + '\'' +
                ", textLoc='" + textLoc + '\'' +
                ", favorite='" + favorite + '\'' +
                ", status='" + status + '\'' +
                ", paymentstatus='" + paymentstatus + '\'' +
                ", highComp='" + highComp + '\'' +
                ", freeClass='" + freeClass + '\'' +
                ", textEnq_id=" + textEnq_id +
                ", textDis=" + textDis +
                ", enq_viewed=" + enq_viewed +
                ", feedback=" + feedback +
                '}';
    }
}
