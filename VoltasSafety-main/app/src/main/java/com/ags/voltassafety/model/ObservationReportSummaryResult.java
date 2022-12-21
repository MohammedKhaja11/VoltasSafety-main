package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ObservationReportSummaryResult implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("all")
    @Expose
    private Integer all;
    @SerializedName("open")
    @Expose
    private Integer open;
    @SerializedName("assigned")
    @Expose
    private Integer assigned;
    @SerializedName("closed")
    @Expose
    private Integer closed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
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

    public Integer getClosed() {
        return closed;
    }

    public void setClosed(Integer closed) {
        this.closed = closed;
    }
}
