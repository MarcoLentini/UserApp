package com.example.userapp.CurrentOrder;

public class CurrentOrderItemModel {
    //for OrderItem we need the name,price,count
    private String dish_name;
    private double dish_price;
    private long dish_qty;

    public CurrentOrderItemModel(String dish_name, double dish_price, long dish_qty) {
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

    public long getDish_qty() {
        return dish_qty;
    }

    public void setDish_qty(int dish_qty) {
        this.dish_qty = dish_qty;
    }
}
