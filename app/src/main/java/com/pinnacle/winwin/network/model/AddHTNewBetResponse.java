package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddHTNewBetResponse implements Parcelable {

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
    private HTNewBetData htNewBetData;

    public AddHTNewBetResponse() {

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

    public HTNewBetData getHtNewBetData() {
        return htNewBetData;
    }

    public void setHtNewBetData(HTNewBetData htNewBetData) {
        this.htNewBetData = htNewBetData;
    }

    protected AddHTNewBetResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        htNewBetData = (HTNewBetData) in.readValue(HTNewBetData.class.getClassLoader());
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
        dest.writeValue(htNewBetData);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AddHTNewBetResponse> CREATOR = new Parcelable.Creator<AddHTNewBetResponse>() {
        @Override
        public AddHTNewBetResponse createFromParcel(Parcel in) {
            return new AddHTNewBetResponse(in);
        }

        @Override
        public AddHTNewBetResponse[] newArray(int size) {
            return new AddHTNewBetResponse[size];
        }
    };
}
