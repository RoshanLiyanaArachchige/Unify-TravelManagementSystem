package com.rlabdevs.unifymobile.models;

public class RoomTypesModel {

    private String ID;
    private String HotelCode;
    private String RoomTypeCode;
    private String RoomType;
    private String StatusCode;

    public RoomTypesModel() {
    }

    public RoomTypesModel(String roomTypeCode, String hotelCode, String roomType, String statusCode) {
        RoomTypeCode = roomTypeCode;
        HotelCode = hotelCode;
        RoomType = roomType;
        StatusCode = statusCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHotelCode() {
        return HotelCode;
    }

    public void setHotelCode(String hotelCode) {
        HotelCode = hotelCode;
    }

    public String getRoomTypeCode() {
        return RoomTypeCode;
    }

    public void setRoomTypeCode(String roomTypeCode) {
        RoomTypeCode = roomTypeCode;
    }

    public String getRoomType() {
        return RoomType;
    }

    public void setRoomType(String roomType) {
        RoomType = roomType;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
