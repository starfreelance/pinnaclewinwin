package com.pinnacle.winwin.ui.home.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectGameModel implements Parcelable {

    private int gameType;
    private int imgResource;
    private String gameName;

    public SelectGameModel() {

    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    protected SelectGameModel(Parcel in) {
        gameType = in.readInt();
        imgResource = in.readInt();
        gameName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gameType);
        dest.writeInt(imgResource);
        dest.writeString(gameName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SelectGameModel> CREATOR = new Parcelable.Creator<SelectGameModel>() {
        @Override
        public SelectGameModel createFromParcel(Parcel in) {
            return new SelectGameModel(in);
        }

        @Override
        public SelectGameModel[] newArray(int size) {
            return new SelectGameModel[size];
        }
    };
}