package com.example.userapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.userapp.R;
import com.example.userapp.home.Restaurant;
import com.example.userapp.home.RestaurantsListAdapter;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private final static String TAG ="HomeFragment";
    private RecyclerView.Adapter restaurantsAdapter;
    private ArrayList<Restaurant> restaurantsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(...) chiamato una volta sola!");
        restaurantsList = new ArrayList<>(MainActivity.restaurantsData);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(...) chiamato !");

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycleView_shops);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
         restaurantsAdapter= new RestaurantsListAdapter(getContext(),restaurantsList);
        recyclerView.setAdapter(restaurantsAdapter);
        return view;
    }
}
