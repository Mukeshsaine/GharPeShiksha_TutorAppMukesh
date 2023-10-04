package com.gharpeshiksha.tutorapp.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllIndiaModel {
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("cursor")
    @Expose
    private String cursor;
    @SerializedName("studentUUID")
    @Expose
    private String studentUUID;
    @SerializedName("distance")
    @Expose
    private Integer distance;
    @SerializedName("default_profile_pic")
    @Expose
    private String defaultProfilePic;
    @SerializedName("subjects")
    @Expose
    private String subjects;
    @SerializedName("tutorUUID")
    @Expose
    private String tutorUUID;
    @SerializedName("enq_id")
    @Expose
    private long enqId;
    @SerializedName("enq_viewed")
    @Expose
    private boolean enqViewed;
    @SerializedName("highComp")
    @Expose
    private boolean highComp;
    @SerializedName("freeClass")
    @Expose
    private boolean freeClass;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("paymentstatus")
    @Expose
    private String paymentstatus;
    @SerializedName("tutors_contacted")
    @Expose
    private long tutorsContacted;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("favorite")
    @Expose
    private String favorite;
    @SerializedName("class")
    @Expose
    private String _class;
    @SerializedName("budget")
    @Expose
    private String budget;
    @SerializedName("status")
    @Expose
    private String status;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String getStudentUUID() {
        return studentUUID;
    }

    public void setStudentUUID(String studentUUID) {
        this.studentUUID = studentUUID;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getDefaultProfilePic() {
        return defaultProfilePic;
    }

    public void setDefaultProfilePic(String defaultProfilePic) {
        this.defaultProfilePic = defaultProfilePic;
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

    public long getEnqId() {
        return enqId;
    }

    public void setEnqId(Integer enqId) {
        this.enqId = enqId;
    }

    public Boolean getEnqViewed() {
        return enqViewed;
    }

    public void setEnqViewed(Boolean enqViewed) {
        this.enqViewed = enqViewed;
    }

    public Boolean getHighComp() {
        return highComp;
    }

    public void setHighComp(Boolean highComp) {
        this.highComp = highComp;
    }

    public Boolean getFreeClass() {
        return freeClass;
    }

    public void setFreeClass(Boolean freeClass) {
        this.freeClass = freeClass;
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

    public long getTutorsContacted() {
        return tutorsContacted;
    }

    public void setTutorsContacted(Integer tutorsContacted) {
        this.tutorsContacted = tutorsContacted;
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

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AllIndiaModel{" +
                "area='" + area + '\'' +
                ", cursor='" + cursor + '\'' +
                ", studentUUID='" + studentUUID + '\'' +
                ", distance=" + distance +
                ", defaultProfilePic='" + defaultProfilePic + '\'' +
                ", subjects='" + subjects + '\'' +
                ", tutorUUID='" + tutorUUID + '\'' +
                ", enqId=" + enqId +
                ", enqViewed=" + enqViewed +
                ", highComp=" + highComp +
                ", freeClass=" + freeClass +
                ", name='" + name + '\'' +
                ", paymentstatus='" + paymentstatus + '\'' +
                ", tutorsContacted=" + tutorsContacted +
                ", time='" + time + '\'' +
                ", favorite='" + favorite + '\'' +
                ", _class='" + _class + '\'' +
                ", budget='" + budget + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
