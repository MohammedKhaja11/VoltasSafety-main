package com.ags.voltassafety.model;

public class ChangePassword {
    int domainId;
    String userID;
    String oldPassword;
    String newPassword;

    public ChangePassword(int domainId, String userID, String oldPassword, String newPassword) {
        this.domainId = domainId;
        this.userID = userID;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
