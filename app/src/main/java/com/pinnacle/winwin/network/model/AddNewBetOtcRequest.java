package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AddNewBetOtcRequest implements Parcelable {

    @SerializedName("bets")
    @Expose
    private List<AddNewBetRequest> betRequests = null;

    public AddNewBetOtcRequest() {

    }

    public List<AddNewBetRequest> getBetRequests() {
        return betRequests;
    }

    public void setBetRequests(List<AddNewBetRequest> betRequests) {
        this.betRequests = betRequests;
    }

    protected AddNewBetOtcRequest(Parcel in) {
        if (in.readByte() == 0x01) {
            betRequests = new ArrayList<AddNewBetRequest>();
            in.readList(betRequests, AddNewBetRequest.class.getClassLoader());
        } else {
            betRequests = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (betRequests == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(betRequests);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AddNewBetOtcRequest> CREATOR = new Parcelable.Creator<AddNewBetOtcRequest>() {
        @Override
        public AddNewBetOtcRequest createFromParcel(Parcel in) {
            return new AddNewBetOtcRequest(in);
        }

        @Override
        public AddNewBetOtcRequest[] newArray(int size) {
            return new AddNewBetOtcRequest[size];
        }
    };
}
