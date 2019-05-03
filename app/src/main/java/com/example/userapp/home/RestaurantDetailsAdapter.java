package com.example.userapp.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.userapp.R;

public class RestaurantDetailsAdapter extends FragmentPagerAdapter {
        private Context mContext;
        private static final int TAB_COUNT = 3;
        private TabRestaurantMenu tabRestaurantMenu;
        private TabRestaurantComments tabRestaurantComments;
        private TabRestaurantInfo tabRestaurantInfo;

        //constructor
        RestaurantDetailsAdapter(Context context,FragmentManager fm) {
            super(fm);
            mContext = context;
         }
        @Override
        public int getCount() {
            return TAB_COUNT;
        }
        //determine fragment for each tab
        @Override
        public Fragment getItem(int i) {
            //Returning the current tabs
            switch (i) {
                case 0:
                    tabRestaurantMenu = new TabRestaurantMenu();
                    return tabRestaurantMenu;
                case 1:
                    tabRestaurantComments = new TabRestaurantComments();
                    return tabRestaurantComments;
                case 2:
                    tabRestaurantInfo = new TabRestaurantInfo();
                    return tabRestaurantInfo;
                default:
                    return null;
            }
        }

    //determine the title for each tab
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
            //generate titile based on the position
        switch (position){
            case 0:
                return mContext.getString(R.string.menu_label);
            case 1:
                return  mContext.getString(R.string.comments_label);
            case 2:
                return  mContext.getString(R.string.information_label);
             default:
                 return  null;
        }
     }

     public TabRestaurantMenu getTabRestaurantMenu() { return tabRestaurantMenu; }
     public TabRestaurantComments getTabRestaurantComments() {
            return tabRestaurantComments;
        }
     public TabRestaurantInfo getTabRestaurantInfo() {
            return tabRestaurantInfo;
        }
}
