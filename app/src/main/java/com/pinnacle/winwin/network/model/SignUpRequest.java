package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpRequest implements Parcelable {

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("email")
    @Expose
    private String email = "";
    @SerializedName("country_code")
    @Expose
    private String countryCode;

    public SignUpRequest() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    protected SignUpRequest(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        deviceId = in.readString();
        mobileNo = in.readString();
        password = in.readString();
        dob = in.readString();
        email = in.readString();
        countryCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(deviceId);
        dest.writeString(mobileNo);
        dest.writeString(password);
        dest.writeString(dob);
        dest.writeString(email);
        dest.writeString(countryCode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SignUpRequest> CREATOR = new Parcelable.Creator<SignUpRequest>() {
        @Override
        public SignUpRequest createFromParcel(Parcel in) {
            return new SignUpRequest(in);
        }

        @Override
        public SignUpRequest[] newArray(int size) {
            return new SignUpRequest[size];
        }
    };
}
