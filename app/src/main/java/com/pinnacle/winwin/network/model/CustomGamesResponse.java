package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "custom_games")
public class CustomGamesResponse implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "game_id")
    @SerializedName("game_id")
    @Expose
    private int gameId;

    @ColumnInfo(name = "game_name")
    @SerializedName("game_name")
    @Expose
    private String gameName;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    @Expose
    private boolean status;

    @Ignore
    @SerializedName("amounts")
    @Expose
    private List<CustomGameAmountResponse> amountDetails = null;

    private int imgResource;

    public CustomGamesResponse() {

    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<CustomGameAmountResponse> getAmountDetails() {
        return amountDetails;
    }

    public void setAmountDetails(List<CustomGameAmountResponse> amountDetails) {
        this.amountDetails = amountDetails;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    protected CustomGamesResponse(Parcel in) {
        gameId = in.readInt();
        gameName = in.readString();
        status = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            amountDetails = new ArrayList<CustomGameAmountResponse>();
            in.readList(amountDetails, CustomGameAmountResponse.class.getClassLoader());
        } else {
            amountDetails = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gameId);
        dest.writeString(gameName);
        dest.writeByte((byte) (status ? 0x01 : 0x00));
        if (amountDetails == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(amountDetails);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomGamesResponse> CREATOR = new Parcelable.Creator<CustomGamesResponse>() {
        @Override
        public CustomGamesResponse createFromParcel(Parcel in) {
            return new CustomGamesResponse(in);
        }

        @Override
        public CustomGamesResponse[] newArray(int size) {
            return new CustomGamesResponse[size];
        }
    };
}
