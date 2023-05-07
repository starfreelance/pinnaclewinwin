package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTInitialResponse implements Parcelable {

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
    private HTInitialData htInitialData;

    public HTInitialResponse() {

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

    public HTInitialData getHtInitialData() {
        return htInitialData;
    }

    public void setHtInitialData(HTInitialData htInitialData) {
        this.htInitialData = htInitialData;
    }

    protected HTInitialResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        htInitialData = (HTInitialData) in.readValue(HTInitialData.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statusCode);
        dest.writeString(statusMessage);
        dest.writeString(message);
        dest.writeValue(htInitialData);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTInitialResponse> CREATOR = new Parcelable.Creator<HTInitialResponse>() {
        @Override
        public HTInitialResponse createFromParcel(Parcel in) {
            return new HTInitialResponse(in);
        }

        @Override
        public HTInitialResponse[] newArray(int size) {
            return new HTInitialResponse[size];
        }
    };
}
