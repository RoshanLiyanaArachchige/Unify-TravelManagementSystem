package com.rlabdevs.unifymobile.models.service;

public class NewServiceHourModel {
    private Integer ServiceHourId;
    private Integer ServiceId;
    private String StartingHour;
    private String EndingHour;

    public NewServiceHourModel() {
    }

    public NewServiceHourModel(Integer serviceHourId, Integer serviceId, String startingHour, String endingHour) {
        ServiceHourId = serviceHourId;
        ServiceId = serviceId;
        StartingHour = startingHour;
        EndingHour = endingHour;
    }

    public Integer getServiceHourId() {
        return ServiceHourId;
    }

    public void setServiceHourId(Integer serviceHourId) {
        ServiceHourId = serviceHourId;
    }

    public Integer getServiceId() {
        return ServiceId;
    }

    public void setServiceId(Integer serviceId) {
        ServiceId = serviceId;
    }

    public String getStartingHour() {
        return StartingHour;
    }

    public void setStartingHour(String startingHour) {
        StartingHour = startingHour;
    }

    public String getEndingHour() {
        return EndingHour;
    }

    public void setEndingHour(String endingHour) {
        EndingHour = endingHour;
    }
}
