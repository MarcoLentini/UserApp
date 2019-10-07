package com.example.userapp.Favorites;

import com.example.userapp.Restaurant.RestaurantModel;

public class FavoritesModel {
    private String id;
    private String userID;
    private String restaurantID;
    RestaurantModel restaurantModel;

    public FavoritesModel( String userID, String restaurantID, RestaurantModel restaurantModel) {
        this.userID = userID;
        this.restaurantID = restaurantID;
        this.restaurantModel = restaurantModel;
    }

    public FavoritesModel(String id, String userID, String restaurantID, RestaurantModel restaurantModel) {
        this.id = id;
        this.userID = userID;
        this.restaurantID = restaurantID;
        this.restaurantModel = restaurantModel;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public RestaurantModel getRestaurantModel() {
        return restaurantModel;
    }

    public void setRestaurantModel(RestaurantModel restaurantModel) {
        this.restaurantModel = restaurantModel;
    }
}
