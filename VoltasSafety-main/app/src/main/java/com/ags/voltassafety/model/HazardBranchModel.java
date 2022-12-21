package com.ags.voltassafety.model;

import java.io.Serializable;

public class HazardBranchModel implements Serializable {

    String startDate;
    String endDate;
    String branch;
    String type;
    String subType;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }



//    public HazardBranchModel(String startDate, String endDate, String branch, String type, String subType) {
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.branch = branch;
//        this.type = type;
//        this.subType = subType;
//    }
}
