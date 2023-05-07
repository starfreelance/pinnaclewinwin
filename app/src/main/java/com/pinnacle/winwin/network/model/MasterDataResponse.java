package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MasterDataResponse implements Parcelable {

    @SerializedName("bazaar_details")
    @Expose
    private List<BazaarDetailsResponse> bazaarDetails = null;
    @SerializedName("game_details")
    @Expose
    private List<GameDetailsResponse> gameDetails = null;
    @SerializedName("paana_details")
    @Expose
    private List<PaanaDetailsResponse> paanaDetails = null;
    @SerializedName("cp_paanas_details")
    @Expose
    private List<CPDetailsResponse> cpPaanaDetails = null;
    @SerializedName("motor_comb_details")
    @Expose
    private List<MotorCombDetailsResponse> motorCombDetails = null;
    @SerializedName("chart_details")
    @Expose
    private List<ChartDetailsResponse> chartDetails = null;

    public MasterDataResponse() {

    }

    public List<BazaarDetailsResponse> getBazaarDetails() {
        return bazaarDetails;
    }

    public void setBazaarDetails(List<BazaarDetailsResponse> bazaarDetails) {
        this.bazaarDetails = bazaarDetails;
    }

    public List<GameDetailsResponse> getGameDetails() {
        return gameDetails;
    }

    public void setGameDetails(List<GameDetailsResponse> gameDetails) {
        this.gameDetails = gameDetails;
    }

    public List<PaanaDetailsResponse> getPaanaDetails() {
        return paanaDetails;
    }

    public void setPaanaDetails(List<PaanaDetailsResponse> paanaDetails) {
        this.paanaDetails = paanaDetails;
    }

    public List<CPDetailsResponse> getCpPaanaDetails() {
        return cpPaanaDetails;
    }

    public void setCpPaanaDetails(List<CPDetailsResponse> cpPaanaDetails) {
        this.cpPaanaDetails = cpPaanaDetails;
    }

    public List<MotorCombDetailsResponse> getMotorCombDetails() {
        return motorCombDetails;
    }

    public void setMotorCombDetails(List<MotorCombDetailsResponse> motorCombDetails) {
        this.motorCombDetails = motorCombDetails;
    }

    public List<ChartDetailsResponse> getChartDetails() {
        return chartDetails;
    }

    public void setChartDetails(List<ChartDetailsResponse> chartDetails) {
        this.chartDetails = chartDetails;
    }

    protected MasterDataResponse(Parcel in) {
        if (in.readByte() == 0x01) {
            bazaarDetails = new ArrayList<BazaarDetailsResponse>();
            in.readList(bazaarDetails, BazaarDetailsResponse.class.getClassLoader());
        } else {
            bazaarDetails = null;
        }
        if (in.readByte() == 0x01) {
            gameDetails = new ArrayList<GameDetailsResponse>();
            in.readList(gameDetails, GameDetailsResponse.class.getClassLoader());
        } else {
            gameDetails = null;
        }
        if (in.readByte() == 0x01) {
            paanaDetails = new ArrayList<PaanaDetailsResponse>();
            in.readList(paanaDetails, PaanaDetailsResponse.class.getClassLoader());
        } else {
            paanaDetails = null;
        }
        if (in.readByte() == 0x01) {
            cpPaanaDetails = new ArrayList<CPDetailsResponse>();
            in.readList(cpPaanaDetails, CPDetailsResponse.class.getClassLoader());
        } else {
            cpPaanaDetails = null;
        }
        if (in.readByte() == 0x01) {
            motorCombDetails = new ArrayList<MotorCombDetailsResponse>();
            in.readList(motorCombDetails, MotorCombDetailsResponse.class.getClassLoader());
        } else {
            motorCombDetails = null;
        }
        if (in.readByte() == 0x01) {
            chartDetails = new ArrayList<ChartDetailsResponse>();
            in.readList(chartDetails, ChartDetailsResponse.class.getClassLoader());
        } else {
            chartDetails = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (bazaarDetails == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(bazaarDetails);
        }
        if (gameDetails == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(gameDetails);
        }
        if (paanaDetails == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(paanaDetails);
        }
        if (cpPaanaDetails == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(cpPaanaDetails);
        }
        if (motorCombDetails == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(motorCombDetails);
        }
        if (chartDetails == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(chartDetails);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MasterDataResponse> CREATOR = new Parcelable.Creator<MasterDataResponse>() {
        @Override
        public MasterDataResponse createFromParcel(Parcel in) {
            return new MasterDataResponse(in);
        }

        @Override
        public MasterDataResponse[] newArray(int size) {
            return new MasterDataResponse[size];
        }
    };
}
