package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ItemsActionDetailsModel implements Serializable {


    @SerializedName("observationAttachmentModels")
    @Expose
    private List<ObservationAttachmentModel> observationAttachmentModels = null;

    public List<ObservationAttachmentModel> getObservationAttachmentModels() {
        return observationAttachmentModels;
    }

    public void setObservationAttachmentModels(List<ObservationAttachmentModel> observationAttachmentModels) {
        this.observationAttachmentModels = observationAttachmentModels;
    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("observationItemId")
    @Expose
    private Integer observationItemId;
    @SerializedName("actionName")
    @Expose
    private String actionName;
    @SerializedName("targetDateOfClosure")
    @Expose
    private String targetDateOfClosure;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reason")
    @Expose
    private List<Integer> reason = null;
    @SerializedName("adminRemarks")
    @Expose
    private String adminRemarks;

    public String getCpRemarks() {
        return cpRemarks;
    }

    public void setCpRemarks(String cpRemarks) {
        this.cpRemarks = cpRemarks;
    }

    @SerializedName("cpRemarks")
    @Expose
    private String cpRemarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getObservationItemId() {
        return observationItemId;
    }

    public void setObservationItemId(Integer observationItemId) {
        this.observationItemId = observationItemId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getTargetDateOfClosure() {
        return targetDateOfClosure;
    }

    public void setTargetDateOfClosure(String targetDateOfClosure) {
        this.targetDateOfClosure = targetDateOfClosure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Integer> getReason() {
        return reason;
    }

    public void setReason(List<Integer> reason) {
        this.reason = reason;
    }

    public String getAdminRemarks() {
        return adminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }

}
