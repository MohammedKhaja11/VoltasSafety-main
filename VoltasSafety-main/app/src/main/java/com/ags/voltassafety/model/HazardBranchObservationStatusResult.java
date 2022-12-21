package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HazardBranchObservationStatusResult implements Serializable {
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("branchObservationStatus")
    @Expose
    private List<BranchObservationstatus> branchObservationStatus = null;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<BranchObservationstatus> getBranchObservationStatus() {
        return branchObservationStatus;
    }

    public void setBranchObservationStatus(List<BranchObservationstatus> branchObservationStatus) {
        this.branchObservationStatus = branchObservationStatus;
    }
}
