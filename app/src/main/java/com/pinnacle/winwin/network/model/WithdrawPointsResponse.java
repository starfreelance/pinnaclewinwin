package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawPointsResponse implements Parcelable {

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
    private WithdrawalPointsData withdrawalPointsData;

    public WithdrawPointsResponse() {

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

    public WithdrawalPointsData getWithdrawalPointsData() {
        return withdrawalPointsData;
    }

    public void setWithdrawalPointsData(WithdrawalPointsData withdrawalPointsData) {
        this.withdrawalPointsData = withdrawalPointsData;
    }

    protected WithdrawPointsResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        withdrawalPointsData = (WithdrawalPointsData) in.readValue(WithdrawalPointsData.class.getClassLoader());
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
        dest.writeValue(withdrawalPointsData);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WithdrawPointsResponse> CREATOR = new Parcelable.Creator<WithdrawPointsResponse>() {
        @Override
        public WithdrawPointsResponse createFromParcel(Parcel in) {
            return new WithdrawPointsResponse(in);
        }

        @Override
        public WithdrawPointsResponse[] newArray(int size) {
            return new WithdrawPointsResponse[size];
        }
    };
}
