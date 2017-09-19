package com.example.nicrodriguez.seniordesign;

import android.support.annotation.Nullable;
import android.annotation.SuppressLint;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;


import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;




public class Map extends Fragment implements MapboxMap.OnMyLocationChangeListener {

    /*Views*/
    MapView mapView;
    FloatingActionButton refresh;
    ToggleButton Follow;
    ToggleButton Record;

    /*Literals*/
    String TripStartTime;

    int hour,maxSpeed = 0,frame = 0;
    double lat,lon,elevation,previousLat, previousLon, previousElevation,travelDistance = 0;
    static int startHour, counter = 0;
    public static String date, time, dateAndTime, TripTitle,EndOfTrip;
    public static List<String> TripList = new ArrayList<>();
//    public static List<String> timeOfMeasurements = new ArrayList<>();
    public static LatLng latlng;
    public static Boolean RecordCheck;
    public static MapboxMap map;
    public static float averageSpeed, totalSpeed = 0;
    private LatLng startLatLng,latLng = new LatLng();

    /*Class instances */
    TripControl tripControl;
    TravelHistory th;
    TripDatabaseHelper tripDB;

    public Map() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            tripControl = new TripControl();
            th = new TravelHistory();
            tripDB = new TripDatabaseHelper(getContext());
            Follow = view.findViewById(R.id.HistoryToggle);
            Record = view.findViewById(R.id.recordButton);
            Record.setText(R.string.recordText0);
            refresh = view.findViewById(R.id.RefreshButton);
            mapView = view.findViewById(R.id.mapView);
            mapView.setStyleUrl(Style.MAPBOX_STREETS);
            mapView.onCreate(savedInstanceState);

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap mapboxMap) {
                    map = mapboxMap;

                    mapboxMap.setOnMyLocationChangeListener(Map.this);
                    mapboxMap.setMyLocationEnabled(true);
                }
            });
    }



    public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
        latLng.setLatitude(startValue.getLatitude() +
                ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
        latLng.setLongitude(startValue.getLongitude() +
                ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
        return latLng;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onMyLocationChange(@Nullable Location location) {

        if (location != null) {
            if(frame == 8){
                frame = 0;
            }
            frame++;
            ObtainNavValues(location, frame);
        }

    }

//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    public void onStart(){
//        super.onStart();
//        mapView.onStart();
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        mapView.onStop();
//    }

    @SuppressLint("SetTextI18n")
    private void ObtainNavValues(Location location, int Frame) {
    /* Declaring Variables Used Map Actions */
        DecimalFormat df = new DecimalFormat("#.##");
        Calendar c = Calendar.getInstance();
        String amPM;
        int SpeedMph;
        double Speed, bearing, accuracy, accuracy_ft;
        float GraphSpeed;

        Speed = location.getSpeed();
        bearing = location.getBearing();
        accuracy = location.getAccuracy();
        accuracy_ft = Double.valueOf(df.format(accuracy*3.28084));

    /* Determing previous values for distance calculations) */
        if(counter>0) {
            startLatLng = new LatLng(lat, lon);
            if(previousLat != lat)
                previousLat = lat;
            if(previousLon!=lon)
                previousLon = lon;
            if(previousElevation != elevation)
                previousElevation = elevation;
        }
        elevation = location.getAltitude();
        lat = location.getLatitude();
        lon = location.getLongitude();

    /* Starting Calculations after first values are measured */
        if(counter>0) {
            float fraction = 9 / 10;
            latlng = evaluate(fraction, startLatLng, new LatLng(lat, lon));
            if(Speed>0) {

                travelDistance += tripDistance(previousLat, lat, previousLon, lon, previousElevation, elevation)/1000/1.60943;
//                travelDistance = travelDistance/* m(1km/1000m)(1mi/1.60934km) = miles */
                travelDistance = Double.valueOf(df.format(travelDistance));
            }


        }
        else {
            latlng = new LatLng(lat, lon);
        }

    /* Converting Speed measurement (kmh) to (mph) and adding to list*/
        if(Speed> 0){
            SpeedMph = (int) (Math.round(Speed * 2.24));
        }else{
            SpeedMph = 0;
        }
        th.speedHistory.add(SpeedMph);

    /* Obtaing the car's maximum speed */
        if(SpeedMph >= maxSpeed)
            maxSpeed = SpeedMph;


    /*Obtains the current date and time and formats it*/
        hour = c.get(Calendar.HOUR);
        if(hour == 0){
            hour = 12;
        }
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        int month = c.get(Calendar.MONTH);
        month +=1;
        if(month > 12){
            month = 1;
        }
        final int AmPM = c.get(Calendar.AM_PM);
            if(AmPM == 0){
                amPM = "AM";
            }else {

                amPM = "PM";
            }
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int year = c.get(Calendar.YEAR);


        date = month+"/"+day+"/"+year;
        time = hour + ":"+minute+":"+second+"_"+amPM;
        dateAndTime = date+"_"+time;

    /* Determining if the user wants to record trip */
        Record.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(!Main2Activity.tripStart){
                    Record.setChecked(true);
                    tripControl.startNewTrip(Follow, Record);
                    TripTitle = dateAndTime;
                    TripStartTime = time;
                    startHour = hour;
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                }else{

                    String TripEndTime = time;
                    String DBTime = TripStartTime+"|"+TripEndTime;
                    tripControl.endTrip(getContext(),Follow, Record, DBTime,tripDB,th,averageSpeed,maxSpeed, travelDistance, date);
                    getActivity().getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                   // getActivity().getWindow().addFlags(WindowManager.LayoutParams.SCREE);
                }
            }
        });


    /*Starts recording data after user starts a new trip and storing information for database*/
        if(Main2Activity.tripStart){
            switch(Frame) {
                case 1:case 2:
                    recordingAnimation1();
                    break;
                case 3:case 4:
                    recordingAnimation2();
                    break;
                case 5:case 6:
                    recordingAnimation3();
                    break;
                case 7:case 8:
                    recordingAnimation4();
                    break;
                default:
                    recordingAnimation1();
                    break;
            }
            /*adding time data to variable list */
            th.Time.add(hour + ":"+minute+":"+second);
            counter++;

            //if(Speed > 0) {
                th.points.add(new LatLng(latlng));
                th.tripHistory.add(lat+","+lon);
                th.lat.add(lat);
                th.lon.add(lon);

            GraphSpeed = Float.valueOf(df.format(Speed * 2.24));
            totalSpeed = totalSpeed+GraphSpeed;
            averageSpeed = totalSpeed/counter;
            averageSpeed = Float.valueOf(df.format(averageSpeed));
            //timeArray = Time.toArray(new String[0]);
            SpeedGraph.addEntry(GraphSpeed,(int)averageSpeed);
        }


    /*adds first ten points of trip to a list so markers can be placed at appropriate locations*/
        if(th.pointsHistory.size()<10){
            th.pointsHistory.add(new LatLng(latlng));

        }
    /*Settings text of Speedometer Class*/
        String speedLabel;
        String latLabel;
        String lonLabel;
        String distanceLabel;
        String AccuracyLabel;
        String averageSpeedLabel;
        String speedLimitLabel;

        int SpeedAvg = Math.round(averageSpeed);
        latLabel = "Latitude: " + lat;
        lonLabel = "Longitude: " + lon;
    if(!UnitsSettingsFragment.isKPH) {
        speedLimitLabel = "Speed Limit: -- mph";
        speedLabel = SpeedMph + " mph";
        distanceLabel = "Distance Traveled: " + travelDistance + " mi";
        AccuracyLabel = "Accuracy: " + accuracy_ft + " ft";
        averageSpeedLabel = "Average Speed: " + SpeedAvg + " mph";
    }else {


        double distanceTraveled = travelDistance*1.609;
        distanceTraveled = Double.valueOf(df.format(distanceTraveled));

        double accuracy_m = accuracy_ft*0.305;
        accuracy_m = Double.valueOf(df.format(accuracy_m));

        int SpeedKph = (int)Math.round(SpeedMph*1.609);
        int SpeedAvgK =(int)Math.round(SpeedAvg*1.609);
        speedLimitLabel = "Speed Limit: -- kph";
        speedLabel = SpeedKph + " kph";
        distanceLabel = "Distance Traveled: " + distanceTraveled + " km";
        AccuracyLabel = "Accuracy: " + accuracy_m + " m";
        averageSpeedLabel = "Average Speed: " + SpeedAvgK + " kph";
    }

        Speedometer.DistanceTraveled.setText(distanceLabel);
        Speedometer.SpeedView.setText(speedLabel);
//        Speedometer.SpeedLimitView.setText(speedLimitLabel);
        Speedometer.LatView.setText(latLabel);
        Speedometer.LonView.setText(lonLabel);
        Speedometer.AccuracyView.setText(AccuracyLabel);
        Speedometer.SpeedAverageView.setText(averageSpeedLabel);

    /*Setting Zoom of camera*/
        int zoom;
        switch (SpeedMph / 10) {
            case 0:case 1:
                zoom = 17;
                break;
            case 2:case 3:case 4:
                zoom = 16;
                break;
            default:
                zoom = 15;
                break;
        }

    /*Creating Polyline based on recorded points*/
        PolyLineCreator(th.points,th.Line, th.pointsHistory);

        if(Follow.isChecked()){
            map.easeCamera(CameraUpdateFactory.newLatLng(latlng));
            CameraPosition position = new CameraPosition.Builder().target(latlng).zoom(zoom) // Sets the zoom
                    .bearing(bearing) // Rotate the camera
                    .tilt(30) // Set the camera tilt
                    .build(); // Creates a CameraPosition from the builder

            map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000);
        }
    }




    public void PolyLineCreator( final List<LatLng> points, final List<PolylineOptions> Lines, final List<LatLng> pointsHistory) {


        Follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //StoredLines.add(map.getPolylines());
                    map.removeAnnotations();
                    refresh.setVisibility(View.INVISIBLE);
                } else {
                    refresh.setVisibility(View.VISIBLE);
                    map.easeCamera(CameraUpdateFactory.newLatLng(latlng));
                    CameraPosition position = new CameraPosition.Builder().target(latlng).zoom(14).tilt(0).bearing(0)
                            .build(); // Creates a CameraPosition from the builder
                    MarkerOptions options = new MarkerOptions().position(new LatLng(pointsHistory.get(2))).title("Starting Location");
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000);
                    map.addMarker(options);
                    Lines.add(new PolylineOptions().addAll(points).color(Color.BLUE).width(6));
                    map.addPolylines(Lines);


                }

            }
        });

        if (!Follow.isShown()){
            refresh.setVisibility(View.INVISIBLE);
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lines.add(new PolylineOptions().addAll(points).color(R.color.mapbox_blue).width(6));
                //map.addPolyline(Lines.get(0));
                map.addPolylines(Lines);
            }
        });
    }

    /* Calculates the distance between lat, lon, and elevation points */
    public static double tripDistance(double lat1, double lat2, double lon1,
                                      double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c * 1000; // convert to meters
        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }



    private void recordingAnimation1(){
        Record.setText(R.string.recordingText1);
    }
    private void recordingAnimation2(){
        Record.setText(R.string.recordingText2);
    }
    private void recordingAnimation3(){
        Record.setText(R.string.recordingText3);
    }
    private void recordingAnimation4(){
        Record.setText(R.string.recordingText4);
    }


}
