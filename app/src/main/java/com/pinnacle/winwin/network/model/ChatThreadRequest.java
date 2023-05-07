package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatThreadRequest implements Parcelable {

    @SerializedName("current_page")
    @Expose
    private int currentPage;
    @SerializedName("cust_id")
    @Expose
    private int custId;

    public ChatThreadRequest() {

    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    protected ChatThreadRequest(Parcel in) {
        currentPage = in.readInt();
        custId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(currentPage);
        dest.writeInt(custId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChatThreadRequest> CREATOR = new Parcelable.Creator<ChatThreadRequest>() {
        @Override
        public ChatThreadRequest createFromParcel(Parcel in) {
            return new ChatThreadRequest(in);
        }

        @Override
        public ChatThreadRequest[] newArray(int size) {
            return new ChatThreadRequest[size];
        }
    };
}
