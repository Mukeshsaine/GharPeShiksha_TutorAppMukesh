package com.gharpeshiksha.tutorapp.data_model;

public class Written_Testimonial {

    String picurl;
    String desc;
    String tutorID;
    String tutorName;

    public Written_Testimonial(String picurl, String desc, String tutorID, String tutorName) {
        this.picurl = picurl;
        this.desc = desc;
        this.tutorID = tutorID;
        this.tutorName = tutorName;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTutorID() {
        return tutorID;
    }

    public void setTutorID(String tutorID) {
        this.tutorID = tutorID;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }


}
