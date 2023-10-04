package com.gharpeshiksha.tutorapp.data_model;

import com.google.gson.annotations.SerializedName;

public class Classes_Model {

    @SerializedName("image")
    private String image;
    @SerializedName("class")
    private String studentscla;

    @SerializedName("filterCls")
    private String filterCls;

    public Classes_Model(String image, String studentscla, String filterCls) {
        this.image = image;
        this.studentscla = studentscla;
        this.filterCls = filterCls;
    }

    public String getFilterCls() {
        return filterCls;
    }

    public void setFilterCls(String filterCls) {
        this.filterCls = filterCls;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStudentscla() {
        return studentscla;
    }

    public void setStudentscla(String studentscla) {
        this.studentscla = studentscla;
    }

    @Override
    public String toString() {
        return "Classes_Model{" +
                "image='" + image + '\'' +
                ", studentscla='" + studentscla + '\'' +
                ", filterCls='" + filterCls + '\'' +
                '}';
    }
}
