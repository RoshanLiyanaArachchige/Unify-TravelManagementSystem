package com.rlabdevs.unifymobile.models;

import java.util.Date;

public class MealReviewModel {
    private String ID;
    private String ReviewContent;
    private String MealCode;
    private Date PostedDateTime;
    private String PostedByUserDetailsCode;

    public MealReviewModel() {
    }

    public MealReviewModel(String ID, String reviewContent, String mealCode, Date postedDateTime, String postedByUserDetailsCode) {
        this.ID = ID;
        ReviewContent = reviewContent;
        MealCode = mealCode;
        PostedDateTime = postedDateTime;
        PostedByUserDetailsCode = postedByUserDetailsCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getReviewContent() {
        return ReviewContent;
    }

    public void setReviewContent(String reviewContent) {
        ReviewContent = reviewContent;
    }

    public String getMealCode() {
        return MealCode;
    }

    public void setMealCode(String mealCode) {
        MealCode = mealCode;
    }

    public Date getPostedDateTime() {
        return PostedDateTime;
    }

    public void setPostedDateTime(Date postedDateTime) {
        PostedDateTime = postedDateTime;
    }

    public String getPostedByUserDetailsCode() {
        return PostedByUserDetailsCode;
    }

    public void setPostedByUserDetailsCode(String postedByUserDetailsCode) {
        PostedByUserDetailsCode = postedByUserDetailsCode;
    }
}
