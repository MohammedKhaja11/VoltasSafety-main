package com.ags.voltassafety.model;

public class IncidentTypeModel {
    String type;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
