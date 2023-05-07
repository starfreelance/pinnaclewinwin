package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BetPaanaData implements Parcelable {

    @SerializedName("selected_paana")
    @Expose
    private String selectedPaana;
    @SerializedName("amount_per_paana")
    @Expose
    private int amountPerPaana;
    @SerializedName("total_amount")
    @Expose
    private int totalAmount;
    @SerializedName("paana_type")
    @Expose
    private String paanaType;

    public BetPaanaData() {

    }

    public String getSelectedPaana() {
        return selectedPaana;
    }

    public void setSelectedPaana(String selectedPaana) {
        this.selectedPaana = selectedPaana;
    }

    public int getAmountPerPaana() {
        return amountPerPaana;
    }

    public void setAmountPerPaana(int amountPerPaana) {
        this.amountPerPaana = amountPerPaana;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaanaType() {
        return paanaType;
    }

    public void setPaanaType(String paanaType) {
        this.paanaType = paanaType;
    }

    protected BetPaanaData(Parcel in) {
        selectedPaana = in.readString();
        amountPerPaana = in.readInt();
        totalAmount = in.readInt();
        paanaType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(selectedPaana);
        dest.writeInt(amountPerPaana);
        dest.writeInt(totalAmount);
        dest.writeString(paanaType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BetPaanaData> CREATOR = new Parcelable.Creator<BetPaanaData>() {
        @Override
        public BetPaanaData createFromParcel(Parcel in) {
            return new BetPaanaData(in);
        }

        @Override
        public BetPaanaData[] newArray(int size) {
            return new BetPaanaData[size];
        }
    };
}
