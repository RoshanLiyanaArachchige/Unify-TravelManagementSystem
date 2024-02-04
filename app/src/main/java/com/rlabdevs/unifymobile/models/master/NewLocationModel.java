package com.rlabdevs.unifymobile.models.master;

public class NewLocationModel {
    private Integer LocationId;
    private String Code;
    private String Name;

    public NewLocationModel() {
    }

    public NewLocationModel(Integer locationId, String code, String name) {
        LocationId = locationId;
        Code = code;
        Name = name;
    }

    public Integer getLocationId() {
        return LocationId;
    }

    public void setLocationId(Integer locationId) {
        LocationId = locationId;
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
}
