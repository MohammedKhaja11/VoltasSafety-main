package com.ags.voltassafety.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RegisterInputModel {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("pfno")
    @Expose
    private String pfno;
    @SerializedName("empName")
    @Expose
    private String empName;
    @SerializedName("verticalId")
    @Expose
    private String verticalId;
    @SerializedName("zoneId")
    @Expose
    private String zoneId;
    @SerializedName("branchId")
    @Expose
    private String branchId;
    @SerializedName("projectName")
    @Expose
    private String projectName;
    @SerializedName("emailID")
    @Expose
    private String emailID;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("projectInchargeName")
    @Expose
    private String projectInchargeName;
    @SerializedName("projectInchargeEmail")
    @Expose
    private String projectInchargeEmail;
    @SerializedName("projectInchargeMobileNo")
    @Expose
    private String projectInchargeMobileNo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPfno() {
        return pfno;
    }

    public void setPfno(String pfno) {
        this.pfno = pfno;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getVerticalId() {
        return verticalId;
    }

    public void setVerticalId(String verticalId) {
        this.verticalId = verticalId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getProjectInchargeName() {
        return projectInchargeName;
    }

    public void setProjectInchargeName(String projectInchargeName) {
        this.projectInchargeName = projectInchargeName;
    }

    public String getProjectInchargeEmail() {
        return projectInchargeEmail;
    }

    public void setProjectInchargeEmail(String projectInchargeEmail) {
        this.projectInchargeEmail = projectInchargeEmail;
    }

    public String getProjectInchargeMobileNo() {
        return projectInchargeMobileNo;
    }

    public void setProjectInchargeMobileNo(String projectInchargeMobileNo) {
        this.projectInchargeMobileNo = projectInchargeMobileNo;
    }

}
