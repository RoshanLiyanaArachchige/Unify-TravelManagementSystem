package com.rlabdevs.unifymobile.models;

public class MealTypesModel {
    private String ID;
    private String RestaurantCode;
    private String MealTypeCode;
    private String MealType;
    private String StatusCode;

    public MealTypesModel() {
    }

    public MealTypesModel(String ID, String restaurantCode, String mealTypeCode, String mealType, String statusCode) {
        this.ID = ID;
        RestaurantCode = restaurantCode;
        MealTypeCode = mealTypeCode;
        MealType = mealType;
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

    public String getMealTypeCode() {
        return MealTypeCode;
    }

    public void setMealTypeCode(String mealTypeCode) {
        MealTypeCode = mealTypeCode;
    }

    public String getMealType() {
        return MealType;
    }

    public void setMealType(String mealType) {
        MealType = mealType;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
