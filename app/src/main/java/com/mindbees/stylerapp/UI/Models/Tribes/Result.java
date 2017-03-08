package com.mindbees.stylerapp.UI.Models.Tribes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 08-03-2017.
 */

public class Result implements Serializable{

    @SerializedName("tribe_id")
    @Expose
    private String tribeId;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("tribe_title")
    @Expose
    private String tribeTitle;
    @SerializedName("tribe_img")
    @Expose
    private String tribeImg;
    @SerializedName("tribe_status")
    @Expose
    private String tribeStatus;

    public String getTribeId() {
        return tribeId;
    }

    public void setTribeId(String tribeId) {
        this.tribeId = tribeId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTribeTitle() {
        return tribeTitle;
    }

    public void setTribeTitle(String tribeTitle) {
        this.tribeTitle = tribeTitle;
    }

    public String getTribeImg() {
        return tribeImg;
    }

    public void setTribeImg(String tribeImg) {
        this.tribeImg = tribeImg;
    }

    public String getTribeStatus() {
        return tribeStatus;
    }

    public void setTribeStatus(String tribeStatus) {
        this.tribeStatus = tribeStatus;
    }
}
