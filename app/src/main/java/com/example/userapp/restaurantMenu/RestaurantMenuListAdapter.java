package com.example.userapp.restaurantMenu;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.bumptech.glide.Glide;
import com.example.userapp.MainActivity;
import com.example.userapp.R;

public class RestaurantMenuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private static ArrayList<HeaderOrMenuItem> dataSet;
    private LayoutInflater mInflater;

    public RestaurantMenuListAdapter(Context context, ArrayList<HeaderOrMenuItem> restaurantMenuData){
        this.context = context;
        this.dataSet = restaurantMenuData;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0) {
            View v = mInflater.inflate(R.layout.cardview_restaurant_menu_header, parent,false);
            return new HeaderViewHolder(v);
        } else {
            View v = mInflater.inflate(R.layout.cardview_restaurant_menu_item, parent,false);
            return new MenuItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        HeaderOrMenuItem item = dataSet.get(position);
        if(item.isHeader()) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            TextView textViewHeader = headerViewHolder.textViewHeader;

            RestaurantMenuHeaderModel header = item.getHeader();
            textViewHeader.setText(header.getHeaderName());
        } else {
            MenuItemViewHolder menuItemViewHolder = (MenuItemViewHolder) viewHolder;
            ImageView imageViewMenuItemLogo = menuItemViewHolder.imageViewMenuItemLogo;
            TextView textViewMenuItemName = menuItemViewHolder.textViewMenuItemName;
            TextView textViewMenuItemDescription = menuItemViewHolder.textViewMenuItemDescription;
            TextView textViewMenuItemPrice = menuItemViewHolder.textViewMenuItemPrice;

            RestaurantMenuItemModel menuItem = item.getMenuItem();
            Uri tmpUri = Uri.parse(menuItem.getImage());
            Glide.with(context).load(tmpUri).placeholder(R.drawable.img_rest_1).into(imageViewMenuItemLogo);
            textViewMenuItemName.setText(menuItem.getName());
            textViewMenuItemDescription.setText(menuItem.getDescription());
            textViewMenuItemPrice.setText(String.valueOf(menuItem.getPrice()));
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        HeaderOrMenuItem item = dataSet.get(position);
        if(item.isHeader())
            return 0; // 0 means header
        else
            return 1; // 1 means menuItem
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textViewHeader = (TextView) itemView.findViewById(R.id.textViewHeaderName);
        }
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMenuItemLogo;
        TextView textViewMenuItemName;
        TextView textViewMenuItemDescription;
        TextView textViewMenuItemPrice;

        public MenuItemViewHolder(View itemView) {
            super(itemView);
            this.imageViewMenuItemLogo = itemView.findViewById(R.id.textViewMenuItemLogo);
            this.textViewMenuItemName = itemView.findViewById(R.id.textViewMenuItemName);
            this.textViewMenuItemDescription = itemView.findViewById(R.id.textViewMenuItemDescription);
            this.textViewMenuItemPrice = itemView.findViewById(R.id.textViewMenuItemPrice);
        }
    }
}
