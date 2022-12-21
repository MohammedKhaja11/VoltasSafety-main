package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class EntityResult {
    @SerializedName("entityName")
    @Expose
    private String entityName;
    @SerializedName("lookUpName")
    @Expose
    private String lookUpName;
    @SerializedName("lookUpValue")
    @Expose
    private String lookUpValue;

    @SerializedName("lookUpId")
    @Expose
    private String lookUpId;

    public String getLookUpId() {
        return lookUpId;
    }

    public void setLookUpId(String lookUpId) {
        this.lookUpId = lookUpId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getLookUpName() {
        return lookUpName;
    }

    public void setLookUpName(String lookUpName) {
        this.lookUpName = lookUpName;
    }

    public String getLookUpValue() {
        return lookUpValue;
    }

    public void setLookUpValue(String lookUpValue) {
        this.lookUpValue = lookUpValue;
    }

    public static Comparator<EntityResult> valueComparator = new Comparator<EntityResult>() {
        @Override
        public int compare(EntityResult t1, EntityResult t2) {
            return t1.getLookUpValue().compareTo(t2.getLookUpValue());
        }
    };
}
