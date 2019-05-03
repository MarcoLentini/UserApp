package com.example.userapp.home;

public class RestaurantModel {
    /*Things should be showed in the cardView of restaurant
    * */
    int id;
    private int restaurantLogo;
    private int monthlySales;
    private double deliveryFee;
    private double minSpend;
    private String name;
    /*
    private String emial;
    private String phone;
    private String description;
    private String address;
    private String openingHours;
    private String deliveryInfo;
    private String notification;
   */


    public RestaurantModel(int id,int restaurantLogo,String name,double deliveryFee,double minSpend,int monthlySales) {
        this.id = id;
        this.restaurantLogo = restaurantLogo;
        this.name = name;
        this.deliveryFee = deliveryFee;
        this.minSpend = minSpend;
        this.monthlySales = monthlySales;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    public double getMinSpend() {
        return minSpend;
    }

    public void setMinSpend(double minSpend) {
        this.minSpend = minSpend;
    }

    public int getRestaurantLogo() {
        return restaurantLogo;
    }

    public void setRestaurantLogo(int restaurantLogo) {
        this.restaurantLogo = restaurantLogo;
    }
    public int  getMonthlySales() {
        return monthlySales;
    }

    public void setMonthlySales(int monthlySales) {
        this.monthlySales = monthlySales;
    }

}
