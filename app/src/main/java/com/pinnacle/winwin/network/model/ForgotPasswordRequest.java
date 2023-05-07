package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRequest implements Parcelable {

    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("otp_value")
    @Expose
    private int otpValue;
    @SerializedName("password")
    @Expose
    private String password;

    public ForgotPasswordRequest() {

    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(int otpValue) {
        this.otpValue = otpValue;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    protected ForgotPasswordRequest(Parcel in) {
        mobileNo = in.readString();
        otpValue = in.readInt();
        password = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobileNo);
        dest.writeInt(otpValue);
        dest.writeString(password);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ForgotPasswordRequest> CREATOR = new Parcelable.Creator<ForgotPasswordRequest>() {
        @Override
        public ForgotPasswordRequest createFromParcel(Parcel in) {
            return new ForgotPasswordRequest(in);
        }

        @Override
        public ForgotPasswordRequest[] newArray(int size) {
            return new ForgotPasswordRequest[size];
        }
    };
}
