package com.rlabdevs.unifymobile.models.service;

public class NewServiceLocationModel {
    private Integer ServiceLocationId;
    private Integer ServiceId;
    private Integer LocationId;
    private Double Longitude;
    private Double Latitude;

    public NewServiceLocationModel() {
    }

    public NewServiceLocationModel(Integer serviceLocationId, Integer serviceId, Integer locationId, Double longitude, Double latitude) {
        ServiceLocationId = serviceLocationId;
        ServiceId = serviceId;
        LocationId = locationId;
        Longitude = longitude;
        Latitude = latitude;
    }

    public Integer getServiceLocationId() {
        return ServiceLocationId;
    }

    public void setServiceLocationId(Integer serviceLocationId) {
        ServiceLocationId = serviceLocationId;
    }

    public Integer getServiceId() {
        return ServiceId;
    }

    public void setServiceId(Integer serviceId) {
        ServiceId = serviceId;
    }

    public Integer getLocationId() {
        return LocationId;
    }

    public void setLocationId(Integer locationId) {
        LocationId = locationId;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }
}
