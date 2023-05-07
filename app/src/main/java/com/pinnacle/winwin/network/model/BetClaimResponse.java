package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BetClaimResponse implements Parcelable {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private BetHistoryData betHistoryData;

    public BetClaimResponse() {

    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
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

    public BetHistoryData getBetHistoryData() {
        return betHistoryData;
    }

    public void setBetHistoryData(BetHistoryData betHistoryData) {
        this.betHistoryData = betHistoryData;
    }

    protected BetClaimResponse(Parcel in) {
        statusCode = in.readByte() == 0x00 ? null : in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        betHistoryData = (BetHistoryData) in.readValue(BetHistoryData.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (statusCode == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(statusCode);
        }
        dest.writeString(statusMessage);
        dest.writeString(message);
        dest.writeValue(betHistoryData);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BetClaimResponse> CREATOR = new Parcelable.Creator<BetClaimResponse>() {
        @Override
        public BetClaimResponse createFromParcel(Parcel in) {
            return new BetClaimResponse(in);
        }

        @Override
        public BetClaimResponse[] newArray(int size) {
            return new BetClaimResponse[size];
        }
    };
}
