package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpResponse implements Parcelable {

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
    private SignUpData signUpData;

    public SignUpResponse() {

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

    public SignUpData getSignUpData() {
        return signUpData;
    }

    public void setSignUpData(SignUpData signUpData) {
        this.signUpData = signUpData;
    }

    protected SignUpResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        signUpData = (SignUpData) in.readValue(SignUpData.class.getClassLoader());
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
        dest.writeValue(signUpData);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SignUpResponse> CREATOR = new Parcelable.Creator<SignUpResponse>() {
        @Override
        public SignUpResponse createFromParcel(Parcel in) {
            return new SignUpResponse(in);
        }

        @Override
        public SignUpResponse[] newArray(int size) {
            return new SignUpResponse[size];
        }
    };
}
