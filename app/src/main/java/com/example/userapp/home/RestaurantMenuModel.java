package com.example.userapp.home;

public class RestaurantMenuModel {
    /*Things should be showed in the cardView of restaurant menus*/
    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String description;
    private int image;
    private String state;


    public RestaurantMenuModel(int id, String name, String category, double price, int quantity, int image, String state, String description) {
        this.id = id;
        this.name = name;
        this.category=category;
        this.price = price;
        this.quantity=quantity;
        this.image = image;
        this.state=state;
        this.description=description;

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
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
