package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("observationsStatus")
    @Expose
    private List<Observationsstatus> observationsStatus = null;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<Observationsstatus> getObservationsStatus() {
        return observationsStatus;
    }

    public void setObservationsStatus(List<Observationsstatus> observationsStatus) {
        this.observationsStatus = observationsStatus;
    }
}
