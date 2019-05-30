package com.example.userapp.HistoryOrder;


import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryOrderModel implements Comparable<HistoryOrderModel>, Serializable {
    private String cust_id;
    private String rs_status;  //the status of current order

    private Long rs_id;        //order id
    private Timestamp timestamp; // order time

    private String rest_name;
    private ArrayList<HistoryOrderItemModel> dishes;
    private Double total_cost;

    private Timestamp delivery_time;  //delivery time
    private String cust_address;     //delivery address

    private Boolean is_current_order;


    public HistoryOrderModel(Boolean is_current_order,String cust_id, String rs_status, Long rs_id, Timestamp timestamp, String rest_name, ArrayList<HistoryOrderItemModel> dishes, Double total_cost,  Timestamp delivery_time, String cust_address) {
        this.is_current_order = is_current_order;
        this.cust_id = cust_id;
        this.rs_status = rs_status;
        this.rs_id = rs_id;
        this.timestamp = timestamp;
        this.rest_name = rest_name;
        this.dishes = dishes;
        this.total_cost = total_cost;
        this.delivery_time = delivery_time;
        this.cust_address = cust_address;
    }

    public Boolean getIs_current_order() {
        return is_current_order;
    }

    public void setIs_current_order(Boolean is_current_order) {
        this.is_current_order = is_current_order;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getRs_status() {
        return rs_status;
    }

    public void setRs_status(String rs_status) {
        this.rs_status = rs_status;
    }

    public Long getRs_id() {
        return rs_id;
    }

    public void setRs_id(Long rs_id) {
        this.rs_id = rs_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public ArrayList<HistoryOrderItemModel> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<HistoryOrderItemModel> dishes) {
        this.dishes = dishes;
    }

    public Double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(Double total_cost) {
        this.total_cost = total_cost;
    }


    public Timestamp getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(Timestamp delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getCust_address() {
        return cust_address;
    }

    public void setCust_address(String cust_address) {
        this.cust_address = cust_address;
    }

     @Override
    public int compareTo(HistoryOrderModel other) {
        return this.timestamp.compareTo(other.getTimestamp());
    }
}

