package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HTNewBetData implements Parcelable {

    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("points")
    @Expose
    private int points;
    @SerializedName("bets")
    @Expose
    private List<AddHTNewBetData> addHTNewBetDataList;

    public HTNewBetData() {

    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<AddHTNewBetData> getAddHTNewBetDataList() {
        return addHTNewBetDataList;
    }

    public void setAddHTNewBetDataList(List<AddHTNewBetData> addHTNewBetDataList) {
        this.addHTNewBetDataList = addHTNewBetDataList;
    }

    protected HTNewBetData(Parcel in) {
        slot = in.readString();
        points = in.readInt();
        if (in.readByte() == 0x01) {
            addHTNewBetDataList = new ArrayList<AddHTNewBetData>();
            in.readList(addHTNewBetDataList, AddHTNewBetData.class.getClassLoader());
        } else {
            addHTNewBetDataList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(slot);
        dest.writeInt(points);
        if (addHTNewBetDataList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(addHTNewBetDataList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTNewBetData> CREATOR = new Parcelable.Creator<HTNewBetData>() {
        @Override
        public HTNewBetData createFromParcel(Parcel in) {
            return new HTNewBetData(in);
        }

        @Override
        public HTNewBetData[] newArray(int size) {
            return new HTNewBetData[size];
        }
    };
}
