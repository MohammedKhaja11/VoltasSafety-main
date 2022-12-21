package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SiteEngineerForEmployeeResult implements Serializable {
    @SerializedName("employe_Number")
    @Expose
    private String employeNumber;
    @SerializedName("employe_Name")
    @Expose
    private String employeName;
    @SerializedName("mobile_Number")
    @Expose
    private String mobileNumber;
    @SerializedName("emailID")
    @Expose
    private String emailID;

    public String getEmployeNumber() {
        return employeNumber;
    }

    public void setEmployeNumber(String employeNumber) {
        this.employeNumber = employeNumber;
    }

    public String getEmployeName() {
        return employeName;
    }

    public void setEmployeName(String employeName) {
        this.employeName = employeName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }
}
