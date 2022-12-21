package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EscalationResult implements Serializable {
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("open")
    @Expose
    private Integer open;
    @SerializedName("assigned")
    @Expose
    private Integer assigned;
    @SerializedName("inTimeAssigned")
    @Expose
    private Integer inTimeAssigned;
    @SerializedName("outTimeAssigned")
    @Expose
    private Integer outTimeAssigned;
    @SerializedName("closed")
    @Expose
    private Integer closed;
    @SerializedName("inTimeClosed")
    @Expose
    private Integer inTimeClosed;
    @SerializedName("outTimeClosed")
    @Expose
    private Integer outTimeClosed;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public Integer getAssigned() {
        return assigned;
    }

    public void setAssigned(Integer assigned) {
        this.assigned = assigned;
    }

    public Integer getInTimeAssigned() {
        return inTimeAssigned;
    }

    public void setInTimeAssigned(Integer inTimeAssigned) {
        this.inTimeAssigned = inTimeAssigned;
    }

    public Integer getOutTimeAssigned() {
        return outTimeAssigned;
    }

    public void setOutTimeAssigned(Integer outTimeAssigned) {
        this.outTimeAssigned = outTimeAssigned;
    }

    public Integer getClosed() {
        return closed;
    }

    public void setClosed(Integer closed) {
        this.closed = closed;
    }

    public Integer getInTimeClosed() {
        return inTimeClosed;
    }

    public void setInTimeClosed(Integer inTimeClosed) {
        this.inTimeClosed = inTimeClosed;
    }

    public Integer getOutTimeClosed() {
        return outTimeClosed;
    }

    public void setOutTimeClosed(Integer outTimeClosed) {
        this.outTimeClosed = outTimeClosed;
    }

}
