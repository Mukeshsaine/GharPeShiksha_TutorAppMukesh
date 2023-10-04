package com.gharpeshiksha.tutorapp.data_model;

public class TransactionModel {

    String date, points, referedto, referedby;

    public TransactionModel(String date, String points, String referedto, String referedby) {
        this.date = date;
        this.points = points;
        this.referedto = referedto;
        this.referedby = referedby;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getReferedto() {
        return referedto;
    }

    public void setReferedto(String referedto) {
        this.referedto = referedto;
    }

    public String getReferedby() {
        return referedby;
    }

    public void setReferedby(String referedby) {
        this.referedby = referedby;
    }
}
