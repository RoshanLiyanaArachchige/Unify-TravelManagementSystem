package com.rlabdevs.unifymobile.models.master;

public class NewCuisineTypeModel {
    private Integer CuisineTypeId;
    private String Code;
    private String Name;
    private Integer StatusId;

    public NewCuisineTypeModel() {
    }

    public NewCuisineTypeModel(Integer cuisineTypeId, String code, String name, Integer statusId) {
        CuisineTypeId = cuisineTypeId;
        Code = code;
        Name = name;
        StatusId = statusId;
    }

    public Integer getCuisineTypeId() {
        return CuisineTypeId;
    }

    public void setCuisineTypeId(Integer cuisineTypeId) {
        CuisineTypeId = cuisineTypeId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getStatusId() {
        return StatusId;
    }

    public void setStatusId(Integer statusId) {
        StatusId = statusId;
    }
}
