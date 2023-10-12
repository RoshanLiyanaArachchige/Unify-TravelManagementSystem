package com.rlabdevs.unifymobile.models;

import java.util.Date;

public class RoomReviewModel {
    private String ID;
    private String ReviewContent;
    private String RoomCode;
    private Date PostedDateTime;
    private String PostedByUserDetailsCode;

    public RoomReviewModel() {
    }

    public RoomReviewModel(String ID, String reviewContent, String roomCode, Date postedDateTime, String postedByUserDetailsCode) {
        this.ID = ID;
        ReviewContent = reviewContent;
        RoomCode = roomCode;
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

    public String getRoomCode() {
        return RoomCode;
    }

    public void setRoomCode(String roomCode) {
        RoomCode = roomCode;
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
