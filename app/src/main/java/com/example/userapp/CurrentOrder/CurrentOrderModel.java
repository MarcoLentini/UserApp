package com.example.userapp.CurrentOrder;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class CurrentOrderModel implements Comparable<CurrentOrderModel>, Serializable {

    /** Reservation */
    private String orderID;
    private Long rs_id;        //order id
    private String rs_status;  //the status of current order
    private Timestamp timestamp; // order time
    private ArrayList<CurrentOrderItemModel> dishes;
    private Boolean is_current_order;
    private long confirmation_code;
    private Double total_income;
    private String notes;

    /** Customer */
    private String cust_id;
    private String cust_name;
    private String cust_address;     //delivery address
    private Boolean is_commented;

    /** Restaurant */
    private String rest_id;
    private String rest_name;

    /** Biker */
    private String biker_id;
    private Date delivery_time;  //delivery time


    public CurrentOrderModel(String orderID, Long rs_id, String rs_status,
                             Timestamp timestamp, ArrayList<CurrentOrderItemModel> dishes,
                             Boolean is_current_order, long confirmation_code,
                             Double total_income, String notes, String cust_id, String cust_name, String cust_address,
                             Boolean is_commented, String rest_id, String rest_name, String biker_id, Date delivery_time) {

        this.orderID = orderID;
        this.rs_id = rs_id;
        this.rs_status = rs_status;
        this.timestamp = timestamp;
        this.dishes = dishes;
        this.is_current_order = is_current_order;
        this.confirmation_code = confirmation_code;
        this.total_income = total_income;
        this.notes = notes;
        this.cust_id = cust_id;
        this.cust_name = cust_name;
        this.cust_address = cust_address;
        this.is_commented = is_commented;
        this.rest_id = rest_id;
        this.rest_name = rest_name;
        this.biker_id = biker_id;
        this.delivery_time = delivery_time;
    }



    @Override
    public int compareTo(CurrentOrderModel other) {
        return this.timestamp.compareTo(other.getTimestamp());
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Long getRs_id() {
        return rs_id;
    }

    public void setRs_id(Long rs_id) {
        this.rs_id = rs_id;
    }

    public String getRs_status() {
        return rs_status;
    }

    public void setRs_status(String rs_status) {
        this.rs_status = rs_status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<CurrentOrderItemModel> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<CurrentOrderItemModel> dishes) {
        this.dishes = dishes;
    }

    public Boolean getIs_current_order() {
        return is_current_order;
    }

    public void setIs_current_order(Boolean is_current_order) {
        this.is_current_order = is_current_order;
    }

    public long getConfirmation_code() {
        return confirmation_code;
    }

    public void setConfirmation_code(long confirmation_code) {
        this.confirmation_code = confirmation_code;
    }

    public Double getTotal_income() {
        return total_income;
    }

    public void setTotal_income(Double total_income) {
        this.total_income = total_income;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
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

    public Date getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(Date delivery_time) {
        this.delivery_time = delivery_time;
    }

    public Boolean getIs_commented() {
        return is_commented;
    }

    public void setIs_commented(Boolean is_commented) {
        this.is_commented = is_commented;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getBiker_id() {
        return biker_id;
    }

    public void setBiker_id(String biker_id) {
        this.biker_id = biker_id;
    }
}
