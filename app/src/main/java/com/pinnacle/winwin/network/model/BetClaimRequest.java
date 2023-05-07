package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BetClaimRequest implements Parcelable {

    @SerializedName("bet_id")
    @Expose
    private int betId;

    public BetClaimRequest() {

    }

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
    }

    protected BetClaimRequest(Parcel in) {
        betId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(betId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BetClaimRequest> CREATOR = new Parcelable.Creator<BetClaimRequest>() {
        @Override
        public BetClaimRequest createFromParcel(Parcel in) {
            return new BetClaimRequest(in);
        }

        @Override
        public BetClaimRequest[] newArray(int size) {
            return new BetClaimRequest[size];
        }
    };
}
