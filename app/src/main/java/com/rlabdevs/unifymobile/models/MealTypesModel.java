package com.rlabdevs.unifymobile.models;

public class MealTypesModel {
    private String ID;
    private String RestaurantCode;
    private String MealTypeCode;
    private String MealTypeName;
    private String StatusCode;

    public MealTypesModel() {
    }

    public MealTypesModel(String ID, String restaurantCode, String mealTypeCode, String mealTypeName, String statusCode) {
        this.ID = ID;
        RestaurantCode = restaurantCode;
        MealTypeCode = mealTypeCode;
        MealTypeName = mealTypeName;
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

    public String getMealTypeName() {
        return MealTypeName;
    }

    public void setMealType(String mealTypeName) {
        MealTypeName = mealTypeName;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
