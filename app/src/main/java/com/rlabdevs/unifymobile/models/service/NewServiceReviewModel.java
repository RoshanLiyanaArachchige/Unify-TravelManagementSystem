package com.rlabdevs.unifymobile.models.service;

public class NewServiceReviewModel {
    private Integer ServiceReviewId;
    private Integer ServiceId;
    private Integer ReviewsId;
    private String ReviewContent;
    private Integer StatusId;

    public NewServiceReviewModel() {
    }

    public NewServiceReviewModel(Integer serviceReviewId, Integer serviceId, Integer reviewsId, String reviewContent, Integer statusId) {
        ServiceReviewId = serviceReviewId;
        ServiceId = serviceId;
        ReviewsId = reviewsId;
        ReviewContent = reviewContent;
        StatusId = statusId;
    }

    public Integer getServiceReviewId() {
        return ServiceReviewId;
    }

    public void setServiceReviewId(Integer serviceReviewId) {
        ServiceReviewId = serviceReviewId;
    }

    public Integer getServiceId() {
        return ServiceId;
    }

    public void setServiceId(Integer serviceId) {
        ServiceId = serviceId;
    }

    public Integer getReviewsId() {
        return ReviewsId;
    }

    public void setReviewsId(Integer reviewsId) {
        ReviewsId = reviewsId;
    }

    public String getReviewContent() {
        return ReviewContent;
    }

    public void setReviewContent(String reviewContent) {
        ReviewContent = reviewContent;
    }

    public Integer getStatusId() {
        return StatusId;
    }

    public void setStatusId(Integer statusId) {
        StatusId = statusId;
    }
}
