package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ObservationListViewResponseResult implements Serializable {
    @SerializedName("observationId")
    @Expose
    private String observationId;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("siteName")
    @Expose
    private String siteName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("dateOfIncidence")
    @Expose
    private String dateOfIncidence;
    @SerializedName("projectManagerName")
    @Expose
    private String projectManagerName;
    @SerializedName("typeOfObservation")
    @Expose
    private String typeOfObservation;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("subType")
    @Expose
    private String subType;
    @SerializedName("targetDateOfClosure")
    @Expose
    private String targetDateOfClosure;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("employeeCode")
    @Expose
    private String employeeCode;
    @SerializedName("employeeName")
    @Expose
    private String employeeName;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("location")
    @Expose
    private String location;

    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfIncidence() {
        return dateOfIncidence;
    }

    public void setDateOfIncidence(String dateOfIncidence) {
        this.dateOfIncidence = dateOfIncidence;
    }

    public String getProjectManagerName() {
        return projectManagerName;
    }

    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    public String getTypeOfObservation() {
        return typeOfObservation;
    }

    public void setTypeOfObservation(String typeOfObservation) {
        this.typeOfObservation = typeOfObservation;
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

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getTargetDateOfClosure() {
        return targetDateOfClosure;
    }

    public void setTargetDateOfClosure(String targetDateOfClosure) {
        this.targetDateOfClosure = targetDateOfClosure;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
