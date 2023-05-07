package com.pinnacle.winwin.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileImageResponse {

    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("message")
    @Expose
    private UpdateProfileImageData updateProfileImageData;

    public UpdateProfileImageResponse() {

    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public UpdateProfileImageData getUpdateProfileImageData() {
        return updateProfileImageData;
    }

    public void setUpdateProfileImageData(UpdateProfileImageData updateProfileImageData) {
        this.updateProfileImageData = updateProfileImageData;
    }
}
