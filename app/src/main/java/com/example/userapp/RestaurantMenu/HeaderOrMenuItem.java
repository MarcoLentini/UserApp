package com.example.userapp.RestaurantMenu;

public class HeaderOrMenuItem {
    private RestaurantMenuHeaderModel header;
    private RestaurantMenuItemModel menuItem;
    private Long positionCat;
    private boolean isHeader;

    public static HeaderOrMenuItem onCreateHeader(RestaurantMenuHeaderModel header,Long positionCat) {
        HeaderOrMenuItem ret = new HeaderOrMenuItem();
        ret.header = header;
        ret.isHeader = true;
        ret.positionCat=positionCat;
        return ret;
    }

    public static HeaderOrMenuItem onCreateMenuItem(RestaurantMenuItemModel menuItem,Long positionCat) {
        HeaderOrMenuItem ret = new HeaderOrMenuItem();
        ret.menuItem = menuItem;
        ret.isHeader = false;
        ret.positionCat=positionCat;
        return ret;
    }

    public RestaurantMenuHeaderModel getHeader() {
        return header;
    }

    public RestaurantMenuItemModel getMenuItem() {
        return menuItem;
    }

    public Long getPositionCat() {
        return positionCat;
    }

    public boolean isHeader() {
        return isHeader;
    }
}
