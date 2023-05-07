package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpiPaymentStatusRequest implements Parcelable {

    @SerializedName("transaction_id")
    @Expose
    String transaction_id;

    @SerializedName("transaction_no")
    @Expose
    String transaction_no;

    @SerializedName("transaction_type")
    @Expose
    String transaction_type;

    @SerializedName("upi_provider")
    @Expose
    String upi_provider;

    @SerializedName("date_time")
    @Expose
    String date_time;

    @SerializedName("upi_id")
    @Expose
    String upi_id;

    @SerializedName("device_id")
    @Expose
    String device_id;

    @SerializedName("mobile_no")
    @Expose
    String mobile_no;

    @SerializedName("amount_point")
    @Expose
    int amount_point;

    @SerializedName("cust_id")
    @Expose
    int cust_id;

    public UpiPaymentStatusRequest() {
    }

    public UpiPaymentStatusRequest(String transaction_id, int cust_id, String transaction_no,
                                   String transaction_type, int amount_point, String upi_provider,
                                   String date_time, String upi_id, String device_id, String mobile_no) {
        this.transaction_id = transaction_id;
        this.cust_id = cust_id;
        this.transaction_no = transaction_no;
        this.transaction_type = transaction_type;
        this.amount_point = amount_point;
        this.upi_provider = upi_provider;
        this.date_time = date_time;
        this.upi_id = upi_id;
        this.device_id = device_id;
        this.mobile_no = mobile_no;
    }

    protected UpiPaymentStatusRequest(Parcel in) {
        transaction_id = in.readString();
        cust_id = in.readInt();
        transaction_no = in.readString();
        transaction_type = in.readString();
        amount_point = in.readInt();
        upi_provider = in.readString();
        date_time = in.readString();
        upi_id = in.readString();
        device_id = in.readString();
        mobile_no = in.readString();
    }

    public static final Creator<UpiPaymentStatusRequest> CREATOR = new Creator<UpiPaymentStatusRequest>() {
        @Override
        public UpiPaymentStatusRequest createFromParcel(Parcel in) {
            return new UpiPaymentStatusRequest(in);
        }

        @Override
        public UpiPaymentStatusRequest[] newArray(int size) {
            return new UpiPaymentStatusRequest[size];
        }
    };

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getCust_id() {
        return cust_id;
    }

    public void setCust_id(int cust_id) {
        this.cust_id = cust_id;
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getAmount_point() {
        return amount_point;
    }

    public void setAmount_point(int amount_point) {
        this.amount_point = amount_point;
    }

    public String getUpi_provider() {
        return upi_provider;
    }

    public void setUpi_provider(String upi_provider) {
        this.upi_provider = upi_provider;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getUpi_id() {
        return upi_id;
    }

    public void setUpi_id(String upi_id) {
        this.upi_id = upi_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(transaction_id);
        parcel.writeInt(cust_id);
        parcel.writeString(transaction_no);
        parcel.writeString(transaction_type);
        parcel.writeInt(amount_point);
        parcel.writeString(upi_provider);
        parcel.writeString(date_time);
        parcel.writeString(upi_id);
        parcel.writeString(device_id);
        parcel.writeString(mobile_no);
    }
}
