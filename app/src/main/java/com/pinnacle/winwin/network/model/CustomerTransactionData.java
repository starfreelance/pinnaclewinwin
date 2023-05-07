package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerTransactionData implements Parcelable {

    @SerializedName("cust_trxn_id")
    @Expose
    private int custTrxnId;
    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("debit_amount")
    @Expose
    private int debitAmount;
    @SerializedName("credit_amount")
    @Expose
    private int creditAmount;
    @SerializedName("final_amount")
    @Expose
    private int finalAmount;
    @SerializedName("particulars")
    @Expose
    private String particulars;
    @SerializedName("transaction_type")
    @Expose
    private String transactionType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("trxn_date")
    @Expose
    private String trxnDate;

    public CustomerTransactionData() {

    }

    public int getCustTrxnId() {
        return custTrxnId;
    }

    public void setCustTrxnId(int custTrxnId) {
        this.custTrxnId = custTrxnId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(int debitAmount) {
        this.debitAmount = debitAmount;
    }

    public int getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(int creditAmount) {
        this.creditAmount = creditAmount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(int finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTrxnDate() {
        return trxnDate;
    }

    public void setTrxnDate(String trxnDate) {
        this.trxnDate = trxnDate;
    }

    protected CustomerTransactionData(Parcel in) {
        custTrxnId = in.readInt();
        custId = in.readInt();
        debitAmount = in.readInt();
        creditAmount = in.readInt();
        finalAmount = in.readInt();
        particulars = in.readString();
        transactionType = in.readString();
        createdAt = in.readString();
        trxnDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custTrxnId);
        dest.writeInt(custId);
        dest.writeInt(debitAmount);
        dest.writeInt(creditAmount);
        dest.writeInt(finalAmount);
        dest.writeString(particulars);
        dest.writeString(transactionType);
        dest.writeString(createdAt);
        dest.writeString(trxnDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomerTransactionData> CREATOR = new Parcelable.Creator<CustomerTransactionData>() {
        @Override
        public CustomerTransactionData createFromParcel(Parcel in) {
            return new CustomerTransactionData(in);
        }

        @Override
        public CustomerTransactionData[] newArray(int size) {
            return new CustomerTransactionData[size];
        }
    };
}
