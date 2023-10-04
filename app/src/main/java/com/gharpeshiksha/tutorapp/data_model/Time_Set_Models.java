package com.gharpeshiksha.tutorapp.data_model;

import com.google.gson.annotations.SerializedName;

public class Time_Set_Models{


    @SerializedName("save")
    private String save;
    @SerializedName("day")
    private String day;
    @SerializedName("opening")
    private String opening;
    @SerializedName("closing")
    private String closing;
    @SerializedName("update")
    private String update;
    @SerializedName("delete")
    private String delete;
    @SerializedName("timestamp")
    public String timestamp;

    public Time_Set_Models() {
    }

    public Time_Set_Models(String save, String day, String opening, String closing, String update, String delete, String timestamp) {
        this.save = save;
        this.day = day;
        this.opening = opening;
        this.closing = closing;
        this.update = update;
        this.delete = delete;
        this.timestamp = timestamp;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Time_Set_Models{" +
                "save='" + save + '\'' +
                ", day='" + day + '\'' +
                ", opening='" + opening + '\'' +
                ", closing='" + closing + '\'' +
                ", update='" + update + '\'' +
                ", delete='" + delete + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
