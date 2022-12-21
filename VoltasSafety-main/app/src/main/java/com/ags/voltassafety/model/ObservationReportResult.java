package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ObservationReportResult implements Serializable {
    @SerializedName("observationTypes")
    @Expose
    private List<ObservationReportType> observationTypes = null;
    @SerializedName("observationStatus")
    @Expose
    private List<ObservationReportType> observationStatus = null;
    @SerializedName("observationTypesTotal")
    @Expose
    private Integer observationTypesTotal;
    @SerializedName("observationStatusTotal")
    @Expose
    private Integer observationStatusTotal;

    public List<ObservationReportType> getObservationTypes() {
        return observationTypes;
    }

    public void setObservationTypes(List<ObservationReportType> observationTypes) {
        this.observationTypes = observationTypes;
    }

    public List<ObservationReportType> getObservationStatus() {
        return observationStatus;
    }

    public void setObservationStatus(List<ObservationReportType> observationStatus) {
        this.observationStatus = observationStatus;
    }

    public Integer getObservationTypesTotal() {
        return observationTypesTotal;
    }

    public void setObservationTypesTotal(Integer observationTypesTotal) {
        this.observationTypesTotal = observationTypesTotal;
    }

    public Integer getObservationStatusTotal() {
        return observationStatusTotal;
    }

    public void setObservationStatusTotal(Integer observationStatusTotal) {
        this.observationStatusTotal = observationStatusTotal;
    }

}
