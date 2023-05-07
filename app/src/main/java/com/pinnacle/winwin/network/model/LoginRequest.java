package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest implements Parcelable {

    @SerializedName("auth_user_id")
    @Expose
    private String authUserId;
    @SerializedName("auth_user_password")
    @Expose
    private String authUserPassword;
    @SerializedName("device_id")
    @Expose
    private String deviceId;

    public String getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(String authUserId) {
        this.authUserId = authUserId;
    }

    public String getAuthUserPassword() {
        return authUserPassword;
    }

    public void setAuthUserPassword(String authUserPassword) {
        this.authUserPassword = authUserPassword;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public LoginRequest() {

    }

    protected LoginRequest(Parcel in) {
        authUserId = in.readString();
        authUserPassword = in.readString();
        deviceId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authUserId);
        dest.writeString(authUserPassword);
        dest.writeString(deviceId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LoginRequest> CREATOR = new Parcelable.Creator<LoginRequest>() {
        @Override
        public LoginRequest createFromParcel(Parcel in) {
            return new LoginRequest(in);
        }

        @Override
        public LoginRequest[] newArray(int size) {
            return new LoginRequest[size];
        }
    };
}
