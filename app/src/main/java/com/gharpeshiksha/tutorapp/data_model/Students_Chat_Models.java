package com.gharpeshiksha.tutorapp.data_model;

import com.google.gson.annotations.SerializedName;

public class Students_Chat_Models{

    public static String SENT_BY_ME = "Tutor";
    public static String SENT_BY_BOT = "bot";
    private boolean isSended = true;

    @SerializedName("message")
    private String message;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("sendBy")
    private String sendBy;
    @SerializedName("isApproved")
    private String isApproved;

    public Students_Chat_Models(String message, String timestamp, String sendBy, boolean isSended, String isApproved) {
        this.message = message;
        this.timestamp = timestamp;
        this.sendBy = sendBy;
        this.isSended = isSended;
        this.isApproved = isApproved;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public static String getSentByMe() {
        return SENT_BY_ME;
    }

    public static void setSentByMe(String sentByMe) {
        SENT_BY_ME = sentByMe;
    }

    public static String getSentByBot() {
        return SENT_BY_BOT;
    }

    public static void setSentByBot(String sentByBot) {
        SENT_BY_BOT = sentByBot;
    }

    public boolean isSended() {
        return isSended;
    }

    public void setSended(boolean sended) {
        isSended = sended;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    @Override
    public String toString() {
        return "Students_Chat_Models{" +
                "message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sendBy='" + sendBy + '\'' +
                '}';
    }
}
