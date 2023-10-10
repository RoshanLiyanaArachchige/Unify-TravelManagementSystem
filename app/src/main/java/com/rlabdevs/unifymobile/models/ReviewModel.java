package com.rlabdevs.unifymobile.models;

import com.google.type.DateTime;

public class ReviewModel {

    private String ID;
    private String ReviewCode;
    private String Title;
    private String Description;
    private double OverallRating;
    private String UserDetailsCode;
    private String RatedForCode;
    private DateTime DateTime;
    private String StatusCode;

    public ReviewModel() {
    }

    public ReviewModel(String ID, String reviewCode, String title, String description, double overallRating, String userDetailsCode, String ratedForCode, com.google.type.DateTime dateTime, String statusCode) {
        this.ID = ID;
        ReviewCode = reviewCode;
        Title = title;
        Description = description;
        OverallRating = overallRating;
        UserDetailsCode = userDetailsCode;
        RatedForCode = ratedForCode;
        DateTime = dateTime;
        StatusCode = statusCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getReviewCode() {
        return ReviewCode;
    }

    public void setReviewCode(String reviewCode) {
        ReviewCode = reviewCode;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getOverallRating() {
        return OverallRating;
    }

    public void setOverallRating(double overallRating) {
        OverallRating = overallRating;
    }

    public String getUserDetailsCode() {
        return UserDetailsCode;
    }

    public void setUserDetailsCode(String userDetailsCode) {
        UserDetailsCode = userDetailsCode;
    }

    public String getRatedForCode() {
        return RatedForCode;
    }

    public void setRatedForCode(String ratedForCode) {
        RatedForCode = ratedForCode;
    }

    public com.google.type.DateTime getDateTime() {
        return DateTime;
    }

    public void setDateTime(com.google.type.DateTime dateTime) {
        DateTime = dateTime;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
