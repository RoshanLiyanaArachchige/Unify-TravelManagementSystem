package com.rlabdevs.unifymobile.models.account;

import java.util.Date;

public class NewUserModel {
    private Integer UserDetailId;
    private String RegistrationCode;
    private String FirstName;
    private String LastName;
    private String Address;
    private String Email;
    private String MobileNo;
    private String NIC;
    private Integer StatusId;
    private NewLoginDetailModel LoginDetail;

    private Integer ApiResponseStatus;
    private String StatusMessage;

    public NewUserModel() {
    }

    public NewUserModel(Integer userDetailId, String registrationCode, String firstName, String lastName, String address, String email, String mobileNo, String NIC, Integer statusId, NewLoginDetailModel loginDetail) {
        UserDetailId = userDetailId;
        RegistrationCode = registrationCode;
        FirstName = firstName;
        LastName = lastName;
        Address = address;
        Email = email;
        MobileNo = mobileNo;
        this.NIC = NIC;
        StatusId = statusId;
        LoginDetail = loginDetail;
    }

    public Integer getUserDetailId() {
        return UserDetailId;
    }

    public void setUserDetailId(Integer userDetailId) {
        UserDetailId = userDetailId;
    }

    public String getRegistrationCode() {
        return RegistrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        RegistrationCode = registrationCode;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public Integer getStatusId() {
        return StatusId;
    }

    public void setStatusId(Integer statusId) {
        StatusId = statusId;
    }

    public NewLoginDetailModel getLoginDetail() {
        return LoginDetail;
    }

    public void setLoginDetail(NewLoginDetailModel loginDetail) {
        LoginDetail = loginDetail;
    }

    public Integer getApiResponseStatus() {
        return ApiResponseStatus;
    }

    public void setApiResponseStatus(Integer apiResponseStatus) {
        ApiResponseStatus = apiResponseStatus;
    }

    public String getStatusMessage() {
        return StatusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        StatusMessage = statusMessage;
    }
}
