package com.ags.voltassafety.model;

public class LoginCredentials {

    private String LoginName;
    private String Password;
    private String IMEI;

    public LoginCredentials(String LoginName, String Password, String IMEI) {
        this.LoginName = LoginName;
        this.Password = Password;
        this.IMEI = IMEI;
    }
}
