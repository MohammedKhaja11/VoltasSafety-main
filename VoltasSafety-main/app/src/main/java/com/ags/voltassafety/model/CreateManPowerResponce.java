package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateManPowerResponce {
    @SerializedName("result")
    @Expose
    private CreateManPowerResult result;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("errorCode")
    @Expose
    private String errorCode;
    @SerializedName("errors")
    @Expose
    private Errors errors;

    public CreateManPowerResult getResult() {
        return result;
    }

    public void setResult(CreateManPowerResult result) {
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

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
