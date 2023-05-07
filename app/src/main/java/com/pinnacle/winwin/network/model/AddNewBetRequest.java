package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AddNewBetRequest implements Parcelable {

    @SerializedName("bazaar_id")
    @Expose
    private int bazaarId;
    @SerializedName("game_id")
    @Expose
    private int gameId;
    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("game_date")
    @Expose
    private String gameDate;
    @SerializedName("paanas")
    @Expose
    private List<BetPaanaData> betPaanaDataList = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("created_by")
    @Expose
    private int createdBy;
    @SerializedName("updated_by")
    @Expose
    private int updatedBy;

    public AddNewBetRequest() {

    }

    public int getBazaarId() {
        return bazaarId;
    }

    public void setBazaarId(int bazaarId) {
        this.bazaarId = bazaarId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public List<BetPaanaData> getBetPaanaDataList() {
        return betPaanaDataList;
    }

    public void setBetPaanaDataList(List<BetPaanaData> betPaanaDataList) {
        this.betPaanaDataList = betPaanaDataList;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    protected AddNewBetRequest(Parcel in) {
        bazaarId = in.readInt();
        gameId = in.readInt();
        custId = in.readInt();
        gameDate = in.readString();
        if (in.readByte() == 0x01) {
            betPaanaDataList = new ArrayList<BetPaanaData>();
            in.readList(betPaanaDataList, BetPaanaData.class.getClassLoader());
        } else {
            betPaanaDataList = null;
        }
        byte statusVal = in.readByte();
        status = statusVal == 0x02 ? null : statusVal != 0x00;
        createdBy = in.readInt();
        updatedBy = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bazaarId);
        dest.writeInt(gameId);
        dest.writeInt(custId);
        dest.writeString(gameDate);
        if (betPaanaDataList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(betPaanaDataList);
        }
        if (status == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (status ? 0x01 : 0x00));
        }
        dest.writeInt(createdBy);
        dest.writeInt(updatedBy);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AddNewBetRequest> CREATOR = new Parcelable.Creator<AddNewBetRequest>() {
        @Override
        public AddNewBetRequest createFromParcel(Parcel in) {
            return new AddNewBetRequest(in);
        }

        @Override
        public AddNewBetRequest[] newArray(int size) {
            return new AddNewBetRequest[size];
        }
    };
}
