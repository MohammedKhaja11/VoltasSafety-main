package com.ags.voltassafety.model;

import java.io.Serializable;

/**
 * Created by VENGABABU.N on 10/20/2017.
 */

public class UserGpsInfo implements Serializable {

    private String UserID;
    private String Username;
    private String MobileNumber;
    private String LogonName;
    private String EmailID;
    private String Latitude;
    private String Langitude;
    private String Address;
    private String GpsDate;
    private String SyncDate;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getLogonName() {
        return LogonName;
    }

    public void setLogonName(String logonName) {
        LogonName = logonName;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLangitude() {
        return Langitude;
    }

    public void setLangitude(String langitude) {
        Langitude = langitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGpsDate() {
        return GpsDate;
    }

    public void setGpsDate(String gpsDate) {
        GpsDate = gpsDate;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String syncDate) {
        SyncDate = syncDate;
    }
}
