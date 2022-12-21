package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer {
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("sapcustomerId")
    @Expose
    private String sapcustomerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("customerClassification")
    @Expose
    private String customerClassification;
    @SerializedName("address")
    @Expose
    private String address;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSapcustomerId() {
        return sapcustomerId;
    }

    public void setSapcustomerId(String sapcustomerId) {
        this.sapcustomerId = sapcustomerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerClassification() {
        return customerClassification;
    }

    public void setCustomerClassification(String customerClassification) {
        this.customerClassification = customerClassification;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
