package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KycRequest implements Parcelable {

    @SerializedName("account_name")
    @Expose
    private String accountName;
    @SerializedName("account_no")
    @Expose
    private String accountNo;
    @SerializedName("account_type")
    @Expose
    private String accountType;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("branch_name")
    @Expose
    private String branchName;
    @SerializedName("ifsc_code")
    @Expose
    private String ifscCode;
    @SerializedName("cust_id")
    @Expose
    private int custId;

    public KycRequest() {

    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    protected KycRequest(Parcel in) {
        accountName = in.readString();
        accountNo = in.readString();
        accountType = in.readString();
        bankName = in.readString();
        branchName = in.readString();
        ifscCode = in.readString();
        custId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountName);
        dest.writeString(accountNo);
        dest.writeString(accountType);
        dest.writeString(bankName);
        dest.writeString(branchName);
        dest.writeString(ifscCode);
        dest.writeInt(custId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<KycRequest> CREATOR = new Parcelable.Creator<KycRequest>() {
        @Override
        public KycRequest createFromParcel(Parcel in) {
            return new KycRequest(in);
        }

        @Override
        public KycRequest[] newArray(int size) {
            return new KycRequest[size];
        }
    };
}
