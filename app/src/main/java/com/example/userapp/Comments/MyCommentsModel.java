package com.example.userapp.Comments;

public class MyCommentsModel {
    private String commentsID;
    private String rest_name;
    private String cust_name;
    private Float ratingDeliveryService;
    private Float ratingFoodQuality;
    private String comments;

    public MyCommentsModel(String commentsID,String rest_name, String cust_name, Float ratingDeliveryService, Float ratingFoodQuality, String comments) {
        this.commentsID = commentsID;
        this.rest_name = rest_name;
        this.cust_name = cust_name;
        this.ratingDeliveryService = ratingDeliveryService;
        this.ratingFoodQuality = ratingFoodQuality;
        this.comments = comments;
    }

    public String getCommentsID() {
        return commentsID;
    }

    public void setCommentsID(String commentsID) {
        this.commentsID = commentsID;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public Float getRatingDeliveryService() {
        return ratingDeliveryService;
    }

    public void setRatingDeliveryService(Float ratingDeliveryService) {
        this.ratingDeliveryService = ratingDeliveryService;
    }

    public Float getRatingFoodQuality() {
        return ratingFoodQuality;
    }

    public void setRatingFoodQuality(Float ratingFoodQuality) {
        this.ratingFoodQuality = ratingFoodQuality;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
