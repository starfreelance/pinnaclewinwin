package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTLastResultResponse implements Parcelable {

    @SerializedName("bet_type")
    @Expose
    private String betType;

    public HTLastResultResponse() {

    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType;
    }

    protected HTLastResultResponse(Parcel in) {
        betType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(betType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTLastResultResponse> CREATOR = new Parcelable.Creator<HTLastResultResponse>() {
        @Override
        public HTLastResultResponse createFromParcel(Parcel in) {
            return new HTLastResultResponse(in);
        }

        @Override
        public HTLastResultResponse[] newArray(int size) {
            return new HTLastResultResponse[size];
        }
    };
}
