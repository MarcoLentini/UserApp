package com.example.userapp.restaurant;

public class RestaurantModel {

    private String id;
    private String name;
    private double deliveryFee;
    private String address;
    private String description;
    private String restaurantLogo;


    public RestaurantModel(String id, String name, double deliveryFee, String address, String description, String restaurantLogo) {
        this.id = id;
        this.name = name;
        this.deliveryFee = deliveryFee;
        this.address = address;
        this.description = description;
        this.restaurantLogo = restaurantLogo;
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
}
