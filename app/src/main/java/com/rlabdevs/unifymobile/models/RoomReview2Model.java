package com.rlabdevs.unifymobile.models;

public class RoomReview2Model {

    private String ID;
    private String RoomReviewCode;
    private String ReviewCode;
    private int ValueRating;
    private int RoomRating;
    private int LocationRating;
    private int CleanlinessRating;
    private int SleepQualityRating;
    private int ServicesAndFacilitiesRating;
    private String StatusCode;

    public RoomReview2Model() {
    }

    public RoomReview2Model(String ID, String roomReviewCode, String reviewCode, int valueRating, int roomRating, int locationRating, int cleanlinessRating, int sleepQualityRating, int servicesAndFacilitiesRating, String statusCode) {
        this.ID = ID;
        RoomReviewCode = roomReviewCode;
        ReviewCode = reviewCode;
        ValueRating = valueRating;
        RoomRating = roomRating;
        LocationRating = locationRating;
        CleanlinessRating = cleanlinessRating;
        SleepQualityRating = sleepQualityRating;
        ServicesAndFacilitiesRating = servicesAndFacilitiesRating;
        StatusCode = statusCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRoomReviewCode() {
        return RoomReviewCode;
    }

    public void setRoomReviewCode(String roomReviewCode) {
        RoomReviewCode = roomReviewCode;
    }

    public String getReviewCode() {
        return ReviewCode;
    }

    public void setReviewCode(String reviewCode) {
        ReviewCode = reviewCode;
    }

    public int getValueRating() {
        return ValueRating;
    }

    public void setValueRating(int valueRating) {
        ValueRating = valueRating;
    }

    public int getRoomRating() {
        return RoomRating;
    }

    public void setRoomRating(int roomRating) {
        RoomRating = roomRating;
    }

    public int getLocationRating() {
        return LocationRating;
    }

    public void setLocationRating(int locationRating) {
        LocationRating = locationRating;
    }

    public int getCleanlinessRating() {
        return CleanlinessRating;
    }

    public void setCleanlinessRating(int cleanlinessRating) {
        CleanlinessRating = cleanlinessRating;
    }

    public int getSleepQualityRating() {
        return SleepQualityRating;
    }

    public void setSleepQualityRating(int sleepQualityRating) {
        SleepQualityRating = sleepQualityRating;
    }

    public int getServicesAndFacilitiesRating() {
        return ServicesAndFacilitiesRating;
    }

    public void setServicesAndFacilitiesRating(int servicesAndFacilitiesRating) {
        ServicesAndFacilitiesRating = servicesAndFacilitiesRating;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
