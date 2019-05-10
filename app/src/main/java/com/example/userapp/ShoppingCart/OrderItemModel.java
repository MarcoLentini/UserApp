package com.example.userapp.ShoppingCart;

import java.io.Serializable;

public class OrderItemModel implements Serializable {
    //for OrderItem we need the name,price,count
    private String dish_name;
    private double dish_price;
    private int dish_qty;

    public OrderItemModel(String dish_name, double dish_price, int dish_qty) {
        this.dish_name = dish_name;
        this.dish_price = dish_price;
        this.dish_qty = dish_qty;
    }


    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public double getDish_price() {
        return dish_price;
    }

    public void setDish_price(double dish_price) {
        this.dish_price = dish_price;
    }

    public int getDish_qty() {
        return dish_qty;
    }

    public void setDish_qty(int dish_qty) {
        this.dish_qty = dish_qty;
    }
}
