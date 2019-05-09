package com.example.userapp.Restaurant;


import java.io.Serializable;
import java.util.ArrayList;

public class RestaurantModel implements Serializable {

    private String id;
    private String name;
    private double deliveryFee;
    private String address;
    private String description;
    private String restaurantLogo;
    private ArrayList<String> tags;


    public RestaurantModel(String id, String name, String address, String description, String restaurantLogo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.restaurantLogo = restaurantLogo;
    }


    public RestaurantModel(String id, String name, double deliveryFee, String address, String description, String restaurantLogo, ArrayList<String> tags) {
        this.id = id;
        this.name = name;
        this.deliveryFee = deliveryFee;
        this.address = address;
        this.description = description;
        this.restaurantLogo = restaurantLogo;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestaurantLogo() {
        return restaurantLogo;
    }

    public void setRestaurantLogo(String restaurantLogo) {
        this.restaurantLogo = restaurantLogo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
