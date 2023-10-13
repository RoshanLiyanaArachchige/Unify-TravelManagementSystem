package com.rlabdevs.unifymobile.models;

import java.util.Date;

public class RoomBookingModel {
    private String BookedByUserDetailsCode;
    private String BookingCode;
    private String BookingStatusCode;
    private Date CheckinDate;
    private Date CheckoutDate;
    private String EmailAddress;
    private String FullName;
    private String HotelCode;
    private String MobileNo;
    private Integer NoOfAdults;
    private Integer NoOfChildren;
    private String RoomCode;
    private String RoomTypeCode;

    public RoomBookingModel() {
    }

    public RoomBookingModel(String bookedByUserDetailsCode, String bookingCode, String bookingStatusCode, Date checkinDate, Date checkoutDate, String emailAddress, String fullName, String hotelCode, String mobileNo, Integer noOfAdults, Integer noOfChildren, String roomCode, String roomTypeCode) {
        BookedByUserDetailsCode = bookedByUserDetailsCode;
        BookingCode = bookingCode;
        BookingStatusCode = bookingStatusCode;
        CheckinDate = checkinDate;
        CheckoutDate = checkoutDate;
        EmailAddress = emailAddress;
        FullName = fullName;
        HotelCode = hotelCode;
        MobileNo = mobileNo;
        NoOfAdults = noOfAdults;
        NoOfChildren = noOfChildren;
        RoomCode = roomCode;
        RoomTypeCode = roomTypeCode;
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

    public Date getCheckinDate() {
        return CheckinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        CheckinDate = checkinDate;
    }

    public Date getCheckoutDate() {
        return CheckoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        CheckoutDate = checkoutDate;
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

    public String getHotelCode() {
        return HotelCode;
    }

    public void setHotelCode(String hotelCode) {
        HotelCode = hotelCode;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public Integer getNoOfAdults() {
        return NoOfAdults;
    }

    public void setNoOfAdults(Integer noOfAdults) {
        NoOfAdults = noOfAdults;
    }

    public Integer getNoOfChildren() {
        return NoOfChildren;
    }

    public void setNoOfChildren(Integer noOfChildren) {
        NoOfChildren = noOfChildren;
    }

    public String getRoomCode() {
        return RoomCode;
    }

    public void setRoomCode(String roomCode) {
        RoomCode = roomCode;
    }

    public String getRoomTypeCode() {
        return RoomTypeCode;
    }

    public void setRoomTypeCode(String roomTypeCode) {
        RoomTypeCode = roomTypeCode;
    }
}
