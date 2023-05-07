package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpiPaymentStatusResponse implements Parcelable {
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
    private UpiPaymentStatusData upiPaymentStatusData;

    protected UpiPaymentStatusResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        upiPaymentStatusData = in.readParcelable(UpiPaymentStatusData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statusCode);
        dest.writeString(statusMessage);
        dest.writeString(message);
        dest.writeParcelable(upiPaymentStatusData, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UpiPaymentStatusResponse> CREATOR = new Creator<UpiPaymentStatusResponse>() {
        @Override
        public UpiPaymentStatusResponse createFromParcel(Parcel in) {
            return new UpiPaymentStatusResponse(in);
        }

        @Override
        public UpiPaymentStatusResponse[] newArray(int size) {
            return new UpiPaymentStatusResponse[size];
        }
    };

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

    public UpiPaymentStatusData getUpiPaymentStatusData() {
        return upiPaymentStatusData;
    }

    public void setUpiPaymentStatusData(UpiPaymentStatusData upiPaymentStatusData) {
        this.upiPaymentStatusData = upiPaymentStatusData;
    }
}
