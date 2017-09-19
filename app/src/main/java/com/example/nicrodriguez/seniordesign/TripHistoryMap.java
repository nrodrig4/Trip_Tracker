package com.example.nicrodriguez.seniordesign;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.w3c.dom.Text;


public class TripHistoryMap extends Fragment {
    public static MapView mapViewPop;
    public static MapboxMap map;
    public static TextView tripLabel, tripLengthLabel, averageSpeedLabel, maxSpeedLabel, startTime, endTime;
    public static ToggleButton updateButton; //deleteTrip,
    public static Button refreshButton,deleteButton;

     public TripHistoryMap() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Mapbox.getInstance(getContext(), "pk.eyJ1IjoibnJvZHJpZzQiLCJhIjoiY2o0aWt3NXNjMGFqdTMycm05eXplYWF0dCJ9.hYRKULV0QG9ouTT-olZrrg");

        tripLabel = view.findViewById(R.id.tripLabelPop);
        tripLengthLabel = view.findViewById(R.id.tripLengthView);
        startTime = view.findViewById(R.id.tripS);
        endTime = view.findViewById(R.id.tripE);
        averageSpeedLabel = view.findViewById(R.id.averageSpeedView);
        maxSpeedLabel = view.findViewById(R.id.maxSpeedView);
//        deleteTrip =  view.findViewById(R.id.deleteToggle);
        mapViewPop = view.findViewById(R.id.mapViewPop);
        refreshButton = view.findViewById(R.id.refreshButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        updateButton = view.findViewById(R.id.updateListButton);
        mapViewPop.setStyleUrl(Style.MAPBOX_STREETS);
        mapViewPop.onCreate(savedInstanceState);

        mapViewPop.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;




            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_history_map, container, false);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        map.clear();
//        mapViewPop.onDestroy();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        mapViewPop.onStart();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapViewPop.onPause();
//
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        mapViewPop.onStop();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapViewPop.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//    }




}
