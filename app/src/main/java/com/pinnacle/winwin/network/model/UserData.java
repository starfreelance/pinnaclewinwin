package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData implements Parcelable {

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("points")
    @Expose
    private int points;
    @SerializedName("is_first_time")
    @Expose
    private boolean isFirstTime;
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("user_token")
    @Expose
    private String userToken;
    @SerializedName("admin_id")
    @Expose
    private int adminId;
    @SerializedName("admin_name")
    @Expose
    private String adminName;
    @SerializedName("admin_mobile_no")
    @Expose
    private String adminMobileNo;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("is_password_changed")
    @Expose
    private boolean isPasswordChanged;
    @SerializedName("bonus")
    @Expose
    private int bonus;
    @SerializedName("is_verified")
    @Expose
    private boolean isVerified;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("is_blocked")
    @Expose
    private boolean isBlocked;
    @SerializedName("earned_amount")
    @Expose
    private int earnedAmount;
    @SerializedName("joined_date")
    @Expose
    private String joinedDate;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("account_no")
    @Expose
    private String accountNumber;
    @SerializedName("ifsc_code")
    @Expose
    private String ifscCode;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("account_name")
    @Expose
    private String accountName;
    @SerializedName("account_type")
    @Expose
    private String accountType;
    @SerializedName("branch_name")
    @Expose
    private String branchName;
    @SerializedName("is_kyc_completed")
    @Expose
    private boolean isKycCompleted;

    public UserData() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean getIsFirstTime() {
        return isFirstTime;
    }

    public void setIsFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminMobileNo() {
        return adminMobileNo;
    }

    public void setAdminMobileNo(String adminMobileNo) {
        this.adminMobileNo = adminMobileNo;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isPasswordChanged() {
        return isPasswordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        isPasswordChanged = passwordChanged;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public int getEarnedAmount() {
        return earnedAmount;
    }

    public void setEarnedAmount(int earnedAmount) {
        this.earnedAmount = earnedAmount;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public boolean isKycCompleted() {
        return isKycCompleted;
    }

    public void setKycCompleted(boolean kycCompleted) {
        isKycCompleted = kycCompleted;
    }

    protected UserData(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        mobileNo = in.readString();
        points = in.readInt();
        isFirstTime = in.readByte() != 0x00;
        status = in.readByte() != 0x00;
        custId = in.readInt();
        userToken = in.readString();
        adminId = in.readInt();
        adminName = in.readString();
        adminMobileNo = in.readString();
        profileImage = in.readString();
        isPasswordChanged = in.readByte() != 0x00;
        bonus = in.readInt();
        isVerified = in.readByte() != 0x00;
        deviceId = in.readString();
        dob = in.readString();
        isBlocked = in.readByte() != 0x00;
        earnedAmount = in.readInt();
        joinedDate = in.readString();
        fullName = in.readString();
        accountNumber = in.readString();
        ifscCode = in.readString();
        bankName = in.readString();
        accountName = in.readString();
        accountType = in.readString();
        branchName = in.readString();
        isKycCompleted = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(mobileNo);
        dest.writeInt(points);
        dest.writeByte((byte) (isFirstTime ? 0x01 : 0x00));
        dest.writeByte((byte) (status ? 0x01 : 0x00));
        dest.writeInt(custId);
        dest.writeString(userToken);
        dest.writeInt(adminId);
        dest.writeString(adminName);
        dest.writeString(adminMobileNo);
        dest.writeString(profileImage);
        dest.writeByte((byte) (isPasswordChanged ? 0x01 : 0x00));
        dest.writeInt(bonus);
        dest.writeByte((byte) (isVerified ? 0x01 : 0x00));
        dest.writeString(deviceId);
        dest.writeString(dob);
        dest.writeByte((byte) (isBlocked ? 0x01 : 0x00));
        dest.writeInt(earnedAmount);
        dest.writeString(joinedDate);
        dest.writeString(fullName);
        dest.writeString(accountNumber);
        dest.writeString(ifscCode);
        dest.writeString(bankName);
        dest.writeString(accountName);
        dest.writeString(accountType);
        dest.writeString(branchName);
        dest.writeByte((byte) (isKycCompleted ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserData> CREATOR = new Parcelable.Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
}
