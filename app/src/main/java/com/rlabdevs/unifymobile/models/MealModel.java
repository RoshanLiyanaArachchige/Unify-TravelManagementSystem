package com.rlabdevs.unifymobile.models;

public class MealModel {
    private String ID;
    private String RestaurantCode;
    private String MealCode;
    private String MealTypeCode;
    private String MealName;
    private String MealDescription;
    private int PortionSize;
    private double MealPrice;
    private String CurrencyCode;
    private String MealImage;
    private String StatusCode;

    public MealModel() {
    }

    public MealModel(String ID, String restaurantCode, String mealCode, String mealTypeCode, String mealName, String mealDescription, int portionSize, double mealPrice, String currencyCode, String mealImage, String statusCode) {
        this.ID = ID;
        RestaurantCode = restaurantCode;
        MealCode = mealCode;
        MealTypeCode = mealTypeCode;
        MealName = mealName;
        MealDescription = mealDescription;
        PortionSize = portionSize;
        MealPrice = mealPrice;
        CurrencyCode = currencyCode;
        MealImage = mealImage;
        StatusCode = statusCode;
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

    public String getMealCode() {
        return MealCode;
    }

    public void setMealCode(String mealCode) {
        MealCode = mealCode;
    }

    public String getMealTypeCode() {
        return MealTypeCode;
    }

    public void setMealTypeCode(String mealTypeCode) {
        MealTypeCode = mealTypeCode;
    }

    public String getMealName() {
        return MealName;
    }

    public void setMealName(String mealName) {
        MealName = mealName;
    }

    public String getMealDescription() {
        return MealDescription;
    }

    public void setMealDescription(String mealDescription) {
        MealDescription = mealDescription;
    }

    public int getPortionSize() {
        return PortionSize;
    }

    public void setPortionSize(int portionSize) {
        PortionSize = portionSize;
    }

    public double getMealPrice() {
        return MealPrice;
    }

    public void setMealPrice(double mealPrice) {
        MealPrice = mealPrice;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getMealImage() {
        return MealImage;
    }

    public void setMealImage(String mealImage) {
        MealImage = mealImage;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
