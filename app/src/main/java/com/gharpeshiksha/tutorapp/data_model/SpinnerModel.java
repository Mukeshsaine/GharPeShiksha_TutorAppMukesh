package com.gharpeshiksha.tutorapp.data_model;

public class SpinnerModel {
    private String day;
    private String openingTime;
    private String closingTime;
    public String timestamp;

    public SpinnerModel(String day, String openingTime, String closingTime) {
        this.day = day;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.timestamp = timestamp;
    }

    public String  getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SpinnerModel{" +
                "day='" + day + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", closingTime='" + closingTime + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
