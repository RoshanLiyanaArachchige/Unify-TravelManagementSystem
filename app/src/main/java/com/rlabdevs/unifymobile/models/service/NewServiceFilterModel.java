package com.rlabdevs.unifymobile.models.service;

import androidx.annotation.NonNull;

public class NewServiceFilterModel {
    private Integer UserId;
    private Integer LocationId;
    private Integer BudgetRangeId;
    private Integer Class;
    private Integer ApiResponseStatus;
    private String StatusMessage;

    public NewServiceFilterModel() {
    }

    public NewServiceFilterModel(Integer userId, Integer locationId, Integer budgetRangeId, Integer aClass) {
        UserId = userId;
        LocationId = locationId;
        BudgetRangeId = budgetRangeId;
        Class = aClass;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public Integer getLocationId() {
        return LocationId;
    }

    public void setLocationId(Integer locationId) {
        LocationId = locationId;
    }

    public Integer getBudgetRangeId() {
        return BudgetRangeId;
    }

    public void setBudgetRangeId(Integer budgetRangeId) {
        BudgetRangeId = budgetRangeId;
    }

    public Integer getServiceClass() {
        return Class;
    }

    public void setServiceClass(Integer aClass) {
        Class = aClass;
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
