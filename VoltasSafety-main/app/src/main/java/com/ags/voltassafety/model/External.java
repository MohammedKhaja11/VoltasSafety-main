package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class External implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("noOfTraininsessions")
    @Expose
    private Integer noOfTraininsessions;
    @SerializedName("staff")
    @Expose
    private Integer staff;
    @SerializedName("labours")
    @Expose
    private Integer labours;

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

    public Integer getNoOfTraininsessions() {
        return noOfTraininsessions;
    }

    public void setNoOfTraininsessions(Integer noOfTraininsessions) {
        this.noOfTraininsessions = noOfTraininsessions;
    }

    public Integer getStaff() {
        return staff;
    }

    public void setStaff(Integer staff) {
        this.staff = staff;
    }

    public Integer getLabours() {
        return labours;
    }

    public void setLabours(Integer labours) {
        this.labours = labours;
    }
}