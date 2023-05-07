package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaazaarHistoryData implements Parcelable {

    @SerializedName("result_date")
    @Expose
    private String resultDate;
    @SerializedName("KO_KC")
    @Expose
    private String kOkC;
    @SerializedName("MO_MC")
    @Expose
    private String mOmC;
    @SerializedName("TO_TC")
    @Expose
    private String tOTC;
    @SerializedName("MDO_MDC")
    @Expose
    private String mDOMDC;
    @SerializedName("MNO_MNC")
    @Expose
    private String mNOMNC;


    public BaazaarHistoryData() {

    }

    public String getResultDate() {
        return resultDate;
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    public String getkOkC() {
        return kOkC;
    }

    public void setkOkC(String kOkC) {
        this.kOkC = kOkC;
    }

    public String getmOmC() {
        return mOmC;
    }

    public void setmOmC(String mOmC) {
        this.mOmC = mOmC;
    }

    public String gettOTC() {
        return tOTC;
    }

    public void settOTC(String tOTC) {
        this.tOTC = tOTC;
    }

    public String getmDOMDC() {
        return mDOMDC;
    }

    public void setmDOMDC(String mDOMDC) {
        this.mDOMDC = mDOMDC;
    }

    public String getmNOMNC() {
        return mNOMNC;
    }

    public void setmNOMNC(String mNOMNC) {
        this.mNOMNC = mNOMNC;
    }

    protected BaazaarHistoryData(Parcel in) {
        resultDate = in.readString();
        kOkC = in.readString();
        mOmC = in.readString();
        tOTC = in.readString();
        mDOMDC = in.readString();
        mNOMNC = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resultDate);
        dest.writeString(kOkC);
        dest.writeString(mOmC);
        dest.writeString(tOTC);
        dest.writeString(mDOMDC);
        dest.writeString(mNOMNC);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BaazaarHistoryData> CREATOR = new Parcelable.Creator<BaazaarHistoryData>() {
        @Override
        public BaazaarHistoryData createFromParcel(Parcel in) {
            return new BaazaarHistoryData(in);
        }

        @Override
        public BaazaarHistoryData[] newArray(int size) {
            return new BaazaarHistoryData[size];
        }
    };
}
