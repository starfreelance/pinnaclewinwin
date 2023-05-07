package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelWithdrawPointsResponse implements Parcelable {

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
    @Expose CancelWithdrawPointsData cancelWithdrawPointsData;

    public CancelWithdrawPointsResponse() {

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

    public CancelWithdrawPointsData getCancelWithdrawPointsData() {
        return cancelWithdrawPointsData;
    }

    public void setCancelWithdrawPointsData(CancelWithdrawPointsData cancelWithdrawPointsData) {
        this.cancelWithdrawPointsData = cancelWithdrawPointsData;
    }

    protected CancelWithdrawPointsResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        cancelWithdrawPointsData = (CancelWithdrawPointsData) in.readValue(CancelWithdrawPointsData.class.getClassLoader());
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
        dest.writeValue(cancelWithdrawPointsData);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CancelWithdrawPointsResponse> CREATOR = new Parcelable.Creator<CancelWithdrawPointsResponse>() {
        @Override
        public CancelWithdrawPointsResponse createFromParcel(Parcel in) {
            return new CancelWithdrawPointsResponse(in);
        }

        @Override
        public CancelWithdrawPointsResponse[] newArray(int size) {
            return new CancelWithdrawPointsResponse[size];
        }
    };
}
