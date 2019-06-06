package com.example.userapp.CurrentOrder;


public class ReservatedDish {

    private String dishName;
    private Double dishPrice;
    private Long dishQty;

    public ReservatedDish(String dishName, Double dishPrice, Long dishQty) {

        this.dishName = dishName;
        this.dishPrice = dishPrice;
        this.dishQty = dishQty;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Double getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(Double dishPrice) {
        this.dishPrice = dishPrice;
    }

    public Long getDishQty() {
        return dishQty;
    }

    public void setDishQty(Long dishQty) {
        this.dishQty = dishQty;
    }
}
