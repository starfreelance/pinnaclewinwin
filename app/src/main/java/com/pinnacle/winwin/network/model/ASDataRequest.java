package com.pinnacle.winwin.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ASDataRequest {

    @SerializedName("asdata")
    @Expose
    private String asData;

    public ASDataRequest() {

    }

    public String getAsData() {
        return asData;
    }

    public void setAsData(String asData) {
        this.asData = asData;
    }
}
