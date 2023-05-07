package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BetHistoryRequest implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("bazaar_id")
    @Expose
    private int bazaarId;
    @SerializedName("game_id")
    @Expose
    private int gameId;
    @SerializedName("game_date")
    @Expose
    private String gameDate;
    @SerializedName("current_page")
    @Expose
    private int currentPage;

    public BetHistoryRequest() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
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

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    protected BetHistoryRequest(Parcel in) {
        custId = in.readInt();
        bazaarId = in.readInt();
        gameId = in.readInt();
        gameDate = in.readString();
        currentPage = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
        dest.writeInt(bazaarId);
        dest.writeInt(gameId);
        dest.writeString(gameDate);
        dest.writeInt(currentPage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BetHistoryRequest> CREATOR = new Parcelable.Creator<BetHistoryRequest>() {
        @Override
        public BetHistoryRequest createFromParcel(Parcel in) {
            return new BetHistoryRequest(in);
        }

        @Override
        public BetHistoryRequest[] newArray(int size) {
            return new BetHistoryRequest[size];
        }
    };
}
