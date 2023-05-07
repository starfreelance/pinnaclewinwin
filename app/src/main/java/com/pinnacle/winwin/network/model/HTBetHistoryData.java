package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HTBetHistoryData implements Parcelable {

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
    private String winningAmount;
    @SerializedName("booking_time")
    @Expose
    private String bookingTime;
    @SerializedName("result")
    @Expose
    private String result;

    public HTBetHistoryData() {

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

    public boolean getWinner() {
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

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    protected HTBetHistoryData(Parcel in) {
        betId = in.readInt();
        custId = in.readInt();
        totalAmount = in.readInt();
        betType = in.readString();
        gameDate = in.readString();
        slot = in.readString();
        isWinner = in.readByte() != 0x00;
        winningAmount = in.readString();
        bookingTime = in.readString();
        result = in.readString();
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
        dest.writeString(winningAmount);
        dest.writeString(bookingTime);
        dest.writeString(result);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HTBetHistoryData> CREATOR = new Parcelable.Creator<HTBetHistoryData>() {
        @Override
        public HTBetHistoryData createFromParcel(Parcel in) {
            return new HTBetHistoryData(in);
        }

        @Override
        public HTBetHistoryData[] newArray(int size) {
            return new HTBetHistoryData[size];
        }
    };
}
