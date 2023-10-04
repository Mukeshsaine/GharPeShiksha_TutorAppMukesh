package com.gharpeshiksha.tutorapp.data_model;

import java.util.ArrayList;

public class amanmodel {

    public ArrayList<String> itmes;
    public String category;

    public amanmodel(ArrayList<String> itmes, String category) {
        this.itmes = itmes;
        this.category = category;
    }

    public ArrayList<String> getItmes() {
        return itmes;
    }

    public void setItmes(ArrayList<String> itmes) {
        this.itmes = itmes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
