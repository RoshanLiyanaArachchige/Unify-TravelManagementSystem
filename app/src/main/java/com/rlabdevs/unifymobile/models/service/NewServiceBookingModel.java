package com.rlabdevs.unifymobile.models.service;

public class NewServiceBookingModel {
    private Integer BookingId;
    private String Code;
    private String FullName;
    private String EmailAddress;
    private String MobileNo;
    private Double TotalPrice;
    private Integer StatusId;

    public NewServiceBookingModel() {
    }

    public NewServiceBookingModel(Integer bookingId, String code, String fullName, String emailAddress, String mobileNo, Double totalPrice, Integer statusId) {
        BookingId = bookingId;
        Code = code;
        FullName = fullName;
        EmailAddress = emailAddress;
        MobileNo = mobileNo;
        TotalPrice = totalPrice;
        StatusId = statusId;
    }

    public Integer getBookingId() {
        return BookingId;
    }

    public void setBookingId(Integer bookingId) {
        BookingId = bookingId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public Double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        TotalPrice = totalPrice;
    }

    public Integer getStatusId() {
        return StatusId;
    }

    public void setStatusId(Integer statusId) {
        StatusId = statusId;
    }
}
