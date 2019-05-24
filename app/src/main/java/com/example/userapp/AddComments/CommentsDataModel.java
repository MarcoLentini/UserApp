package com.example.userapp.AddComments;

public class CommentsDataModel {
    private String restId;
    private String bikerId;
    private String userId;
    private Double voteForRestaurant;
    private Double voteForBiker;
    private String notes;

    public CommentsDataModel(String restId, String bikerId, String userId, Double voteForRestaurant, Double voteForBiker, String notes) {
        this.restId = restId;
        this.bikerId = bikerId;
        this.userId = userId;
        this.voteForRestaurant = voteForRestaurant;
        this.voteForBiker = voteForBiker;
        this.notes = notes;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getBikerId() {
        return bikerId;
    }

    public void setBikerId(String bikerId) {
        this.bikerId = bikerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getVoteForRestaurant() {
        return voteForRestaurant;
    }

    public void setVoteForRestaurant(Double voteForRestaurant) {
        this.voteForRestaurant = voteForRestaurant;
    }

    public Double getVoteForBiker() {
        return voteForBiker;
    }

    public void setVoteForBiker(Double voteForBiker) {
        this.voteForBiker = voteForBiker;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
