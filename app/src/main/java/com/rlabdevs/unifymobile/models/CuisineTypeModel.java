package com.rlabdevs.unifymobile.models;

public class CuisineTypeModel {
    private String CuisineTypeCode;
    private String CuisineTypeName;

    public CuisineTypeModel() {
    }

    public CuisineTypeModel(String cuisineTypeCode, String cuisineTypeName) {
        CuisineTypeCode = cuisineTypeCode;
        CuisineTypeName = cuisineTypeName;
    }

    public String getCuisineTypeCode() {
        return CuisineTypeCode;
    }

    public void setCuisineTypeCode(String cuisineTypeCode) {
        CuisineTypeCode = cuisineTypeCode;
    }

    public String getCuisineTypeName() {
        return CuisineTypeName;
    }

    public void setCuisineTypeName(String cuisineTypeName) {
        CuisineTypeName = cuisineTypeName;
    }
}
