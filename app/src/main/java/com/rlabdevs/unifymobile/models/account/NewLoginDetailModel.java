package com.rlabdevs.unifymobile.models.account;

import java.util.Date;

public class NewLoginDetailModel {
    private Integer LoginDetailId;
    private Integer UserDetailId;
    private Integer UserRoleId;
    private String Username;
    private String Password;
    private Integer StatusId;

    public NewLoginDetailModel() {
    }

    public NewLoginDetailModel(Integer loginDetailId, Integer userDetailId, Integer userRoleId, String username, String password, Integer statusId) {
        LoginDetailId = loginDetailId;
        UserDetailId = userDetailId;
        UserRoleId = userRoleId;
        Username = username;
        Password = password;
        StatusId = statusId;
    }

    public Integer getLoginDetailId() {
        return LoginDetailId;
    }

    public void setLoginDetailId(Integer loginDetailId) {
        LoginDetailId = loginDetailId;
    }

    public Integer getUserDetailId() {
        return UserDetailId;
    }

    public void setUserDetailId(Integer userDetailId) {
        UserDetailId = userDetailId;
    }

    public Integer getUserRoleId() {
        return UserRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        UserRoleId = userRoleId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Integer getStatusId() {
        return StatusId;
    }

    public void setStatusId(Integer statusId) {
        StatusId = statusId;
    }
}
