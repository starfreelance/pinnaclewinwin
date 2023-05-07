package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddHTNewBetData implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("points")
    @Expose
    private int points;
    @SerializedName("customer")
    @Expose
    private UserData userData;
    @SerializedName("game_date")
    @Expose
    private String gameDate;
    @SerializedName("total_amount")
    @Expose
    private int totalAmount;
    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("bet_type")
    @Expose
    private String betType;
    @SerializedName("is_winner")
    @Expose
    private boolean isWinner;
    @SerializedName("winning_amount")
    @Expose
    private String winningAmount;
    @SerializedName("bet_id")
    @Expose
    private int betId;

    public AddHTNewBetData() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public String getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(String winningAmount) {
        this.winningAmount = winningAmount;
    }

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
    }

    protected AddHTNewBetData(Parcel in) {
        custId = in.readInt();
        points = in.readInt();
        userData = (UserData) in.readValue(UserData.class.getClassLoader());
        gameDate = in.readString();
        totalAmount = in.readInt();
        slot = in.readString();
        betType = in.readString();
        isWinner = in.readByte() != 0x00;
        winningAmount = in.readString();
        betId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
        dest.writeInt(points);
        dest.writeValue(userData);
        dest.writeString(gameDate);
        dest.writeInt(totalAmount);
        dest.writeString(slot);
        dest.writeString(betType);
        dest.writeByte((byte) (isWinner ? 0x01 : 0x00));
        dest.writeString(winningAmount);
        dest.writeInt(betId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AddHTNewBetData> CREATOR = new Parcelable.Creator<AddHTNewBetData>() {
        @Override
        public AddHTNewBetData createFromParcel(Parcel in) {
            return new AddHTNewBetData(in);
        }

        @Override
        public AddHTNewBetData[] newArray(int size) {
            return new AddHTNewBetData[size];
        }
    };
}
