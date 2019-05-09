package com.example.userapp.RestaurantMenu;

public class HeaderOrMenuItem {
    private RestaurantMenuHeaderModel header;
    private RestaurantMenuItemModel menuItem;
    private boolean isHeader;

    public static HeaderOrMenuItem onCreateHeader(RestaurantMenuHeaderModel header) {
        HeaderOrMenuItem ret = new HeaderOrMenuItem();
        ret.header = header;
        ret.isHeader = true;
        return ret;
    }

    public static HeaderOrMenuItem onCreateMenuItem(RestaurantMenuItemModel menuItem) {
        HeaderOrMenuItem ret = new HeaderOrMenuItem();
        ret.menuItem = menuItem;
        ret.isHeader = false;
        return ret;
    }

    public RestaurantMenuHeaderModel getHeader() {
        return header;
    }

    public RestaurantMenuItemModel getMenuItem() {
        return menuItem;
    }

    public boolean isHeader() {
        return isHeader;
    }
}
