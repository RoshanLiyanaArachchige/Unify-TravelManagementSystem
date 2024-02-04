package com.rlabdevs.unifymobile.models.service;

public class NewServiceAmenityModel {
    private Integer ServiceAmenitiesId;
    private Integer ServiceId;
    private Integer AppliedAmenitiesId;
    private Integer AmenityId;

    public NewServiceAmenityModel() {
    }

    public NewServiceAmenityModel(Integer serviceAmenitiesId, Integer serviceId, Integer appliedAmenitiesId, Integer amenityId) {
        ServiceAmenitiesId = serviceAmenitiesId;
        ServiceId = serviceId;
        AppliedAmenitiesId = appliedAmenitiesId;
        AmenityId = amenityId;
    }

    public Integer getServiceAmenitiesId() {
        return ServiceAmenitiesId;
    }

    public void setServiceAmenitiesId(Integer serviceAmenitiesId) {
        ServiceAmenitiesId = serviceAmenitiesId;
    }

    public Integer getServiceId() {
        return ServiceId;
    }

    public void setServiceId(Integer serviceId) {
        ServiceId = serviceId;
    }

    public Integer getAppliedAmenitiesId() {
        return AppliedAmenitiesId;
    }

    public void setAppliedAmenitiesId(Integer appliedAmenitiesId) {
        AppliedAmenitiesId = appliedAmenitiesId;
    }

    public Integer getAmenityId() {
        return AmenityId;
    }

    public void setAmenityId(Integer amenityId) {
        AmenityId = amenityId;
    }
}
