package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class SiteEngineerResult {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userPhone")
    @Expose
    private String userPhone;
    @SerializedName("userMail")
    @Expose
    private String userMail;

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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public static Comparator<SiteEngineerResult> nameComparator = new Comparator<SiteEngineerResult>() {
        @Override
        public int compare(SiteEngineerResult t1, SiteEngineerResult t2) {
            return t1.getUserName().trim().toUpperCase().compareTo(t2.getUserName().trim().toUpperCase());
        }
    };

}
