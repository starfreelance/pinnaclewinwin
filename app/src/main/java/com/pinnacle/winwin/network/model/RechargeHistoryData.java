package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RechargeHistoryData implements Parcelable {

    @SerializedName("payment_id")
    @Expose
    private int paymentId;
    @SerializedName("bill_id")
    @Expose
    private String billId;
    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("amount_point")
    @Expose
    private int amountPoint;
    @SerializedName("admin_id")
    @Expose
    private int adminId;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("transaction_no")
    @Expose
    private String transactionNo;
    @SerializedName("transaction_type")
    @Expose
    private String transactionType;
    @SerializedName("transaction_details")
    @Expose
    private String transactionDetails;
    @SerializedName("upi_provider")
    @Expose
    private String upiProvider;
    @SerializedName("requested_by")
    @Expose
    private String requestedBy;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("mobile_no")
    @Expose
    private String mobNo;

    public RechargeHistoryData() {

    }

    protected RechargeHistoryData(Parcel in) {
        paymentId = in.readInt();
        billId = in.readString();
        custId = in.readInt();
        amount = in.readInt();
        amountPoint = in.readInt();
        adminId = in.readInt();
        transactionId = in.readString();
        transactionNo = in.readString();
        transactionType = in.readString();
        transactionDetails = in.readString();
        upiProvider = in.readString();
        requestedBy = in.readString();
        status = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        dateTime = in.readString();
        mobNo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(paymentId);
        dest.writeString(billId);
        dest.writeInt(custId);
        dest.writeInt(amount);
        dest.writeInt(amountPoint);
        dest.writeInt(adminId);
        dest.writeString(transactionId);
        dest.writeString(transactionNo);
        dest.writeString(transactionType);
        dest.writeString(transactionDetails);
        dest.writeString(upiProvider);
        dest.writeString(requestedBy);
        dest.writeString(status);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(dateTime);
        dest.writeString(mobNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RechargeHistoryData> CREATOR = new Creator<RechargeHistoryData>() {
        @Override
        public RechargeHistoryData createFromParcel(Parcel in) {
            return new RechargeHistoryData(in);
        }

        @Override
        public RechargeHistoryData[] newArray(int size) {
            return new RechargeHistoryData[size];
        }
    };

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(String transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getAmountPoint() {
        return amountPoint;
    }

    public void setAmountPoint(int amountPoint) {
        this.amountPoint = amountPoint;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getUpiProvider() {
        return upiProvider;
    }

    public void setUpiProvider(String upiProvider) {
        this.upiProvider = upiProvider;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

}
