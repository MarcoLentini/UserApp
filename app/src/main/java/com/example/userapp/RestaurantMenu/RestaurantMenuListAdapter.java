package com.example.userapp.RestaurantMenu;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.userapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RestaurantMenuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = "RestaurantMenuListAdapter";
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
            DecimalFormat format = new DecimalFormat("0.00");
            String formattedPrice = format.format(menuItem.getPrice());
            textViewMenuItemPrice.setText("€ " +formattedPrice);

            //adding for shopping cart
            ImageView imageViewItemAdd = menuItemViewHolder.imageViewItemAdd;
            ImageView imageViewItemRemove = menuItemViewHolder.imageViewItemRemove;
            TextView textViewItemCount = menuItemViewHolder.textViewItemCount;
            TextView textViewItemCountPiù = menuItemViewHolder.textViewItemCountPiù;

            if(dataSet.get(position)!=null && !dataSet.get(position).isHeader()){

                RestaurantMenuItemModel menuItem1 = dataSet.get(position).getMenuItem();
                int count = ((RestaurantMenuActivity)context).getSelectedItemCountById(menuItem1.getCategoryId()+"_"+menuItem1.getName());
                if (count == 0 ){
                    textViewItemCount.setVisibility(View.INVISIBLE);
                    textViewItemCountPiù.setVisibility(View.INVISIBLE);
                    imageViewItemRemove.setVisibility(View.INVISIBLE);
                }else{
                    textViewItemCount.setVisibility(View.VISIBLE);
                    textViewItemCountPiù.setVisibility(View.VISIBLE);
                    imageViewItemRemove.setVisibility(View.VISIBLE);
                    textViewItemCount.setText(String.valueOf(count));
                    textViewItemCountPiù.setText(String.valueOf(count));


                }
            }else{
                textViewItemCount.setVisibility(View.INVISIBLE);
                textViewItemCountPiù.setVisibility(View.INVISIBLE);

                imageViewItemRemove.setVisibility(View.INVISIBLE);
            }



            imageViewItemAdd.setOnClickListener(v -> {
                int count = ((RestaurantMenuActivity)context).getSelectedItemCountById(menuItem.getCategoryId()+"_"+menuItem.getName());
                /*
                imageViewItemRemove.setVisibility(View.VISIBLE);
                textViewItemCount.setVisibility(View.VISIBLE);
                textViewItemCount.setText(String.valueOf(count));
               */
                Log.i("TAG","Add one more for current item"+String.valueOf(count));
                if (count < 1){
                    imageViewItemRemove.setVisibility(View.VISIBLE);
                    textViewItemCount.setVisibility(View.VISIBLE);
                    textViewItemCountPiù.setVisibility(View.VISIBLE);

                }

                ((RestaurantMenuActivity)context).handlerShoppingCarNum(1,menuItem,true);

            });

            imageViewItemRemove.setOnClickListener(v -> {
                int count = ((RestaurantMenuActivity)context).getSelectedItemCountById(menuItem.getCategoryId()+"_"+menuItem.getName());
                Log.i("TAG","Remove one for current item "+String.valueOf(count));
                if (count < 2) {
                    imageViewItemRemove.setVisibility(View.GONE);
                    textViewItemCount.setVisibility(View.GONE);
                    textViewItemCountPiù.setVisibility(View.GONE);
                }
                ((RestaurantMenuActivity)context).handlerShoppingCarNum(0,menuItem,true);
            });

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

        //adding for shopping cart
        ImageView imageViewItemAdd;
        ImageView imageViewItemRemove;
        TextView textViewItemCount;
        TextView textViewItemCountPiù;

        public MenuItemViewHolder(View itemView) {
            super(itemView);
            this.imageViewMenuItemLogo = itemView.findViewById(R.id.textViewMenuItemLogo);
            this.textViewMenuItemName = itemView.findViewById(R.id.textViewMenuItemName);
            this.textViewMenuItemDescription = itemView.findViewById(R.id.textViewMenuItemDescription);
            this.textViewMenuItemPrice = itemView.findViewById(R.id.textViewMenuItemPrice);

            //adding for shopping cart
            this.textViewItemCount = itemView.findViewById(R.id.cv_tv_acount);
            this.textViewItemCountPiù = itemView.findViewById(R.id.cv_tv_acount_piu);
            this.imageViewItemAdd = itemView.findViewById(R.id.cv_iv_add);
            this.imageViewItemRemove = itemView.findViewById(R.id.cv_iv_remove);
        }
    }
}
