package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpResponse implements Parcelable {

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
    private OtpData otpData;

    public OtpResponse() {

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

    public OtpData getOtpData() {
        return otpData;
    }

    public void setOtpData(OtpData otpData) {
        this.otpData = otpData;
    }

    protected OtpResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        otpData = (OtpData) in.readValue(OtpData.class.getClassLoader());
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
        dest.writeValue(otpData);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OtpResponse> CREATOR = new Parcelable.Creator<OtpResponse>() {
        @Override
        public OtpResponse createFromParcel(Parcel in) {
            return new OtpResponse(in);
        }

        @Override
        public OtpResponse[] newArray(int size) {
            return new OtpResponse[size];
        }
    };
}