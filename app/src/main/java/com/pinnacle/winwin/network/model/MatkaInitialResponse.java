package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatkaInitialResponse implements Parcelable {

    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
//    @SerializedName("message")
    @SerializedName("data")
    @Expose
    private MatkaInitialData matkaInitialData;

    public MatkaInitialResponse() {

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

    public MatkaInitialData getMatkaInitialData() {
        return matkaInitialData;
    }

    public void setMatkaInitialData(MatkaInitialData matkaInitialData) {
        this.matkaInitialData = matkaInitialData;
    }

    protected MatkaInitialResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        matkaInitialData = (MatkaInitialData) in.readValue(MatkaInitialData.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statusCode);
        dest.writeString(statusMessage);
        dest.writeValue(matkaInitialData);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MatkaInitialResponse> CREATOR = new Parcelable.Creator<MatkaInitialResponse>() {
        @Override
        public MatkaInitialResponse createFromParcel(Parcel in) {
            return new MatkaInitialResponse(in);
        }

        @Override
        public MatkaInitialResponse[] newArray(int size) {
            return new MatkaInitialResponse[size];
        }
    };
}
