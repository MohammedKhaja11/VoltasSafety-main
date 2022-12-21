package com.ags.voltassafety.model;

public class ObservationData {
    String article_id;
    String customername;
    String location;
    String incidentdateandtime;
    String createdon;
    String observationstatus;
    String priority;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private boolean expanded;

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIncidentdateandtime() {
        return incidentdateandtime;
    }

    public void setIncidentdateandtime(String incidentdateandtime) {
        this.incidentdateandtime = incidentdateandtime;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public String getObservationstatus() {
        return observationstatus;
    }

    public void setObservationstatus(String observationstatus) {
        this.observationstatus = observationstatus;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
