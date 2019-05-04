package com.example.userapp.restaurantMenu;

public class RestaurantMenuItemModel {

    /*Things should be showed in the cardView of restaurant menus*/
    //private int id;
    private String name;
    //private String category;
    private double price;
    //private int quantity;
    private String description;
    private int image;
    //private String state;

    public RestaurantMenuItemModel(String name, double price, String description, int image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
