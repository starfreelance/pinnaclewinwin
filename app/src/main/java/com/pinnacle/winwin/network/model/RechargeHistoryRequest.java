package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RechargeHistoryRequest implements Parcelable {

    @SerializedName("current_page")
    @Expose
    private int currentPage;
    @SerializedName("cust_id")
    @Expose
    private String custId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("mobile_no")
    @Expose
    private String mobNo;

    public RechargeHistoryRequest() {

    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    protected RechargeHistoryRequest(Parcel in) {
        currentPage = in.readInt();
        custId = in.readString();
        fullName = in.readString();
        mobNo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(currentPage);
        dest.writeString(custId);
        dest.writeString(fullName);
        dest.writeString(mobNo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RechargeHistoryRequest> CREATOR = new Parcelable.Creator<RechargeHistoryRequest>() {
        @Override
        public RechargeHistoryRequest createFromParcel(Parcel in) {
            return new RechargeHistoryRequest(in);
        }

        @Override
        public RechargeHistoryRequest[] newArray(int size) {
            return new RechargeHistoryRequest[size];
        }
    };
}
