package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatkaInitialRequest implements Parcelable {

    @SerializedName("id")
    @Expose
    private int customerId;

    public MatkaInitialRequest() {

    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    protected MatkaInitialRequest(Parcel in) {
        customerId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(customerId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MatkaInitialRequest> CREATOR = new Parcelable.Creator<MatkaInitialRequest>() {
        @Override
        public MatkaInitialRequest createFromParcel(Parcel in) {
            return new MatkaInitialRequest(in);
        }

        @Override
        public MatkaInitialRequest[] newArray(int size) {
            return new MatkaInitialRequest[size];
        }
    };
}
