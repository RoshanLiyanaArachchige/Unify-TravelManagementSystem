package com.rlabdevs.unifymobile.models;

import com.google.firebase.firestore.Exclude;
import com.rlabdevs.unifymobile.R;

public class UserDetailsModel {

    @Exclude
    private String ID;
    private String UserDetailsCode;
    private String FirstName;
    private String LastName;
    private String Address;
    private String NIC;
    private String MobileNo;
    private String Email;
    private String StatusCode;

    public UserDetailsModel() {
    }

    public UserDetailsModel(String userDetailsCode, String firstName, String lastName, String address, String NIC, String mobileNo, String email, String statusCode) {
        UserDetailsCode = userDetailsCode;
        FirstName = firstName;
        LastName = lastName;
        Address = address;
        this.NIC = NIC;
        MobileNo = mobileNo;
        Email = email;
        StatusCode = statusCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserDetailsCode() {
        return UserDetailsCode;
    }

    public void setUserDetailsCode(String userDetailsCode) {
        UserDetailsCode = userDetailsCode;
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

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
