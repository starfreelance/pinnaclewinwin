package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordRequest implements Parcelable {

    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("old_password")
    @Expose
    private String oldPassword;
    @SerializedName("new_password")
    @Expose
    private String newPassword;

    public ChangePasswordRequest() {

    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    protected ChangePasswordRequest(Parcel in) {
        mobileNo = in.readString();
        oldPassword = in.readString();
        newPassword = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobileNo);
        dest.writeString(oldPassword);
        dest.writeString(newPassword);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChangePasswordRequest> CREATOR = new Parcelable.Creator<ChangePasswordRequest>() {
        @Override
        public ChangePasswordRequest createFromParcel(Parcel in) {
            return new ChangePasswordRequest(in);
        }

        @Override
        public ChangePasswordRequest[] newArray(int size) {
            return new ChangePasswordRequest[size];
        }
    };
}
