package com.pinnacle.winwin.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IfscResponse {

    @SerializedName("RTGS")
    @Expose
    private boolean rtgs;
    @SerializedName("ADDRESS")
    @Expose
    private String address;
    @SerializedName("IMPS")
    @Expose
    private boolean imps;
    @SerializedName("DISTRICT")
    @Expose
    private String district;
    @SerializedName("CONTACT")
    @Expose
    private String contact;
    @SerializedName("MICR")
    @Expose
    private String micr;
    @SerializedName("CITY")
    @Expose
    private String city;
    @SerializedName("UPI")
    @Expose
    private boolean upi;
    @SerializedName("NEFT")
    @Expose
    private boolean neft;
    @SerializedName("BRANCH")
    @Expose
    private String branch;
    @SerializedName("CENTRE")
    @Expose
    private String centre;
    @SerializedName("STATE")
    @Expose
    private String state;
    @SerializedName("SWIFT")
    @Expose
    private String swift;
    @SerializedName("BANK")
    @Expose
    private String bank;
    @SerializedName("BANKCODE")
    @Expose
    private String bankcode;
    @SerializedName("IFSC")
    @Expose
    private String ifsc;

    public IfscResponse() {

    }

    public boolean isRtgs() {
        return rtgs;
    }

    public void setRtgs(boolean rtgs) {
        this.rtgs = rtgs;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isImps() {
        return imps;
    }

    public void setImps(boolean imps) {
        this.imps = imps;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMicr() {
        return micr;
    }

    public void setMicr(String micr) {
        this.micr = micr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isUpi() {
        return upi;
    }

    public void setUpi(boolean upi) {
        this.upi = upi;
    }

    public boolean isNeft() {
        return neft;
    }

    public void setNeft(boolean neft) {
        this.neft = neft;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }
}
