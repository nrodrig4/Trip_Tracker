package com.example.nicrodriguez.seniordesign;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;
import com.mapbox.mapboxsdk.Mapbox;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.widget.Toast.LENGTH_SHORT;
import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class MainActivity extends AppCompatActivity{

    WifiManager wifi;
    ConnectivityManager connectivityManager;
    public static boolean tripStart = false;
    public static AlertDialog.Builder tripDialog;





    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, "pk.eyJ1IjoibnJvZHJpZzQiLCJhIjoiY2o0cTB2MDNyMGI3ZDMzcDUyNGQ3ZzVsNyJ9.ibmFyPh4stkiMs3nDjxluw");
        setContentView(R.layout.activity_main);


        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);


        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        tripDialog = new AlertDialog.Builder(this);
        tripDialog.setCancelable(false);


    }


//    public  void detectTripStart(int Rotation){
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//
//
//    }
//
//    @Override
//    public void onBackPressed() {
//            super.onBackPressed();
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.map_download:
//                    if(!hasInternetConnection(this)){
//                        Toast.makeText(this, "NOT CONNECTED TO THE INTERNET", LENGTH_SHORT).show();
//                    }else if(tripStart){
//                        Toast.makeText(this,"END TRIP TO DOWNLOAD NEW MAP", LENGTH_SHORT).show();
//                    }else
//                        startActivity(new Intent(this, DownloadOfflineMapActivity.class));
//                break;
//            case R.id.travel_history:
//                    if(tripStart){
//                        Toast.makeText(this,"END TRIP TO DOWNLOAD NEW MAP", LENGTH_SHORT).show();
//                    }else
//                        startActivity(new Intent(this,TravelHistoryPopup.class));
//
//
//                break;
//
//
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onPause() {
        super.onPause();

    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }







//
//    public static void verifyStoragePermissions(Activity activity) {
//        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
//
//
//    }

//    public static class MyPagerAdapter extends FragmentPagerAdapter {
//        private static int NUM_ITEMS = 2;
//
//        public MyPagerAdapter(FragmentManager fragmentManager) {
//            super(fragmentManager);
//        }
//
//        // Returns total number of pages.
//        @Override
//        public int getCount() {
//            return NUM_ITEMS;
//        }
//
//        // Returns the fragment to display for a particular page.
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 0:
//                    return new Map();
//                case 1:
//                    return new fragment4;
//                 default:
//                    return null;
//            }
//        }
//
//        // Returns the page title for the top indicator
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "Tab " + position;
//        }
//
//    }

//    public static boolean hasInternetConnection(final Context context) {
//        final ConnectivityManager connectivityManager = (ConnectivityManager)context.
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        final Network network = connectivityManager.getActiveNetwork();
//        final NetworkCapabilities capabilities = connectivityManager
//                .getNetworkCapabilities(network);
//
//        return capabilities != null
//                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
//    }


}