package com.rlabdevs.unifymobile.models;

public class ThingsToDoModel {
    private String TaskID;
    private String TaskTitle;
    private String TaskDescription;
    private Double Latitude;
    private Double Longitude;
    private String LocationName;
    private String TaskImage;
    private String Timings;
    private Double EntryFee;
    private String CurrencyCode;
    private String StatusCode;

    public ThingsToDoModel() {
    }

    public ThingsToDoModel(String taskTitle, String taskDescription, Double latitude, Double longitude, String locationName, String taskImage, String timings, Double entryFee, String currencyCode, String statusCode) {
        TaskTitle = taskTitle;
        TaskDescription = taskDescription;
        Latitude = latitude;
        Longitude = longitude;
        LocationName = locationName;
        TaskImage = taskImage;
        Timings = timings;
        EntryFee = entryFee;
        CurrencyCode = currencyCode;
        StatusCode = statusCode;
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public String getTaskTitle() {
        return TaskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        TaskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return TaskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        TaskDescription = taskDescription;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getTaskImage() {
        return TaskImage;
    }

    public void setTaskImage(String taskImage) {
        TaskImage = taskImage;
    }

    public String getTimings() {
        return Timings;
    }

    public void setTimings(String timings) {
        Timings = timings;
    }

    public Double getEntryFee() {
        return EntryFee;
    }

    public void setEntryFee(Double entryFee) {
        EntryFee = entryFee;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
