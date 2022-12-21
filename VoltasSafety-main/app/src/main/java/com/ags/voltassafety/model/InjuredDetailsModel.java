package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class InjuredDetailsModel implements Serializable {

    @SerializedName("designation")
    @Expose
    private String designation;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @SerializedName("observationAttachmentModels")
    @Expose
    private List<ObservationAttachmentModel> observationAttachmentModels = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("incidentItemId")
    @Expose
    private Integer incidentItemId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("injuredDateTime")
    @Expose
    private String injuredDateTime;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("employeeType")
    @Expose
    private String employeeType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("treatment")
    @Expose
    private String treatment;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("itemAttachments")
    @Expose
    private List<Integer> itemAttachments = null;

    public List<ObservationAttachmentModel> getObservationAttachmentModels() {
        return observationAttachmentModels;
    }

    public void setObservationAttachmentModels(List<ObservationAttachmentModel> observationAttachmentModels) {
        this.observationAttachmentModels = observationAttachmentModels;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIncidentItemId() {
        return incidentItemId;
    }

    public void setIncidentItemId(Integer incidentItemId) {
        this.incidentItemId = incidentItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getInjuredDateTime() {
        return injuredDateTime;
    }

    public void setInjuredDateTime(String injuredDateTime) {
        this.injuredDateTime = injuredDateTime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
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

    public List<Integer> getItemAttachments() {
        return itemAttachments;
    }

    public void setItemAttachments(List<Integer> itemAttachments) {
        this.itemAttachments = itemAttachments;
    }
}
