package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MatkaInitialData implements Parcelable {

    @SerializedName("customer_data")
    @Expose
    private UserData userData;
    @SerializedName("bazaar_details")
    @Expose
    private List<BazaarDetailsResponse> bazaarDetails = null;
    @SerializedName("version")
    @Expose
    private VersionResponse versionResponse;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("app_version")
    @Expose
    /*private AppVersionResponse appVersionResponse;*/
    private String appVersion;
    @SerializedName("custom_games")
    @Expose
    private List<CustomGamesResponse> customGames = null;
    @SerializedName("min_recharge")
    @Expose
    private String minRecharge;
    @SerializedName("max_recharge")
    @Expose
    private String maxRecharge;
//    @SerializedName("in_approval")
//    @Expose
//    private boolean inApproval;

    public MatkaInitialData() {

    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<BazaarDetailsResponse> getBazaarDetails() {
        return bazaarDetails;
    }

    public void setBazaarDetails(List<BazaarDetailsResponse> bazaarDetails) {
        this.bazaarDetails = bazaarDetails;
    }

    public VersionResponse getVersionResponse() {
        return versionResponse;
    }

    public void setVersionResponse(VersionResponse versionResponse) {
        this.versionResponse = versionResponse;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /*public AppVersionResponse getAppVersionResponse() {
        return appVersionResponse;
    }

    public void setAppVersionResponse(AppVersionResponse appVersionResponse) {
        this.appVersionResponse = appVersionResponse;
    }*/

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public List<CustomGamesResponse> getCustomGames() {
        return customGames;
    }

    public void setCustomGames(List<CustomGamesResponse> customGames) {
        this.customGames = customGames;
    }

    public String getMinRecharge() {
        return minRecharge;
    }

    public void setMinRecharge(String minRecharge) {
        this.minRecharge = minRecharge;
    }

    public String getMaxRecharge() {
        return maxRecharge;
    }

    public void setMaxRecharge(String maxRecharge) {
        this.maxRecharge = maxRecharge;
    }

    /*public boolean isInApproval() {
        return inApproval;
    }

    public void setInApproval(boolean inApproval) {
        this.inApproval = inApproval;
    }*/

    protected MatkaInitialData(Parcel in) {
        userData = (UserData) in.readValue(UserData.class.getClassLoader());
        if (in.readByte() == 0x01) {
            bazaarDetails = new ArrayList<BazaarDetailsResponse>();
            in.readList(bazaarDetails, BazaarDetailsResponse.class.getClassLoader());
        } else {
            bazaarDetails = null;
        }
        versionResponse = (VersionResponse) in.readValue(VersionResponse.class.getClassLoader());
        /*appVersionResponse = (AppVersionResponse) in.readValue(AppVersionResponse.class.getClassLoader());*/
        appVersion = in.readString();
        notes = in.readString();
        if (in.readByte() == 0x01) {
            customGames = new ArrayList<CustomGamesResponse>();
            in.readList(customGames, CustomGamesResponse.class.getClassLoader());
        } else {
            customGames = null;
        }
        minRecharge = in.readString();
        maxRecharge = in.readString();
//        inApproval = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userData);
        if (bazaarDetails == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(bazaarDetails);
        }
        dest.writeValue(versionResponse);
        /*dest.writeValue(appVersionResponse);*/
        dest.writeString(appVersion);
        dest.writeString(notes);
        if (customGames == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(customGames);
        }
        dest.writeString(minRecharge);
        dest.writeString(maxRecharge);
//        dest.writeByte((byte) (inApproval ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MatkaInitialData> CREATOR = new Parcelable.Creator<MatkaInitialData>() {
        @Override
        public MatkaInitialData createFromParcel(Parcel in) {
            return new MatkaInitialData(in);
        }

        @Override
        public MatkaInitialData[] newArray(int size) {
            return new MatkaInitialData[size];
        }
    };
}
