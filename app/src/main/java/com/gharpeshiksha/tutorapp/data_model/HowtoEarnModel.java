package com.gharpeshiksha.tutorapp.data_model;

public class HowtoEarnModel {
    String title, subTitle, description, buttonName, buttonAction, status;

    public HowtoEarnModel(String title, String subTitle, String description, String buttonName, String buttonAction, String status) {
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.buttonName = buttonName;
        this.buttonAction = buttonAction;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonAction() {
        return buttonAction;
    }

    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
