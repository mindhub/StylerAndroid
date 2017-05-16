package com.mindbees.stylerapp.UI.Models.NewStylers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 05-04-2017.
 */

public class Result implements Serializable{
    @SerializedName("newStylers")
    @Expose
    private List<NewStyler> newStylers = null;
    @SerializedName("onlineStylers")
    @Expose
    private List<OnlineStyler> onlineStylers = null;

    public List<NewStyler> getNewStylers() {
        return newStylers;
    }

    public void setNewStylers(List<NewStyler> newStylers) {
        this.newStylers = newStylers;
    }

    public List<OnlineStyler> getOnlineStylers() {
        return onlineStylers;
    }

    public void setOnlineStylers(List<OnlineStyler> onlineStylers) {
        this.onlineStylers = onlineStylers;
    }
}
