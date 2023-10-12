package com.rlabdevs.unifymobile.models;

import java.util.List;

public class RestaurantModel {
    private String ID;
    private String RestaurantCode;
    private String RestaurantName;
    private String RestaurantLocation;
    private String LocationCode;
    private int RestaurantClass;
    private double RestaurantRating;
    private double AveragePrice;
    private String CurrencyCode;
    private String RestaurantTelNo;
    private String OpeningHour;
    private String ClosingHour;
    private String RestaurantImage;
    private boolean FreeWIFI;
    private boolean Beverages;
    private boolean Parking;
    private boolean Takeaway;
    private List<String> CuisineTypes;
    private String UserDetailsCode;
    private String StatusCode;
    private double Latitude;
    private double Longitude;

    public RestaurantModel() {
    }

    public RestaurantModel(String ID, String restaurantCode, String restaurantName, String restaurantLocation, String locationCode, int restaurantClass, double restaurantRating, double averagePrice, String currencyCode, String restaurantTelNo, String openingHour, String closingHour, String restaurantImage, boolean freeWIFI, boolean beverages, boolean parking, boolean takeaway, List<String> cuisineTypes, String userDetailsCode, String statusCode, double latitude, double longitude) {
        this.ID = ID;
        RestaurantCode = restaurantCode;
        RestaurantName = restaurantName;
        RestaurantLocation = restaurantLocation;
        LocationCode = locationCode;
        RestaurantClass = restaurantClass;
        RestaurantRating = restaurantRating;
        AveragePrice = averagePrice;
        CurrencyCode = currencyCode;
        RestaurantTelNo = restaurantTelNo;
        OpeningHour = openingHour;
        ClosingHour = closingHour;
        RestaurantImage = restaurantImage;
        FreeWIFI = freeWIFI;
        Beverages = beverages;
        Parking = parking;
        Takeaway = takeaway;
        CuisineTypes = cuisineTypes;
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

    public String getRestaurantCode() {
        return RestaurantCode;
    }

    public void setRestaurantCode(String restaurantCode) {
        RestaurantCode = restaurantCode;
    }

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public String getRestaurantLocation() {
        return RestaurantLocation;
    }

    public void setRestaurantLocation(String restaurantLocation) {
        RestaurantLocation = restaurantLocation;
    }

    public String getLocationCode() {
        return LocationCode;
    }

    public void setLocationCode(String locationCode) {
        LocationCode = locationCode;
    }

    public int getRestaurantClass() {
        return RestaurantClass;
    }

    public void setRestaurantClass(int restaurantClass) {
        RestaurantClass = restaurantClass;
    }

    public double getRestaurantRating() {
        return RestaurantRating;
    }

    public void setRestaurantRating(double restaurantRating) {
        RestaurantRating = restaurantRating;
    }

    public double getAveragePrice() {
        return AveragePrice;
    }

    public void setAveragePrice(double averagePrice) {
        AveragePrice = averagePrice;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getRestaurantTelNo() {
        return RestaurantTelNo;
    }

    public void setRestaurantTelNo(String restaurantTelNo) {
        RestaurantTelNo = restaurantTelNo;
    }

    public String getOpeningHour() {
        return OpeningHour;
    }

    public void setOpeningHour(String openingHour) {
        OpeningHour = openingHour;
    }

    public String getClosingHour() {
        return ClosingHour;
    }

    public void setClosingHour(String closingHour) {
        ClosingHour = closingHour;
    }

    public String getRestaurantImage() {
        return RestaurantImage;
    }

    public void setRestaurantImage(String restaurantImage) {
        RestaurantImage = restaurantImage;
    }

    public boolean isFreeWIFI() {
        return FreeWIFI;
    }

    public void setFreeWIFI(boolean freeWIFI) {
        FreeWIFI = freeWIFI;
    }

    public boolean isBeverages() {
        return Beverages;
    }

    public void setBeverages(boolean beverages) {
        Beverages = beverages;
    }

    public boolean isParking() {
        return Parking;
    }

    public void setParking(boolean parking) {
        Parking = parking;
    }

    public boolean isTakeaway() {
        return Takeaway;
    }

    public void setTakeaway(boolean takeaway) {
        Takeaway = takeaway;
    }

    public List<String> getCuisineTypes() {
        return CuisineTypes;
    }

    public void setCuisineTypes(List<String> cuisineTypes) {
        CuisineTypes = cuisineTypes;
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
