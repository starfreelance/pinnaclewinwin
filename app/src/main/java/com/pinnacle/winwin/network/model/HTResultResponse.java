package com.pinnacle.winwin.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTResultResponse {

    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private HTResultData htResultData;

    public HTResultResponse() {

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HTResultData getHtResultData() {
        return htResultData;
    }

    public void setHtResultData(HTResultData htResultData) {
        this.htResultData = htResultData;
    }
}
