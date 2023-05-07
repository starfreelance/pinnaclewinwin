package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaazaarHistoryRequest implements Parcelable {

    @SerializedName("current_page")
    @Expose
    private int currentPage;
    @SerializedName("data_per_page")
    @Expose
    private int dataPerPage;

    public BaazaarHistoryRequest() {

    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getDataPerPage() {
        return dataPerPage;
    }

    public void setDataPerPage(int dataPerPage) {
        this.dataPerPage = dataPerPage;
    }

    protected BaazaarHistoryRequest(Parcel in) {
        currentPage = in.readInt();
        dataPerPage = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(currentPage);
        dest.writeInt(dataPerPage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BaazaarHistoryRequest> CREATOR = new Parcelable.Creator<BaazaarHistoryRequest>() {
        @Override
        public BaazaarHistoryRequest createFromParcel(Parcel in) {
            return new BaazaarHistoryRequest(in);
        }

        @Override
        public BaazaarHistoryRequest[] newArray(int size) {
            return new BaazaarHistoryRequest[size];
        }
    };
}
