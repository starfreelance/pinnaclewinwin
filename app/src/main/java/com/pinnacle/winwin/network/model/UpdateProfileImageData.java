package com.pinnacle.winwin.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileImageData {

    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    public UpdateProfileImageData() {

    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
