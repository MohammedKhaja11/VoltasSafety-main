package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SiteEnginnerResponse {
    @SerializedName("result")
    @Expose
    private List<SiteEngineerResult> result = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("errorCode")
    @Expose
    private String errorCode;
    @SerializedName("errors")
    @Expose
    private List<String> errors = null;

    public List<SiteEngineerResult> getResult() {
        return result;
    }

    public void setResult(List<SiteEngineerResult> result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
