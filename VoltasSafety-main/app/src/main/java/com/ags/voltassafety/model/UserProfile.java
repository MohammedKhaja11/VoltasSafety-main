package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfile {
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("employeeCode")
    @Expose
    private String employeeCode;
    @SerializedName("eMail")
    @Expose
    private String eMail;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("branchNames")
    @Expose
    private List<String> branchNames = null;
    @SerializedName("roleNames")
    @Expose
    private List<String> roleNames = null;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public List<String> getBranchNames() {
        return branchNames;
    }

    public void setBranchNames(List<String> branchNames) {
        this.branchNames = branchNames;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }
}
