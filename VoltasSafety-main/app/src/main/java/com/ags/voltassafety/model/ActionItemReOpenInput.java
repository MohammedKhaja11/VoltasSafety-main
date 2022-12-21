package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionItemReOpenInput {

    @SerializedName("itemId")
    @Expose
    private Integer itemId;
    @SerializedName("actionId")
    @Expose
    private Integer actionId;
    @SerializedName("adminRemark")
    @Expose
    private String adminRemark;
    @SerializedName("observationId")
    @Expose
    private String observationId;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }

    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }

}