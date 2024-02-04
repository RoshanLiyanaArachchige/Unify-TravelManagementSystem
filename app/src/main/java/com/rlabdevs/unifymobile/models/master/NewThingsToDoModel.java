package com.rlabdevs.unifymobile.models.master;

public class NewThingsToDoModel {
    private Integer ThingsToDoId;
    private String TaskTitle;
    private String TaskDescription;
    private String TaskImage;
    private String Timings;
    private Double Latitude;
    private Double Longitude;
    private Double Distance;
    private String LocationName;
    private Double EntryFee;
    private Integer CurrencyId;

    public NewThingsToDoModel() {
    }

    public NewThingsToDoModel(Integer thingsToDoId, String taskTitle, String taskDescription, String taskImage, String timings, Double latitude, Double longitude, Double distance, String locationName, Double entryFee, Integer currencyId) {
        ThingsToDoId = thingsToDoId;
        TaskTitle = taskTitle;
        TaskDescription = taskDescription;
        TaskImage = taskImage;
        Timings = timings;
        Latitude = latitude;
        Longitude = longitude;
        Distance = distance;
        LocationName = locationName;
        EntryFee = entryFee;
        CurrencyId = currencyId;
    }

    public Integer getThingsToDoId() {
        return ThingsToDoId;
    }

    public void setThingsToDoId(Integer thingsToDoId) {
        ThingsToDoId = thingsToDoId;
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

    public Double getDistance() {
        return Distance;
    }

    public void setDistance(Double distance) {
        Distance = distance;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public Double getEntryFee() {
        return EntryFee;
    }

    public void setEntryFee(Double entryFee) {
        EntryFee = entryFee;
    }

    public Integer getCurrencyId() {
        return CurrencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        CurrencyId = currencyId;
    }
}
