package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CountryCodeResponse implements Parcelable {

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
    private List<CountryCodeData> countryCodeDataList;

    public CountryCodeResponse() {

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

    public List<CountryCodeData> getCountryCodeDataList() {
        return countryCodeDataList;
    }

    public void setCountryCodeDataList(List<CountryCodeData> countryCodeDataList) {
        this.countryCodeDataList = countryCodeDataList;
    }

    protected CountryCodeResponse(Parcel in) {
        statusCode = in.readInt();
        statusMessage = in.readString();
        message = in.readString();
        if (in.readByte() == 0x01) {
            countryCodeDataList = new ArrayList<CountryCodeData>();
            in.readList(countryCodeDataList, CountryCodeData.class.getClassLoader());
        } else {
            countryCodeDataList = null;
        }
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
        if (countryCodeDataList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(countryCodeDataList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CountryCodeResponse> CREATOR = new Parcelable.Creator<CountryCodeResponse>() {
        @Override
        public CountryCodeResponse createFromParcel(Parcel in) {
            return new CountryCodeResponse(in);
        }

        @Override
        public CountryCodeResponse[] newArray(int size) {
            return new CountryCodeResponse[size];
        }
    };
}
