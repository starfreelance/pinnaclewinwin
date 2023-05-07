package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AddHTNewBetRequest implements Parcelable {

    @SerializedName("bets")
    @Expose
    private List<HTNewBet> htNewBets = null;

    public AddHTNewBetRequest() {

    }

    public List<HTNewBet> getHtNewBets() {
        return htNewBets;
    }

    public void setHtNewBets(List<HTNewBet> htNewBets) {
        this.htNewBets = htNewBets;
    }

    protected AddHTNewBetRequest(Parcel in) {
        if (in.readByte() == 0x01) {
            htNewBets = new ArrayList<HTNewBet>();
            in.readList(htNewBets, HTNewBet.class.getClassLoader());
        } else {
            htNewBets = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (htNewBets == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(htNewBets);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AddHTNewBetRequest> CREATOR = new Parcelable.Creator<AddHTNewBetRequest>() {
        @Override
        public AddHTNewBetRequest createFromParcel(Parcel in) {
            return new AddHTNewBetRequest(in);
        }

        @Override
        public AddHTNewBetRequest[] newArray(int size) {
            return new AddHTNewBetRequest[size];
        }
    };
}
