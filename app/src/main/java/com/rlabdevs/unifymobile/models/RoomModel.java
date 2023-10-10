package com.rlabdevs.unifymobile.models;

import com.google.firebase.firestore.Exclude;

public class RoomModel {

    private String ID;
    private String HotelCode;
    private String RoomCode;
    private String RoomTypeCode;
    private String RoomDescription;
    private double RoomPrice;
    private int NoOfAdults;
    private int NoOfChildren;
    private int NoOfTotalRooms;
    private String CurrencyCode;
    private String RoomImage;
    private boolean FreeWIFI;
    private boolean AirConditioned;
    private boolean FreeBreakfast;
    private boolean TeaCoffee;
    private boolean Bar;
    private boolean RoomService;
    private boolean Television;
    private boolean Pool;
    private boolean Parking;
    private boolean Spa;
    private String StatusCode;

    public RoomModel() {
    }

    public RoomModel(String hotelCode, String roomCode, String roomTypeCode, String roomDescription, double roomPrice, int noOfAdults, int noOfChildren, int noOfTotalRooms, String currencyCode, String roomImage, boolean freeWIFI, boolean airConditioned, boolean freeBreakfast, boolean teaCoffee, boolean bar, boolean roomService, boolean television, boolean pool, boolean parking, boolean spa, String statusCode) {
        HotelCode = hotelCode;
        RoomCode = roomCode;
        RoomTypeCode = roomTypeCode;
        RoomDescription = roomDescription;
        RoomPrice = roomPrice;
        NoOfAdults = noOfAdults;
        NoOfChildren = noOfChildren;
        NoOfTotalRooms = noOfTotalRooms;
        CurrencyCode = currencyCode;
        RoomImage = roomImage;
        FreeWIFI = freeWIFI;
        AirConditioned = airConditioned;
        FreeBreakfast = freeBreakfast;
        TeaCoffee = teaCoffee;
        Bar = bar;
        RoomService = roomService;
        Television = television;
        Pool = pool;
        Parking = parking;
        Spa = spa;
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

    public String getRoomDescription() {
        return RoomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        RoomDescription = roomDescription;
    }

    public double getRoomPrice() {
        return RoomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        RoomPrice = roomPrice;
    }

    public int getNoOfAdults() {
        return NoOfAdults;
    }

    public void setNoOfAdults(int noOfAdults) {
        NoOfAdults = noOfAdults;
    }

    public int getNoOfChildren() {
        return NoOfChildren;
    }

    public void setNoOfChildren(int noOfChildren) {
        NoOfChildren = noOfChildren;
    }

    public int getNoOfTotalRooms() {
        return NoOfTotalRooms;
    }

    public void setNoOfTotalRooms(int noOfTotalRooms) {
        NoOfTotalRooms = noOfTotalRooms;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getRoomImage() {
        return RoomImage;
    }

    public void setRoomImage(String roomImage) {
        RoomImage = roomImage;
    }

    public boolean isFreeWIFI() {
        return FreeWIFI;
    }

    public void setFreeWIFI(boolean freeWIFI) {
        FreeWIFI = freeWIFI;
    }

    public boolean isAirConditioned() {
        return AirConditioned;
    }

    public void setAirConditioned(boolean airConditioned) {
        AirConditioned = airConditioned;
    }

    public boolean isFreeBreakfast() {
        return FreeBreakfast;
    }

    public void setFreeBreakfast(boolean freeBreakfast) {
        FreeBreakfast = freeBreakfast;
    }

    public boolean isTeaCoffee() {
        return TeaCoffee;
    }

    public void setTeaCoffee(boolean teaCoffee) {
        TeaCoffee = teaCoffee;
    }

    public boolean isBar() {
        return Bar;
    }

    public void setBar(boolean bar) {
        Bar = bar;
    }

    public boolean isRoomService() {
        return RoomService;
    }

    public void setRoomService(boolean roomService) {
        RoomService = roomService;
    }

    public boolean isTelevision() {
        return Television;
    }

    public void setTelevision(boolean television) {
        Television = television;
    }

    public boolean isPool() {
        return Pool;
    }

    public void setPool(boolean pool) {
        Pool = pool;
    }

    public boolean isParking() {
        return Parking;
    }

    public void setParking(boolean parking) {
        Parking = parking;
    }

    public boolean isSpa() {
        return Spa;
    }

    public void setSpa(boolean spa) {
        Spa = spa;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
