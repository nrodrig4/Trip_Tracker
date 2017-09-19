package com.example.nicrodriguez.seniordesign;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class TravelHistoryPopup extends AppCompatActivity {




    public static AlertDialog.Builder deleteDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_history_popup);
        setTitle(UserLogin.selectedUser+"'s Travel History");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.activity_app_bar));

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setCancelable(false);

        TripHistoryMap.refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TravelHistoryPopup.this,TravelHistoryPopup.class));
            }
        });

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int)(width*0.9),(int)(height*0.9));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.travel_history_p, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.map_type_streets:
                    if(hasInternetConnection(this)) {
                        TripHistoryMap.map.setStyle(Style.MAPBOX_STREETS);
                        TripHistoryMap.tripLengthLabel.setTextColor(Color.BLACK);
                        TripHistoryMap.startTime.setTextColor(Color.BLACK);
                        TripHistoryMap.endTime.setTextColor(Color.BLACK);
                        TripHistoryMap.averageSpeedLabel.setTextColor(Color.BLACK);
                        TripHistoryMap.maxSpeedLabel.setTextColor(Color.BLACK);
                    }else{
                        Toast.makeText(this,"NEED TO CONNECT TO THE INTERNET TO CHANGE MAP STYLES",Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.map_type_hybrid:
                    if(hasInternetConnection(this)) {
                        TripHistoryMap.map.setStyle(Style.SATELLITE_STREETS);
                        TripHistoryMap.tripLengthLabel.setTextColor(Color.WHITE);
                        TripHistoryMap.startTime.setTextColor(Color.WHITE);
                        TripHistoryMap.endTime.setTextColor(Color.WHITE);
                        TripHistoryMap.averageSpeedLabel.setTextColor(Color.WHITE);
                        TripHistoryMap.maxSpeedLabel.setTextColor(Color.WHITE);
                    }else{
                        Toast.makeText(this,"NEED TO CONNECT TO THE INTERNET TO CHANGE MAP STYLES",Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.map_type_satellite:
                    if(hasInternetConnection(this)) {
                        TripHistoryMap.map.setStyle(Style.SATELLITE);
                        TripHistoryMap.tripLengthLabel.setTextColor(Color.WHITE);
                        TripHistoryMap.startTime.setTextColor(Color.WHITE);
                        TripHistoryMap.endTime.setTextColor(Color.WHITE);
                        TripHistoryMap.averageSpeedLabel.setTextColor(Color.WHITE);
                        TripHistoryMap.maxSpeedLabel.setTextColor(Color.WHITE);
                    }else{
                        Toast.makeText(this,"NEED TO CONNECT TO THE INTERNET TO CHANGE MAP STYLES",Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.map_type_terrain:
                    if(hasInternetConnection(this)) {
                        TripHistoryMap.map.setStyle(Style.OUTDOORS);
                        TripHistoryMap.tripLengthLabel.setTextColor(Color.BLACK);
                        TripHistoryMap.startTime.setTextColor(Color.BLACK);
                        TripHistoryMap.endTime.setTextColor(Color.BLACK);
                        TripHistoryMap.averageSpeedLabel.setTextColor(Color.BLACK);
                        TripHistoryMap.maxSpeedLabel.setTextColor(Color.BLACK);
                    }else{
                        Toast.makeText(this,"NEED TO CONNECT TO THE INTERNET TO CHANGE MAP STYLES",Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.map_type_dark:
                    if(hasInternetConnection(this)) {
                        TripHistoryMap.map.setStyle(Style.DARK);
                        TripHistoryMap.tripLengthLabel.setTextColor(Color.WHITE);
                        TripHistoryMap.startTime.setTextColor(Color.WHITE);
                        TripHistoryMap.endTime.setTextColor(Color.WHITE);
                        TripHistoryMap.averageSpeedLabel.setTextColor(Color.WHITE);
                        TripHistoryMap.maxSpeedLabel.setTextColor(Color.WHITE);
                    }else{
                        Toast.makeText(this,"NEED TO CONNECT TO THE INTERNET TO CHANGE MAP STYLES",Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.map_type_tron:
                    if(hasInternetConnection(this)) {
                        TripHistoryMap.map.setStyleUrl("mapbox://styles/nrodrig4/cj656qa8s5xzb2rnzbotjp1be");
                        TripHistoryMap.tripLengthLabel.setTextColor(Color.WHITE);
                        TripHistoryMap.startTime.setTextColor(Color.WHITE);
                        TripHistoryMap.endTime.setTextColor(Color.WHITE);
                        TripHistoryMap.averageSpeedLabel.setTextColor(Color.WHITE);
                        TripHistoryMap.maxSpeedLabel.setTextColor(Color.WHITE);
                    }else{
                    Toast.makeText(this,"NEED TO CONNECT TO THE INTERNET TO CHANGE MAP STYLES",Toast.LENGTH_LONG).show();
                }
                    break;
            }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_travel_history_popup);
    }

    public static boolean hasInternetConnection(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager)context.
                getSystemService(Context.CONNECTIVITY_SERVICE);

        final Network network = connectivityManager.getActiveNetwork();
        final NetworkCapabilities capabilities = connectivityManager
                .getNetworkCapabilities(network);

        return capabilities != null
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }



}
