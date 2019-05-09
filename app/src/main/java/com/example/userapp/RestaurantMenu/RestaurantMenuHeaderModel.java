package com.example.userapp.RestaurantMenu;

public class RestaurantMenuHeaderModel {

    private String headerId;
    private String headerName;

    public RestaurantMenuHeaderModel(String headerId, String headerName) {
        this.headerId = headerId;
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }
}
