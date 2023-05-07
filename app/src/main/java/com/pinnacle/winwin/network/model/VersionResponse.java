package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionResponse implements Parcelable {

    @SerializedName("db_version_id")
    @Expose
    private int dbVersionId;
    @SerializedName("db_version")
    @Expose
    private String dbVersion;
    @SerializedName("version")
    @Expose
    private String version;

    public VersionResponse() {

    }

    public int getDbVersionId() {
        return dbVersionId;
    }

    public void setDbVersionId(int dbVersionId) {
        this.dbVersionId = dbVersionId;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    protected VersionResponse(Parcel in) {
        dbVersionId = in.readInt();
        dbVersion = in.readString();
        version = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dbVersionId);
        dest.writeString(dbVersion);
        dest.writeString(version);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VersionResponse> CREATOR = new Parcelable.Creator<VersionResponse>() {
        @Override
        public VersionResponse createFromParcel(Parcel in) {
            return new VersionResponse(in);
        }

        @Override
        public VersionResponse[] newArray(int size) {
            return new VersionResponse[size];
        }
    };
}
