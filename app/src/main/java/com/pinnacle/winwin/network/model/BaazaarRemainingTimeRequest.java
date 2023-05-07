package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaazaarRemainingTimeRequest implements Parcelable {

    @SerializedName("id")
    @Expose
    private int baazaarId;

    public BaazaarRemainingTimeRequest() {

    }

    public int getBaazaarId() {
        return baazaarId;
    }

    public void setBaazaarId(int baazaarId) {
        this.baazaarId = baazaarId;
    }

    protected BaazaarRemainingTimeRequest(Parcel in) {
        baazaarId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(baazaarId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BaazaarRemainingTimeRequest> CREATOR = new Parcelable.Creator<BaazaarRemainingTimeRequest>() {
        @Override
        public BaazaarRemainingTimeRequest createFromParcel(Parcel in) {
            return new BaazaarRemainingTimeRequest(in);
        }

        @Override
        public BaazaarRemainingTimeRequest[] newArray(int size) {
            return new BaazaarRemainingTimeRequest[size];
        }
    };
}
