package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class ObservationHeader implements Serializable {

    @SerializedName("observationId")
    @Expose
    private String observationId;
    @SerializedName("sapCustomerId")
    @Expose
    private String sapCustomerId;
    @SerializedName("customerId")
    @Expose
    private String customerId;
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
    @SerializedName("siteEngineerId")
    @Expose
    private String siteEngineerId;
    @SerializedName("siteEngineerName")
    @Expose
    private String siteEngineerName;
    @SerializedName("projectManagerId")
    @Expose
    private String projectManagerId;
    @SerializedName("projectManagerName")
    @Expose
    private String projectManagerName;
    @SerializedName("typeOfObservation")
    @Expose
    private String typeOfObservation;
    @SerializedName("subType")
    @Expose
    private String subType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("cancelRemark")
    @Expose
    private String cancelRemark;
    @SerializedName("adminRemark")
    @Expose
    private String adminRemark;
    @SerializedName("targetDateOfClosure")
    @Expose
    private String targetDateOfClosure;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userEmployeeId")
    @Expose
    private String userEmployeeId;
    @SerializedName("userPhoneNumber")
    @Expose
    private String userPhoneNumber;
    @SerializedName("userEmail")
    @Expose
    private String userEmail;
    @SerializedName("managerId")
    @Expose
    private String managerId;
    @SerializedName("managerName")
    @Expose
    private String managerName;
    @SerializedName("managerEmployeeId")
    @Expose
    private String managerEmployeeId;
    @SerializedName("managerPhoneNumber")
    @Expose
    private String managerPhoneNumber;
    @SerializedName("managerEmail")
    @Expose
    private String managerEmail;
    @SerializedName("guestName")
    @Expose
    private String guestName;
    @SerializedName("guestPhone")
    @Expose
    private String guestPhone;
    @SerializedName("guestEmail")
    @Expose
    private String guestEmail;
    @SerializedName("guestPfno")
    @Expose
    private String guestPfno;
    @SerializedName("field1")
    @Expose
    private String field1;
    @SerializedName("field2")
    @Expose
    private String field2;
    @SerializedName("guestZoneId")
    @Expose
    private String guestZoneId;
    @SerializedName("guestZone")
    @Expose
    private String guestZone;
    @SerializedName("guestBranchId")
    @Expose
    private String guestBranchId;
    @SerializedName("guestBranch")
    @Expose
    private String guestBranch;
    @SerializedName("concernEngineerOrSupervisor")
    @Expose
    private String concernEngineerOrSupervisor;
    @SerializedName("concernEngineerOrSupervisorEmail")
    @Expose
    private String concernEngineerOrSupervisorEmail;
    @SerializedName("otherResponsiblepersons")
    @Expose
    private List<String> otherResponsiblepersons = null;
    @SerializedName("observationItemsDetailsModels")
    @Expose
    private List<ObservationItemsDetailsModel> observationItemsDetailsModels = null;

    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }

    public String getSapCustomerId() {
        return sapCustomerId;
    }

    public void setSapCustomerId(String sapCustomerId) {
        this.sapCustomerId = sapCustomerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getSiteEngineerId() {
        return siteEngineerId;
    }

    public void setSiteEngineerId(String siteEngineerId) {
        this.siteEngineerId = siteEngineerId;
    }

    public String getSiteEngineerName() {
        return siteEngineerName;
    }

    public void setSiteEngineerName(String siteEngineerName) {
        this.siteEngineerName = siteEngineerName;
    }

    public String getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(String projectManagerId) {
        this.projectManagerId = projectManagerId;
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

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
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

    public String getCancelRemark() {
        return cancelRemark;
    }

    public void setCancelRemark(String cancelRemark) {
        this.cancelRemark = cancelRemark;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }

    public String getTargetDateOfClosure() {
        return targetDateOfClosure;
    }

    public void setTargetDateOfClosure(String targetDateOfClosure) {
        this.targetDateOfClosure = targetDateOfClosure;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmployeeId() {
        return userEmployeeId;
    }

    public void setUserEmployeeId(String userEmployeeId) {
        this.userEmployeeId = userEmployeeId;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerEmployeeId() {
        return managerEmployeeId;
    }

    public void setManagerEmployeeId(String managerEmployeeId) {
        this.managerEmployeeId = managerEmployeeId;
    }

    public String getManagerPhoneNumber() {
        return managerPhoneNumber;
    }

    public void setManagerPhoneNumber(String managerPhoneNumber) {
        this.managerPhoneNumber = managerPhoneNumber;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getGuestPfno() {
        return guestPfno;
    }

    public void setGuestPfno(String guestPfno) {
        this.guestPfno = guestPfno;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getGuestZoneId() {
        return guestZoneId;
    }

    public void setGuestZoneId(String guestZoneId) {
        this.guestZoneId = guestZoneId;
    }

    public String getGuestZone() {
        return guestZone;
    }

    public void setGuestZone(String guestZone) {
        this.guestZone = guestZone;
    }

    public String getGuestBranchId() {
        return guestBranchId;
    }

    public void setGuestBranchId(String guestBranchId) {
        this.guestBranchId = guestBranchId;
    }

    public String getGuestBranch() {
        return guestBranch;
    }

    public void setGuestBranch(String guestBranch) {
        this.guestBranch = guestBranch;
    }

    public String getConcernEngineerOrSupervisor() {
        return concernEngineerOrSupervisor;
    }

    public void setConcernEngineerOrSupervisor(String concernEngineerOrSupervisor) {
        this.concernEngineerOrSupervisor = concernEngineerOrSupervisor;
    }

    public String getConcernEngineerOrSupervisorEmail() {
        return concernEngineerOrSupervisorEmail;
    }

    public void setConcernEngineerOrSupervisorEmail(String concernEngineerOrSupervisorEmail) {
        this.concernEngineerOrSupervisorEmail = concernEngineerOrSupervisorEmail;
    }


    public List<String> getOtherResponsiblepersons() {
        return otherResponsiblepersons;
    }

    public void setOtherResponsiblepersons(List<String> otherResponsiblepersons) {
        this.otherResponsiblepersons = otherResponsiblepersons;
    }

    public List<ObservationItemsDetailsModel> getObservationItemsDetailsModels() {
        return observationItemsDetailsModels;
    }

    public void setObservationItemsDetailsModels(List<ObservationItemsDetailsModel> observationItemsDetailsModels) {
        this.observationItemsDetailsModels = observationItemsDetailsModels;
    }


    public static Comparator<ObservationHeader> dateComparator = new Comparator<ObservationHeader>() {
        @Override
        public int compare(ObservationHeader t1, ObservationHeader t2) {
            return t2.getDateOfIncidence().compareTo(t1.getDateOfIncidence());
        }
    };
}

