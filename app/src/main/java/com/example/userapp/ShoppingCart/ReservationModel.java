package com.example.userapp.ShoppingCart;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class ReservationModel implements Comparable<ReservationModel>, Serializable {

    /*private int id;
    private int customerId;
    private int remainingMinutes;
    private String notes;
    private String customerPhoneNumber;
    private ArrayList<ReservatedDish> reservatedDishes;
    private String state;
    private double totalIncome;*/

    private Long rs_id;
    private String rs_status;

    private String cust_id;
    private String cust_name;
    private String cust_phone;
    private String cust_address;

    private Timestamp timestamp;
    private String notes;

    private String rest_id;
    private String rest_name;
    private String rest_address;

    private ArrayList<OrderItemModel> dishes;
    private Double total_income;



    public ReservationModel(String cust_id, Timestamp timestamp, String notes, ArrayList<OrderItemModel> dishesArrayList, Double total_income, String rest_id, String rest_name, String rest_address) {
        this.rs_id = null;
        this.rs_status = "PENDING";
        this.cust_id = cust_id;
        this.timestamp = timestamp;
        this.notes = notes;
        this.dishes = dishesArrayList;
        this.rs_status = rs_status;
        this.total_income = total_income;

        this.rest_id = rest_id;
        this.rest_address = rest_address;
        this.rest_name = rest_name;

    }

    public Long getRs_id() {
        return rs_id;
    }

    public void setRs_id(Long rs_id) {
        this.rs_id = rs_id;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCust_phone() {
        return cust_phone;
    }

    public void setCust_phone(String cust_phone) {
        this.cust_phone = cust_phone;
    }

    public String getRs_status() {
        return rs_status;
    }

    public void setRs_status(String rs_status) {
        this.rs_status = rs_status;
    }

    public Double getTotal_income() {
        return total_income;
    }

    public void setTotal_income(Double total_income) {
        this.total_income = total_income;
    }


    @Override
    public int compareTo(ReservationModel other) {
        return this.timestamp.compareTo(other.getTimestamp());
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_address() {
        return cust_address;
    }

    public void setCust_address(String cust_address) {
        this.cust_address = cust_address;
    }

    public String getRest_id() {
        return rest_id;
    }

    public void setRest_id(String rest_id) {
        this.rest_id = rest_id;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public String getRest_address() {
        return rest_address;
    }

    public void setRest_address(String rest_address) {
        this.rest_address = rest_address;
    }

    public ArrayList<OrderItemModel> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<OrderItemModel> dishes) {
        this.dishes = dishes;
    }
}
