package com.rlabdevs.unifymobile.models;

public class LocationModel {
    private String LocationCode;
    private String LocationName;
    private String StatusCode;

    public LocationModel() {
    }

    public LocationModel(String locationCode, String locationName, String statusCode) {
        LocationCode = locationCode;
        LocationName = locationName;
        StatusCode = statusCode;
    }

    public String getLocationCode() {
        return LocationCode;
    }

    public void setLocationCode(String locationCode) {
        LocationCode = locationCode;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
