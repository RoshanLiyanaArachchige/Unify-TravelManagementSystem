package com.rlabdevs.unifymobile.models;

import com.google.firebase.firestore.Exclude;

public class LoginModel {

    @Exclude
    private String ID;
    private String LoginCode;
    private String UserDetailsCode;
    private String UserName;
    private String Password;
    private String UserRoleCode;
    private String StatusCode;

    public LoginModel() {
    }

    public LoginModel(String loginCode, String userDetailsCode, String userName, String password, String userRoleCode, String statusCode) {
        LoginCode = loginCode;
        UserDetailsCode = userDetailsCode;
        UserName = userName;
        Password = password;
        UserRoleCode = userRoleCode;
        StatusCode = statusCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLoginCode() {
        return LoginCode;
    }

    public void setLoginCode(String loginCode) {
        LoginCode = loginCode;
    }

    public String getUserDetailsCode() {
        return UserDetailsCode;
    }

    public void setUserDetailsCode(String userDetailsCode) {
        UserDetailsCode = userDetailsCode;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserRoleCode() {
        return UserRoleCode;
    }

    public void setUserRoleCode(String userRoleCode) {
        UserRoleCode = userRoleCode;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
