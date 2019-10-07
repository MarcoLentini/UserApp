package com.example.userapp.ShoppingCart;

import java.io.Serializable;
import java.util.Objects;

public class AddressModel implements Serializable {
    private String town;
    private String street;
    private Long number;
    private String notes;

    public AddressModel(String town, String street, Long number, String notes) {
        this.town = town;
        this.street = street;
        this.number = number;
        this.notes = notes;
    }

    public AddressModel(String town, String street, Long number) {
        this.town = town;
        this.street = street;
        this.number = number;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return street + " " + number + ", "+town;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressModel that = (AddressModel) o;
        return Objects.equals(town, that.town) &&
                Objects.equals(street, that.street) &&
                Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(town, street, number, notes);
    }
}
