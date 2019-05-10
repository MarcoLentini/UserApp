package com.example.userapp.ShoppingCart;

import java.io.Serializable;

public class OrderItemModel implements Serializable {
    //for OrderItem we need the name,price,count
    private String dish_name;
    private double dish_price;
    private int dish_qty;

    public OrderItemModel(String name,double price,int count) {
        this.dish_name = name;
        this.dish_price = price;
        this.dish_qty = count;
    }
    public int getCount() {
        return dish_qty;
    }

    public void setCount(int count) {
        this.dish_qty = count;
    }

    public String getName() {
        return dish_name;
    }

    public void setName(String name) {
        this.dish_name = name;
    }


    public double getPrice() {
        return dish_price;
    }

    public void setPrice(double price) {
        this.dish_price = price;
    }


}
