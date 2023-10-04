package com.gharpeshiksha.tutorapp.data_model;

public class NotesModel {

    private String Img;
    private String Subject;

    public NotesModel(String img, String subject) {
        Img = img;
        Subject = subject;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }
}
