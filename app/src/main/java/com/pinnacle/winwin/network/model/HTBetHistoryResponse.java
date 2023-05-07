package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTBetHistoryResponse implements Parcelable {

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
    HTBetHistoryDataResponse htBetHistoryDataResponse;

    public HTBetHistoryResponse() {

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

    public HTBetHistoryDataResponse getHtBetHistoryDataResponse() {
        return htBetHistoryDataResponse;
    }

    public void setHtBetHistoryDataResponse(HTBetHistoryDataResponse htBetHistoryDataResponse) {
        this.htBetHistoryDataResponse = htBetHistoryDataResponse;
    }

    protected HTBetHistoryResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        htBetHistoryDataResponse = (HTBetHistoryDataResponse) in.readValue(HTBetHistoryDataResponse.class.getClassLoader());
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
        dest.writeValue(htBetHistoryDataResponse);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTBetHistoryResponse> CREATOR = new Parcelable.Creator<HTBetHistoryResponse>() {
        @Override
        public HTBetHistoryResponse createFromParcel(Parcel in) {
            return new HTBetHistoryResponse(in);
        }

        @Override
        public HTBetHistoryResponse[] newArray(int size) {
            return new HTBetHistoryResponse[size];
        }
    };
}
