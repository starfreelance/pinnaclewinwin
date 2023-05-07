package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryCodeData implements Parcelable {

    @SerializedName("country_code_id")
    @Expose
    private int countryCodeId;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("country_name")
    @Expose
    private String countryName;

    public CountryCodeData() {

    }

    public int getCountryCodeId() {
        return countryCodeId;
    }

    public void setCountryCodeId(int countryCodeId) {
        this.countryCodeId = countryCodeId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    protected CountryCodeData(Parcel in) {
        countryCodeId = in.readInt();
        countryCode = in.readString();
        countryName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(countryCodeId);
        dest.writeString(countryCode);
        dest.writeString(countryName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CountryCodeData> CREATOR = new Parcelable.Creator<CountryCodeData>() {
        @Override
        public CountryCodeData createFromParcel(Parcel in) {
            return new CountryCodeData(in);
        }

        @Override
        public CountryCodeData[] newArray(int size) {
            return new CountryCodeData[size];
        }
    };
}
