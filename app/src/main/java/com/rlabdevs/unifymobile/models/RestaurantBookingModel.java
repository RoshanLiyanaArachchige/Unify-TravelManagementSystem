package com.rlabdevs.unifymobile.models;

import java.util.Date;

public class RestaurantBookingModel {
    private String ID;
    private String BookedByUserDetailsCode;
    private String BookingCode;
    private String BookingStatusCode;
    private Date ReservationDate;
    private String EmailAddress;
    private String FullName;
    private String RestaurantCode;
    private String MobileNo;
    private Integer NoOfPersons;

    public RestaurantBookingModel() {
    }

    public RestaurantBookingModel(String ID, String bookedByUserDetailsCode, String bookingCode, String bookingStatusCode, Date reservationDate, String emailAddress, String fullName, String restaurantCode, String mobileNo, Integer noOfPersons) {
        this.ID = ID;
        BookedByUserDetailsCode = bookedByUserDetailsCode;
        BookingCode = bookingCode;
        BookingStatusCode = bookingStatusCode;
        ReservationDate = reservationDate;
        EmailAddress = emailAddress;
        FullName = fullName;
        RestaurantCode = restaurantCode;
        MobileNo = mobileNo;
        NoOfPersons = noOfPersons;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBookedByUserDetailsCode() {
        return BookedByUserDetailsCode;
    }

    public void setBookedByUserDetailsCode(String bookedByUserDetailsCode) {
        BookedByUserDetailsCode = bookedByUserDetailsCode;
    }

    public String getBookingCode() {
        return BookingCode;
    }

    public void setBookingCode(String bookingCode) {
        BookingCode = bookingCode;
    }

    public String getBookingStatusCode() {
        return BookingStatusCode;
    }

    public void setBookingStatusCode(String bookingStatusCode) {
        BookingStatusCode = bookingStatusCode;
    }

    public Date getReservationDate() {
        return ReservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        ReservationDate = reservationDate;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getRestaurantCode() {
        return RestaurantCode;
    }

    public void setRestaurantCode(String restaurantCode) {
        RestaurantCode = restaurantCode;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public Integer getNoOfPersons() {
        return NoOfPersons;
    }

    public void setNoOfPersons(Integer noOfPersons) {
        NoOfPersons = noOfPersons;
    }
}
