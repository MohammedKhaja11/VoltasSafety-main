package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ObservationItemsDetailsModel implements Serializable {

    @SerializedName("observationAttachmentModels")
    @Expose
    private List<ObservationAttachmentModel> observationAttachmentModels = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("observationId")
    @Expose
    private String observationId;
    @SerializedName("typeOfObservation")
    @Expose
    private String typeOfObservation;
    @SerializedName("vertical")
    @Expose
    private String vertical;
    @SerializedName("hazardType")
    @Expose
    private String hazardType;
    @SerializedName("risk")
    @Expose
    private String risk;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("briefDescription")
    @Expose
    private String briefDescription;
    @SerializedName("actionsDescription")
    @Expose
    private String actionsDescription;
    @SerializedName("description2")
    @Expose
    private String description2;
    @SerializedName("incidentType")
    @Expose
    private String incidentType;
    @SerializedName("injured")
    @Expose
    private String injured;
    @SerializedName("directCause")
    @Expose
    private String directCause;
    @SerializedName("routeCause")
    @Expose
    private String routeCause;
    @SerializedName("underlayingCause")
    @Expose
    private String underlayingCause;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("adminRemarks")
    @Expose
    private String adminRemarks;
    @SerializedName("itemAttachments")
    @Expose
    private List<Integer> itemAttachments = null;
    @SerializedName("correctiveAction")
    @Expose
    private String correctiveAction;
    @SerializedName("preventiveAction")
    @Expose
    private String preventiveAction;
    @SerializedName("subcontractor")
    @Expose
    private String subcontractor;
    @SerializedName("classification")
    @Expose
    private String classification;
    @SerializedName("remarkreason")
    @Expose
    private String remarkreason;
    @SerializedName("analysis")
    @Expose
    private String analysis;
    @SerializedName("injuredDetailsModels")
    @Expose
    private List<InjuredDetailsModel> injuredDetailsModels = null;
    @SerializedName("itemsActionDetailsModels")
    @Expose
    private List<ItemsActionDetailsModel> itemsActionDetailsModels = null;

    public List<ObservationAttachmentModel> getObservationAttachmentModels() {
        return observationAttachmentModels;
    }

    public void setObservationAttachmentModels(List<ObservationAttachmentModel> observationAttachmentModels) {
        this.observationAttachmentModels = observationAttachmentModels;
    }


    public String getSubcontractor() {
        return subcontractor;
    }

    public void setSubcontractor(String subcontractor) {
        this.subcontractor = subcontractor;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getRemarkreason() {
        return remarkreason;
    }

    public void setRemarkreason(String remarkreason) {
        this.remarkreason = remarkreason;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }

    public String getTypeOfObservation() {
        return typeOfObservation;
    }

    public void setTypeOfObservation(String typeOfObservation) {
        this.typeOfObservation = typeOfObservation;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }

    public String getHazardType() {
        return hazardType;
    }

    public void setHazardType(String hazardType) {
        this.hazardType = hazardType;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public String getActionsDescription() {
        return actionsDescription;
    }

    public void setActionsDescription(String actionsDescription) {
        this.actionsDescription = actionsDescription;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public String getInjured() {
        return injured;
    }

    public void setInjured(String injured) {
        this.injured = injured;
    }

    public String getDirectCause() {
        return directCause;
    }

    public void setDirectCause(String directCause) {
        this.directCause = directCause;
    }

    public String getRouteCause() {
        return routeCause;
    }

    public void setRouteCause(String routeCause) {
        this.routeCause = routeCause;
    }

    public String getUnderlayingCause() {
        return underlayingCause;
    }

    public void setUnderlayingCause(String underlayingCause) {
        this.underlayingCause = underlayingCause;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAdminRemarks() {
        return adminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }

    public List<Integer> getItemAttachments() {
        return itemAttachments;
    }

    public void setItemAttachments(List<Integer> itemAttachments) {
        this.itemAttachments = itemAttachments;
    }

    public String getCorrectiveAction() {
        return correctiveAction;
    }

    public void setCorrectiveAction(String correctiveAction) {
        this.correctiveAction = correctiveAction;
    }

    public String getPreventiveAction() {
        return preventiveAction;
    }

    public void setPreventiveAction(String preventiveAction) {
        this.preventiveAction = preventiveAction;
    }

    public List<InjuredDetailsModel> getInjuredDetailsModels() {
        return injuredDetailsModels;
    }

    public void setInjuredDetailsModels(List<InjuredDetailsModel> injuredDetailsModels) {
        this.injuredDetailsModels = injuredDetailsModels;
    }

    public List<ItemsActionDetailsModel> getItemsActionDetailsModels() {
        return itemsActionDetailsModels;
    }

    public void setItemsActionDetailsModels(List<ItemsActionDetailsModel> itemsActionDetailsModels) {
        this.itemsActionDetailsModels = itemsActionDetailsModels;
    }
}
