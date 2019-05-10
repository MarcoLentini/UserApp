package com.example.userapp.ShoppingCart;

import java.io.Serializable;

public class OrderItemModel implements Serializable {
    //for OrderItem we need the name,price,count
    private String name;
    private double price;
    private int count;

    public OrderItemModel(String name,double price,int count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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


}
