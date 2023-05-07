package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTInitialRequest implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;

    public HTInitialRequest() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    protected HTInitialRequest(Parcel in) {
        custId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTInitialRequest> CREATOR = new Parcelable.Creator<HTInitialRequest>() {
        @Override
        public HTInitialRequest createFromParcel(Parcel in) {
            return new HTInitialRequest(in);
        }

        @Override
        public HTInitialRequest[] newArray(int size) {
            return new HTInitialRequest[size];
        }
    };
}
