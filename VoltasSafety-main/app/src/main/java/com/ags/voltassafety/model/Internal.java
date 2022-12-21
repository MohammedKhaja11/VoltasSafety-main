package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Internal implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("noOfTrainingSessions")
    @Expose
    private Integer noOfTrainingSessions;
    @SerializedName("staff")
    @Expose
    private Integer staff;
    @SerializedName("labour")
    @Expose
    private Integer labour;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNoOfTrainingSessions() {
        return noOfTrainingSessions;
    }

    public void setNoOfTrainingSessions(Integer noOfTrainingSessions) {
        this.noOfTrainingSessions = noOfTrainingSessions;
    }

    public Integer getStaff() {
        return staff;
    }

    public void setStaff(Integer staff) {
        this.staff = staff;
    }

    public Integer getLabour() {
        return labour;
    }

    public void setLabour(Integer labour) {
        this.labour = labour;
    }
}