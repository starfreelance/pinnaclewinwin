package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "custom_game_amount_details")
public class CustomGameAmountResponse implements Parcelable {

    @ColumnInfo(name = "game_id")
    private int gameId;

    @PrimaryKey
    @ColumnInfo(name = "amount_id")
    @SerializedName("amount_id")
    @Expose
    private int amountId;

    @ColumnInfo(name = "amount_value")
    @SerializedName("amount_value")
    @Expose
    private int amountValue;

    @ColumnInfo(name = "amount_display")
    @SerializedName("amount_display")
    @Expose
    private String amountDisplay;

    public CustomGameAmountResponse() {

    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getAmountId() {
        return amountId;
    }

    public void setAmountId(int amountId) {
        this.amountId = amountId;
    }

    public int getAmountValue() {
        return amountValue;
    }

    public void setAmountValue(int amountValue) {
        this.amountValue = amountValue;
    }

    public String getAmountDisplay() {
        return amountDisplay;
    }

    public void setAmountDisplay(String amountDisplay) {
        this.amountDisplay = amountDisplay;
    }

    protected CustomGameAmountResponse(Parcel in) {
        amountId = in.readInt();
        amountValue = in.readInt();
        amountDisplay = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(amountId);
        dest.writeInt(amountValue);
        dest.writeString(amountDisplay);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomGameAmountResponse> CREATOR = new Parcelable.Creator<CustomGameAmountResponse>() {
        @Override
        public CustomGameAmountResponse createFromParcel(Parcel in) {
            return new CustomGameAmountResponse(in);
        }

        @Override
        public CustomGameAmountResponse[] newArray(int size) {
            return new CustomGameAmountResponse[size];
        }
    };
}
