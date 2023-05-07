package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaazaarHistoryResponse implements Parcelable {

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
    private BaazaarHistoryDataResponse baazaarHistoryDataResponse;

    public BaazaarHistoryResponse() {

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

    public BaazaarHistoryDataResponse getBaazaarHistoryDataResponse() {
        return baazaarHistoryDataResponse;
    }

    public void setBaazaarHistoryDataResponse(BaazaarHistoryDataResponse baazaarHistoryDataResponse) {
        this.baazaarHistoryDataResponse = baazaarHistoryDataResponse;
    }

    protected BaazaarHistoryResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        baazaarHistoryDataResponse = (BaazaarHistoryDataResponse) in.readValue(BaazaarHistoryDataResponse.class.getClassLoader());
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
        dest.writeValue(baazaarHistoryDataResponse);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BaazaarHistoryResponse> CREATOR = new Parcelable.Creator<BaazaarHistoryResponse>() {
        @Override
        public BaazaarHistoryResponse createFromParcel(Parcel in) {
            return new BaazaarHistoryResponse(in);
        }

        @Override
        public BaazaarHistoryResponse[] newArray(int size) {
            return new BaazaarHistoryResponse[size];
        }
    };
}
