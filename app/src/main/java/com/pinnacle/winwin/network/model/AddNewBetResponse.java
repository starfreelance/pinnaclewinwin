package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddNewBetResponse implements Parcelable {

    @SerializedName("data")
    @Expose
    private UserData userData;
    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("message")
    @Expose
    private String message;

    public AddNewBetResponse() {

    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
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

    protected AddNewBetResponse(Parcel in) {
        userData = (UserData) in.readValue(UserData.class.getClassLoader());
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userData);
        dest.writeInt(statusCode);
        dest.writeString(statusMessage);
        dest.writeString(message);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AddNewBetResponse> CREATOR = new Parcelable.Creator<AddNewBetResponse>() {
        @Override
        public AddNewBetResponse createFromParcel(Parcel in) {
            return new AddNewBetResponse(in);
        }

        @Override
        public AddNewBetResponse[] newArray(int size) {
            return new AddNewBetResponse[size];
        }
    };
}
