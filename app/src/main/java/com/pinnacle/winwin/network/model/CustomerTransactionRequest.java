package com.pinnacle.winwin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerTransactionRequest implements Parcelable {

    @SerializedName("cust_id")
    @Expose
    private int custId;
    @SerializedName("admin_id")
    @Expose
    private int adminId;
    @SerializedName("current_page")
    @Expose
    private int currentPage;
    @SerializedName("data_per_page")
    @Expose
    private int dataPerPage;

    public CustomerTransactionRequest() {

    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getDataPerPage() {
        return dataPerPage;
    }

    public void setDataPerPage(int dataPerPage) {
        this.dataPerPage = dataPerPage;
    }

    protected CustomerTransactionRequest(Parcel in) {
        custId = in.readInt();
        adminId = in.readInt();
        currentPage = in.readInt();
        dataPerPage = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(custId);
        dest.writeInt(adminId);
        dest.writeInt(currentPage);
        dest.writeInt(dataPerPage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomerTransactionRequest> CREATOR = new Parcelable.Creator<CustomerTransactionRequest>() {
        @Override
        public CustomerTransactionRequest createFromParcel(Parcel in) {
            return new CustomerTransactionRequest(in);
        }

        @Override
        public CustomerTransactionRequest[] newArray(int size) {
            return new CustomerTransactionRequest[size];
        }
    };
}
