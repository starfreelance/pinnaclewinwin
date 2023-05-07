package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RechargeHistoryResponse implements Parcelable {

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
    private RechargeHistoryDataResponse rechargeHistoryDataResponse;

    public RechargeHistoryResponse() {

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

    public RechargeHistoryDataResponse getRechargeHistoryDataResponse() {
        return rechargeHistoryDataResponse;
    }

    public void setRechargeHistoryDataResponse(RechargeHistoryDataResponse rechargeHistoryDataResponse) {
        this.rechargeHistoryDataResponse = rechargeHistoryDataResponse;
    }

    protected RechargeHistoryResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        rechargeHistoryDataResponse = (RechargeHistoryDataResponse) in.readValue(RechargeHistoryDataResponse.class.getClassLoader());
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
        dest.writeValue(rechargeHistoryDataResponse);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RechargeHistoryResponse> CREATOR = new Parcelable.Creator<RechargeHistoryResponse>() {
        @Override
        public RechargeHistoryResponse createFromParcel(Parcel in) {
            return new RechargeHistoryResponse(in);
        }

        @Override
        public RechargeHistoryResponse[] newArray(int size) {
            return new RechargeHistoryResponse[size];
        }
    };
}
