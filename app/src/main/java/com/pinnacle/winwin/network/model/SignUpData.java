package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpData implements Parcelable {

    @SerializedName("user_token")
    @Expose
    private String userToken;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("is_verified")
    @Expose
    private boolean isVerified;

    public SignUpData() {

    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    protected SignUpData(Parcel in) {
        userToken = in.readString();
        mobileNo = in.readString();
        custId = in.readInt();
        isVerified = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userToken);
        dest.writeString(mobileNo);
        dest.writeInt(custId);
        dest.writeByte((byte) (isVerified ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SignUpData> CREATOR = new Parcelable.Creator<SignUpData>() {
        @Override
        public SignUpData createFromParcel(Parcel in) {
            return new SignUpData(in);
        }

        @Override
        public SignUpData[] newArray(int size) {
            return new SignUpData[size];
        }
    };
}
