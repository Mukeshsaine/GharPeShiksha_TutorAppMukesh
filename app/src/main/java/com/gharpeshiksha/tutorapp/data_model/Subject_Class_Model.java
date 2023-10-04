package com.gharpeshiksha.tutorapp.data_model;

import com.google.gson.annotations.SerializedName;

public class Subject_Class_Model {

    @SerializedName("sub")
    private String sub;
    @SerializedName("img")
    private String img;

    public Subject_Class_Model(String sub, String img) {
        this.sub = sub;
        this.img = img;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
