package com.example.nicrodriguez.seniordesign;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;

import static android.widget.Toast.LENGTH_SHORT;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    WifiManager wifi;
    ConnectivityManager connectivityManager;
    public static boolean tripStart = false;
    public static AlertDialog.Builder tripDialog;
    DrawerLayout drawer;
    public static String title;
    public String tableName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Mapbox.getInstance(this, "pk.eyJ1IjoibnJvZHJpZzQiLCJhIjoiY2o0cTB2MDNyMGI3ZDMzcDUyNGQ3ZzVsNyJ9.ibmFyPh4stkiMs3nDjxluw");
        tableName = UserLogin.tableName;
        if(UserLogin.tableName == null){
            startActivity(new Intent(Main2Activity.this,SplashActivity.class));
        }
        TripDatabaseHelper db = new TripDatabaseHelper(Main2Activity.this);
        db.TABLE_NAME = tableName;
        setContentView(R.layout.activity_main2);
        setTitle(title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.userName);
        nav_user.setText(UserLogin.selectedUser);
        navigationView.setNavigationItemSelectedListener(this);


        ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);


        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        tripDialog = new AlertDialog.Builder(this);
        tripDialog.setCancelable(false);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }









//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.map_download:
//                if(!hasInternetConnection(this)){
//                    Toast.makeText(this, "NOT CONNECTED TO THE INTERNET", LENGTH_SHORT).show();
//                }else if(tripStart){
//                    Toast.makeText(this,"END TRIP TO DOWNLOAD NEW MAP", LENGTH_SHORT).show();
//                }else
//                    startActivity(new Intent(this, DownloadOfflineMapActivity.class));
//                break;
//            case R.id.travel_history:
//                if(tripStart){
//                    Toast.makeText(this,"END TRIP TO DOWNLOAD NEW MAP", LENGTH_SHORT).show();
//                }else
//                    startActivity(new Intent(this,TravelHistoryPopup.class));
//
//
//                break;
//
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.download_offline:
                if(!hasInternetConnection(this)){
                    Toast.makeText(this, "NOT CONNECTED TO THE INTERNET", LENGTH_SHORT).show();
                    break;
                }else if(tripStart){
                    Toast.makeText(this,"END TRIP TO DOWNLOAD NEW MAP", LENGTH_SHORT).show();
                    break;
                }else
                    drawer.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(this, DownloadOfflineMapActivity.class));
                break;

            case R.id.trip_history_tab:
                if(tripStart){
                    Toast.makeText(this,"END TRIP TO VIEW PAST HISTORY", LENGTH_SHORT).show();
                    break;
                }else
                    drawer.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(this,TravelHistoryPopup.class));
                break;

            case R.id.user_stats_tab:
                if(tripStart){
                    Toast.makeText(this,"END TRIP TO VIEW PAST HISTORY", LENGTH_SHORT).show();
                    break;
                }else
                    drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this,UserStats.class));
                break;

            case R.id.settings_tab:
                if(tripStart){
                    Toast.makeText(this,"END TRIP TO VIEW PAST HISTORY", LENGTH_SHORT).show();
                    break;
                }else
                    drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this,Settings.class));
                break;

            case R.id.logout:
                if(tripStart){
                    Toast.makeText(this,"END TRIP TO LOGOUT", LENGTH_SHORT).show();
                    break;
                }else
                    drawer.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(this,UserLogin.class));
                    break;


        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
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
