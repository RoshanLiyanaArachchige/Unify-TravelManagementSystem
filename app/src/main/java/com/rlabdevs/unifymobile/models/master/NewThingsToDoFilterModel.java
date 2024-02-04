package com.rlabdevs.unifymobile.models.master;

import java.util.List;

public class NewThingsToDoFilterModel {
    private Integer ApiResponseStatus;
    private String StatusMessage;
    private List<NewThingsToDoModel> ThingsToDoList;

    public NewThingsToDoFilterModel() {
    }

    public NewThingsToDoFilterModel(Integer apiResponseStatus, String statusMessage, List<NewThingsToDoModel> thingsToDoList) {
        ApiResponseStatus = apiResponseStatus;
        StatusMessage = statusMessage;
        ThingsToDoList = thingsToDoList;
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

    public List<NewThingsToDoModel> getThingsToDoList() {
        return ThingsToDoList;
    }

    public void setThingsToDoList(List<NewThingsToDoModel> thingsToDoList) {
        ThingsToDoList = thingsToDoList;
    }
}
