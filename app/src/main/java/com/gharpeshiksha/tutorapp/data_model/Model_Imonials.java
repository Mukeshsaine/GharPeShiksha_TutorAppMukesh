package com.gharpeshiksha.tutorapp.data_model;

import com.google.gson.annotations.SerializedName;

public class Model_Imonials {
    @SerializedName("youtube_url")
    private String youtube_url;
    @SerializedName("desc")
    private String desc;
    @SerializedName("video_id")
    private String videoId;
    @SerializedName("imgUrl")
    private String imgUrl;

    public Model_Imonials(String youtube_url, String desc, String videoId, String imgUrl) {
        this.youtube_url = youtube_url;
        this.desc = desc;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getYoutube_url() {
        return youtube_url;
    }

    public void setYoutube_url(String youtube_url) {
        this.youtube_url = youtube_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
