package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BetHistoryData implements Parcelable {

    @SerializedName("bet_id")
    @Expose
    private int betId;
    @SerializedName("bazaar_id")
    @Expose
    private int bazaarId;
    @SerializedName("game_id")
    @Expose
    private int gameId;
    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("selected_paana")
    @Expose
    private String selectedPaana;
    @SerializedName("total_amount")
    @Expose
    private int totalAmount;
    @SerializedName("amount_per_paana")
    @Expose
    private int amountPerPaana;
    @SerializedName("game_date")
    @Expose
    private String gameDate;
    @SerializedName("paana_type")
    @Expose
    private String paanaType;
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("bet_status")
    @Expose
    private String betStatus;
    @SerializedName("result")
    @Expose
    private String bazaarResult;
    @SerializedName("bazaar_name")
    @Expose
    private String bazaarName;
    @SerializedName("game_name")
    @Expose
    private String gameName;
    @SerializedName("winning_amount")
    @Expose
    private int winningAmount;
    @SerializedName("updated_points")
    @Expose
    private int updatedPoints;
    @SerializedName("booking_time")
    @Expose
    private String bookingTime;

    public BetHistoryData() {

    }

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
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

    public String getSelectedPaana() {
        return selectedPaana;
    }

    public void setSelectedPaana(String selectedPaana) {
        this.selectedPaana = selectedPaana;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getAmountPerPaana() {
        return amountPerPaana;
    }

    public void setAmountPerPaana(int amountPerPaana) {
        this.amountPerPaana = amountPerPaana;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getPaanaType() {
        return paanaType;
    }

    public void setPaanaType(String paanaType) {
        this.paanaType = paanaType;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getBetStatus() {
        return betStatus;
    }

    public void setBetStatus(String betStatus) {
        this.betStatus = betStatus;
    }

    public String getBazaarResult() {
        return bazaarResult;
    }

    public void setBazaarResult(String bazaarResult) {
        this.bazaarResult = bazaarResult;
    }

    public String getBazaarName() {
        return bazaarName;
    }

    public void setBazaarName(String bazaarName) {
        this.bazaarName = bazaarName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(int winningAmount) {
        this.winningAmount = winningAmount;
    }

    public int getUpdatedPoints() {
        return updatedPoints;
    }

    public void setUpdatedPoints(int updatedPoints) {
        this.updatedPoints = updatedPoints;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    protected BetHistoryData(Parcel in) {
        betId = in.readInt();
        bazaarId = in.readInt();
        gameId = in.readInt();
        custId = in.readInt();
        selectedPaana = in.readString();
        totalAmount = in.readInt();
        amountPerPaana = in.readInt();
        gameDate = in.readString();
        paanaType = in.readString();
        status = in.readByte() != 0x00;
        betStatus = in.readString();
        bazaarResult = in.readString();
        bazaarName = in.readString();
        gameName = in.readString();
        winningAmount = in.readInt();
        updatedPoints = in.readInt();
        bookingTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(betId);
        dest.writeInt(bazaarId);
        dest.writeInt(gameId);
        dest.writeInt(custId);
        dest.writeString(selectedPaana);
        dest.writeInt(totalAmount);
        dest.writeInt(amountPerPaana);
        dest.writeString(gameDate);
        dest.writeString(paanaType);
        dest.writeByte((byte) (status ? 0x01 : 0x00));
        dest.writeString(betStatus);
        dest.writeString(bazaarResult);
        dest.writeString(bazaarName);
        dest.writeString(gameName);
        dest.writeInt(winningAmount);
        dest.writeInt(updatedPoints);
        dest.writeString(bookingTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BetHistoryData> CREATOR = new Parcelable.Creator<BetHistoryData>() {
        @Override
        public BetHistoryData createFromParcel(Parcel in) {
            return new BetHistoryData(in);
        }

        @Override
        public BetHistoryData[] newArray(int size) {
            return new BetHistoryData[size];
        }
    };
}
