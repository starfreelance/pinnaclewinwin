package com.pinnacle.winwin.network.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "amount_details")
public class AmountDetailsResponse implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "game_id")
    private int gameId;

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

    public AmountDetailsResponse() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    protected AmountDetailsResponse(Parcel in) {
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
    public static final Parcelable.Creator<AmountDetailsResponse> CREATOR = new Parcelable.Creator<AmountDetailsResponse>() {
        @Override
        public AmountDetailsResponse createFromParcel(Parcel in) {
            return new AmountDetailsResponse(in);
        }

        @Override
        public AmountDetailsResponse[] newArray(int size) {
            return new AmountDetailsResponse[size];
        }
    };
}
