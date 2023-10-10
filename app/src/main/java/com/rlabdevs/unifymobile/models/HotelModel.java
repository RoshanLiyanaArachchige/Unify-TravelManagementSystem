package com.rlabdevs.unifymobile.models;

public class HotelModel {

    private String ID;
    private String HotelCode;
    private String HotelName;
    private String HotelLocation;
    private int HotelClass;
    private double HotelRating;
    private double Budget;
    private String CurrencyCode;
    private String HotelTelNo;
    private String CheckIn;
    private String CheckOut;
    private String HotelImage;
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
    private String UserDetailsCode;
    private String StatusCode;
    private double Latitude;
    private double Longitude;

    public HotelModel() {
    }

    public HotelModel(String ID, String hotelCode, String hotelName, String hotelLocation, int hotelClass, double hotelRating, double budget, String currencyCode, String hotelTelNo, String checkIn, String checkOut, String hotelImage, boolean freeWIFI, boolean airConditioned, boolean freeBreakfast, boolean teaCoffee, boolean bar, boolean roomService, boolean television, boolean pool, boolean parking, boolean spa, String userDetailsCode, String statusCode, double latitude, double longitude) {
        this.ID = ID;
        HotelCode = hotelCode;
        HotelName = hotelName;
        HotelLocation = hotelLocation;
        HotelClass = hotelClass;
        HotelRating = hotelRating;
        Budget = budget;
        CurrencyCode = currencyCode;
        HotelTelNo = hotelTelNo;
        CheckIn = checkIn;
        CheckOut = checkOut;
        HotelImage = hotelImage;
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
        UserDetailsCode = userDetailsCode;
        StatusCode = statusCode;
        Latitude = latitude;
        Longitude = longitude;
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

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public String getHotelLocation() {
        return HotelLocation;
    }

    public void setHotelLocation(String hotelLocation) {
        HotelLocation = hotelLocation;
    }

    public int getHotelClass() {
        return HotelClass;
    }

    public void setHotelClass(int hotelClass) {
        HotelClass = hotelClass;
    }

    public double getHotelRating() {
        return HotelRating;
    }

    public void setHotelRating(double hotelRating) {
        HotelRating = hotelRating;
    }

    public double getBudget() {
        return Budget;
    }

    public void setBudget(double budget) {
        Budget = budget;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getHotelTelNo() {
        return HotelTelNo;
    }

    public void setHotelTelNo(String hotelTelNo) {
        HotelTelNo = hotelTelNo;
    }

    public String getCheckIn() {
        return CheckIn;
    }

    public void setCheckIn(String checkIn) {
        CheckIn = checkIn;
    }

    public String getCheckOut() {
        return CheckOut;
    }

    public void setCheckOut(String checkOut) {
        CheckOut = checkOut;
    }

    public String getHotelImage() {
        return HotelImage;
    }

    public void setHotelImage(String hotelImage) {
        HotelImage = hotelImage;
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

    public String getUserDetailsCode() {
        return UserDetailsCode;
    }

    public void setUserDetailsCode(String userDetailsCode) {
        UserDetailsCode = userDetailsCode;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}

