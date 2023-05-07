package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTExistingBetResponse implements Parcelable {

    @SerializedName("bet_id")
    @Expose
    private int betId;
    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("total_amount")
    @Expose
    private int totalAmount;
    @SerializedName("bet_type")
    @Expose
    private String betType;
    @SerializedName("game_date")
    @Expose
    private String gameDate;
    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("is_winner")
    @Expose
    private boolean isWinner;
    @SerializedName("winning_amount")
    @Expose
    private int winningAmount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public HTExistingBetResponse() {

    }

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public int getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(int winningAmount) {
        this.winningAmount = winningAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected HTExistingBetResponse(Parcel in) {
        betId = in.readInt();
        custId = in.readInt();
        totalAmount = in.readInt();
        betType = in.readString();
        gameDate = in.readString();
        slot = in.readString();
        isWinner = in.readByte() != 0x00;
        winningAmount = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(betId);
        dest.writeInt(custId);
        dest.writeInt(totalAmount);
        dest.writeString(betType);
        dest.writeString(gameDate);
        dest.writeString(slot);
        dest.writeByte((byte) (isWinner ? 0x01 : 0x00));
        dest.writeInt(winningAmount);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTExistingBetResponse> CREATOR = new Parcelable.Creator<HTExistingBetResponse>() {
        @Override
        public HTExistingBetResponse createFromParcel(Parcel in) {
            return new HTExistingBetResponse(in);
        }

        @Override
        public HTExistingBetResponse[] newArray(int size) {
            return new HTExistingBetResponse[size];
        }
    };
}
