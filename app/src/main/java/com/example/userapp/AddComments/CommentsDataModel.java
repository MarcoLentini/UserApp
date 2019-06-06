package com.example.userapp.AddComments;

public class CommentsDataModel {
    private String custName;
    private String commentsId;
    private Long reservationId;
    private String restId;
    private String bikerId;
    private String userId;
    private Float voteForRestaurant;
    private Float voteForBiker;
    private String notes;
    private String restName;


    public CommentsDataModel(String custName,String commentsId,Long reservationId, String restId, String restName,
                             String bikerId, String userId, Float voteForRestaurant, Float voteForBiker, String notes) {
        this.custName = custName;
        this.commentsId = commentsId;
        this.reservationId = reservationId;
        this.restId = restId;
        this.restName = restName;
        this.bikerId = bikerId;
        this.userId = userId;
        this.voteForRestaurant = voteForRestaurant;
        this.voteForBiker = voteForBiker;
        this.notes = notes;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(String commentsId) {
        this.commentsId = commentsId;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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

    public Float getVoteForRestaurant() {
        return voteForRestaurant;
    }

    public void setVoteForRestaurant(Float voteForRestaurant) {
        this.voteForRestaurant = voteForRestaurant;
    }

    public Float getVoteForBiker() {
        return voteForBiker;
    }

    public void setVoteForBiker(Float voteForBiker) {
        this.voteForBiker = voteForBiker;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "CommentsDataModel{" +
                "commentsId='" + commentsId + '\'' +
                ", reservationId=" + reservationId +
                ", restId='" + restId + '\'' +
                ", bikerId='" + bikerId + '\'' +
                ", userId='" + userId + '\'' +
                ", voteForRestaurant=" + voteForRestaurant +
                ", voteForBiker=" + voteForBiker +
                ", notes='" + notes + '\'' +
                ", restName='" + restName + '\'' +
                '}';
    }
}
