package com.gharpeshiksha.tutorapp.data_model;

import com.google.gson.annotations.SerializedName;

public class Model_archived {

    @SerializedName("area")
    private String area;
    @SerializedName("studentUUID")
    private String studentUUID;
    @SerializedName("distance")
    private double distance;
    @SerializedName("subjects")
    private String subjects;
    @SerializedName("tutorUUID")
    private String tutorUUID;
    @SerializedName("enq_id")
    private long enq_id;
    @SerializedName("op_count")
    private double op_count;
    @SerializedName("highComp")
    private String highComp1;
    @SerializedName("freeClass")
    private String freeClass1;
    @SerializedName("name")
    private String name;
    @SerializedName("paymentstatus")
    private String paymentstatus;
    @SerializedName("tutors_contacted")
    private int tutors_contacted;
    @SerializedName("time")
    private String time;
    @SerializedName("favorite")
    private String favorite;
    @SerializedName("class")
    private String classfor;
    @SerializedName("budget")
    private String budget;
    @SerializedName("status")
    private String status1;
    @SerializedName("enq_viewed")
    private Boolean enq_viewed;
    @SerializedName("cursorStr")
    private String cursor;
    @SerializedName("timestamp")
    private long timestamp;

    public Model_archived() {
    }

    public Model_archived(String area, String studentUUID, double distance, String subjects, String tutorUUID, long enq_id, double op_count,
                          String highComp1, String freeClass1, String name, String paymentstatus, int tutors_contacted, String time,
                          String favorite, String classfor, String budget, String status1, Boolean enq_viewed, String cursor,long timestamp) {
        this.area = area;
        this.studentUUID = studentUUID;
        this.distance = distance;
        this.subjects = subjects;
        this.tutorUUID = tutorUUID;
        this.enq_id = enq_id;
        this.op_count = op_count;
        this.highComp1 = highComp1;
        this.freeClass1 = freeClass1;
        this.name = name;
        this.paymentstatus = paymentstatus;
        this.tutors_contacted = tutors_contacted;
        this.time = time;
        this.favorite = favorite;
        this.classfor = classfor;
        this.budget = budget;
        this.status1 = status1;
        this.enq_viewed = enq_viewed;
        this.cursor = cursor;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStudentUUID() {
        return studentUUID;
    }

    public void setStudentUUID(String studentUUID) {
        this.studentUUID = studentUUID;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getTutorUUID() {
        return tutorUUID;
    }

    public void setTutorUUID(String tutorUUID) {
        this.tutorUUID = tutorUUID;
    }

    public long getEnq_id() {
        return enq_id;
    }

    public void setEnq_id(long enq_id) {
        this.enq_id = enq_id;
    }

    public double getOp_count() {
        return op_count;
    }

    public void setOp_count(double op_count) {
        this.op_count = op_count;
    }

    public String getHighComp1() {
        return highComp1;
    }

    public void setHighComp1(String highComp1) {
        this.highComp1 = highComp1;
    }

    public String getFreeClass1() {
        return freeClass1;
    }

    public void setFreeClass1(String freeClass1) {
        this.freeClass1 = freeClass1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public int getTutors_contacted() {
        return tutors_contacted;
    }

    public void setTutors_contacted(int tutors_contacted) {
        this.tutors_contacted = tutors_contacted;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getClassfor() {
        return classfor;
    }

    public void setClassfor(String classfor) {
        this.classfor = classfor;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public Boolean getEnq_viewed() {
        return enq_viewed;
    }

    public void setEnq_viewed(Boolean enq_viewed) {
        this.enq_viewed = enq_viewed;
    }

    @Override
    public String toString() {
        return "Model_archived{" +
                "area='" + area + '\'' +
                ", studentUUID=" + studentUUID +
                ", distance=" + distance +
                ", subjects='" + subjects + '\'' +
                ", tutorUUID=" + tutorUUID +
                ", enq_id=" + enq_id +
                ", op_count=" + op_count +
                ", highComp1='" + highComp1 + '\'' +
                ", freeClass1='" + freeClass1 + '\'' +
                ", name='" + name + '\'' +
                ", paymentstatus='" + paymentstatus + '\'' +
                ", tutors_contacted=" + tutors_contacted +
                ", time='" + time + '\'' +
                ", favorite='" + favorite + '\'' +
                ", classfor='" + classfor + '\'' +
                ", budget='" + budget + '\'' +
                ", status1='" + status1 + '\'' +
                ", enq_viewed='" + enq_viewed +
                ", cursor=" + cursor +
                '}';
    }
}
