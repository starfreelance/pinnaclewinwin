package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BetHistoryResponse implements Parcelable {

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
    private BetHistoryDataResponse betHistoryDataResponse;

    public BetHistoryResponse() {

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

    public BetHistoryDataResponse getBetHistoryDataResponse() {
        return betHistoryDataResponse;
    }

    public void setBetHistoryDataResponse(BetHistoryDataResponse betHistoryDataResponse) {
        this.betHistoryDataResponse = betHistoryDataResponse;
    }

    protected BetHistoryResponse(Parcel in) {
        statusCode = in.readByte() == 0x00 ? null : in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        betHistoryDataResponse = (BetHistoryDataResponse) in.readValue(BetHistoryDataResponse.class.getClassLoader());
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
        dest.writeValue(betHistoryDataResponse);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BetHistoryResponse> CREATOR = new Parcelable.Creator<BetHistoryResponse>() {
        @Override
        public BetHistoryResponse createFromParcel(Parcel in) {
            return new BetHistoryResponse(in);
        }

        @Override
        public BetHistoryResponse[] newArray(int size) {
            return new BetHistoryResponse[size];
        }
    };
}
