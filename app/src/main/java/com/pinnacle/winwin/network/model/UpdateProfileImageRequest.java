package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileImageRequest implements Parcelable {

    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("cust_id")
    @Expose
    private int custId;

    public UpdateProfileImageRequest() {

    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    protected UpdateProfileImageRequest(Parcel in) {
        profileImage = in.readString();
        custId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profileImage);
        dest.writeInt(custId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UpdateProfileImageRequest> CREATOR = new Parcelable.Creator<UpdateProfileImageRequest>() {
        @Override
        public UpdateProfileImageRequest createFromParcel(Parcel in) {
            return new UpdateProfileImageRequest(in);
        }

        @Override
        public UpdateProfileImageRequest[] newArray(int size) {
            return new UpdateProfileImageRequest[size];
        }
    };
}
