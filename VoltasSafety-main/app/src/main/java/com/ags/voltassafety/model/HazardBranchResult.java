package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HazardBranchResult implements Serializable {
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("branchObservations")
    @Expose
    private List<BranchObservation> branchObservations = null;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<BranchObservation> getBranchObservations() {
        return branchObservations;
    }

    public void setBranchObservations(List<BranchObservation> branchObservations) {
        this.branchObservations = branchObservations;
    }
}
