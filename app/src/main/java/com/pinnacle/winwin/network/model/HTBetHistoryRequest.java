package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTBetHistoryRequest implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("data_per_page")
    @Expose
    private int dataPerPage;
    @SerializedName("current_page")
    @Expose
    private int currentPage;

    public HTBetHistoryRequest() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getDataPerPage() {
        return dataPerPage;
    }

    public void setDataPerPage(int dataPerPage) {
        this.dataPerPage = dataPerPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    protected HTBetHistoryRequest(Parcel in) {
        custId = in.readInt();
        dataPerPage = in.readInt();
        currentPage = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
        dest.writeInt(dataPerPage);
        dest.writeInt(currentPage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTBetHistoryRequest> CREATOR = new Parcelable.Creator<HTBetHistoryRequest>() {
        @Override
        public HTBetHistoryRequest createFromParcel(Parcel in) {
            return new HTBetHistoryRequest(in);
        }

        @Override
        public HTBetHistoryRequest[] newArray(int size) {
            return new HTBetHistoryRequest[size];
        }
    };
}
