package com.rlabdevs.unifymobile.models.service;

import java.util.List;

public class NewServiceModel {
    private Integer ServiceId;
    private String Code;
    private String Name;
    private String Description;
    private String ImagePath;
    private String TelephoneNo;
    private Double StartingPrice;
    private Integer CurrencyId;
    private Double Rating;
    private Integer StatusId;
    private NewServiceLocationModel ServiceLocation;
    private NewServiceClassModel ServiceClass;
    private NewServiceHourModel ServiceHour;
    private List<NewServiceAmenityModel> ServiceAmenities;
    private List<NewServiceReviewModel> ServiceReviews;

    private Integer UserId;
    private Integer ApiResponseStatus;
    private String StatusMessage;

    public NewServiceModel() {
    }

    public NewServiceModel(Integer serviceId, String code, String name, String description, String imagePath, String telephoneNo, Double startingPrice, Integer currencyId, Double rating, Integer statusId, NewServiceLocationModel serviceLocation, NewServiceClassModel serviceClass, NewServiceHourModel serviceHour, List<NewServiceAmenityModel> serviceAmenities, List<NewServiceReviewModel> serviceReviews, Integer apiResponseStatus, String statusMessage) {
        ServiceId = serviceId;
        Code = code;
        Name = name;
        Description = description;
        ImagePath = imagePath;
        TelephoneNo = telephoneNo;
        StartingPrice = startingPrice;
        CurrencyId = currencyId;
        Rating = rating;
        StatusId = statusId;
        ServiceLocation = serviceLocation;
        ServiceClass = serviceClass;
        ServiceHour = serviceHour;
        ServiceAmenities = serviceAmenities;
        ServiceReviews = serviceReviews;
        ApiResponseStatus = apiResponseStatus;
        StatusMessage = statusMessage;
    }

    public Integer getServiceId() {
        return ServiceId;
    }

    public void setServiceId(Integer serviceId) {
        ServiceId = serviceId;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getTelephoneNo() {
        return TelephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        TelephoneNo = telephoneNo;
    }

    public Double getStartingPrice() {
        return StartingPrice;
    }

    public void setStartingPrice(Double startingPrice) {
        StartingPrice = startingPrice;
    }

    public Integer getCurrencyId() {
        return CurrencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        CurrencyId = currencyId;
    }

    public Double getRating() {
        return Rating;
    }

    public void setRating(Double rating) {
        Rating = rating;
    }

    public Integer getStatusId() {
        return StatusId;
    }

    public void setStatusId(Integer statusId) {
        StatusId = statusId;
    }

    public NewServiceLocationModel getServiceLocation() {
        return ServiceLocation;
    }

    public void setServiceLocation(NewServiceLocationModel serviceLocation) {
        ServiceLocation = serviceLocation;
    }

    public NewServiceClassModel getServiceClass() {
        return ServiceClass;
    }

    public void setServiceClass(NewServiceClassModel serviceClass) {
        ServiceClass = serviceClass;
    }

    public NewServiceHourModel getServiceHour() {
        return ServiceHour;
    }

    public void setServiceHour(NewServiceHourModel serviceHour) {
        ServiceHour = serviceHour;
    }

    public List<NewServiceAmenityModel> getServiceAmenities() {
        return ServiceAmenities;
    }

    public void setServiceAmenities(List<NewServiceAmenityModel> serviceAmenities) {
        ServiceAmenities = serviceAmenities;
    }

    public List<NewServiceReviewModel> getServiceReviews() {
        return ServiceReviews;
    }

    public void setServiceReviews(List<NewServiceReviewModel> serviceReviews) {
        ServiceReviews = serviceReviews;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public Integer getApiResponseStatus() {
        return ApiResponseStatus;
    }

    public void setApiResponseStatus(Integer apiResponseStatus) {
        ApiResponseStatus = apiResponseStatus;
    }

    public String getStatusMessage() {
        return StatusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        StatusMessage = statusMessage;
    }
}
