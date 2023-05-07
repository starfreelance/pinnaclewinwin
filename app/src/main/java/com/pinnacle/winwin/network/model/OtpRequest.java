package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpRequest implements Parcelable {

    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("otp_value")
    @Expose
    private int otpValue;

    public OtpRequest() {

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

    protected OtpRequest(Parcel in) {
        mobileNo = in.readString();
        otpValue = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobileNo);
        dest.writeInt(otpValue);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OtpRequest> CREATOR = new Parcelable.Creator<OtpRequest>() {
        @Override
        public OtpRequest createFromParcel(Parcel in) {
            return new OtpRequest(in);
        }

        @Override
        public OtpRequest[] newArray(int size) {
            return new OtpRequest[size];
        }
    };
}
