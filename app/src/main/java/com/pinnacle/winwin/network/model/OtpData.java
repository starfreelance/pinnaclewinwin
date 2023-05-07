package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpData implements Parcelable {

    @SerializedName("otp_value")
    @Expose
    private int otpValue;
    @SerializedName("otp_expiration")
    @Expose
    private String otpExpiration;
    @SerializedName("customer")
    @Expose
    private UserData userData;
    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("otp_id")
    @Expose
    private int otpId;
    private String mobileNo;

    public OtpData() {

    }

    public int getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(int otpValue) {
        this.otpValue = otpValue;
    }

    public String getOtpExpiration() {
        return otpExpiration;
    }

    public void setOtpExpiration(String otpExpiration) {
        this.otpExpiration = otpExpiration;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getOtpId() {
        return otpId;
    }

    public void setOtpId(int otpId) {
        this.otpId = otpId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    protected OtpData(Parcel in) {
        otpValue = in.readInt();
        otpExpiration = in.readString();
        userData = (UserData) in.readValue(UserData.class.getClassLoader());
        custId = in.readInt();
        otpId = in.readInt();
        mobileNo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(otpValue);
        dest.writeString(otpExpiration);
        dest.writeValue(userData);
        dest.writeInt(custId);
        dest.writeInt(otpId);
        dest.writeString(mobileNo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OtpData> CREATOR = new Parcelable.Creator<OtpData>() {
        @Override
        public OtpData createFromParcel(Parcel in) {
            return new OtpData(in);
        }

        @Override
        public OtpData[] newArray(int size) {
            return new OtpData[size];
        }
    };
}
