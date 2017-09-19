package com.example.nicrodriguez.seniordesign;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AllTimeStatsFrag extends Fragment{
    MapView mapViewStat;
    MapboxMap mapStat;
    TextView allAvg, allMax, allDist, allTime, allTripNum, allListTitle;
    ListView allTripListView;
    Marker marker, marker2;
    Polyline line;
    TripDatabaseHelper db;
    View lastSelected;
    ArrayAdapter<String> listAdapter;

    List<String> idOfTrip = new ArrayList<>();




    /* declaring variables used to store Data Base information*/
    List<String> tripDBList = new ArrayList<>();
    List<String> tripTime = new ArrayList<>();
    List<String> pointListLat = new ArrayList<>();
    List<String> pointListLon = new ArrayList<>();
    List<String> averageSpeeds = new ArrayList<>();
    List<String> maxSpeeds = new ArrayList<>();
    List<String> speedPoints = new ArrayList<>();
    List<String> travelDistances = new ArrayList<>();
    List<String> timeMeasurements = new ArrayList<>();
    List<String> date = new ArrayList<>();
    List<LatLng> tripLatLon = new ArrayList<>();

    Double[] latVal,lonVal;
//    static List<String> tripList = new ArrayList<>();

    public AllTimeStatsFrag() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_time_stats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allAvg = view.findViewById(R.id.allAverageSpeed);
        allMax = view.findViewById(R.id.allMaxSpeed);
        allDist = view.findViewById(R.id.allDistanceTraveledText);
        allTime = view.findViewById(R.id.allTimeOnRoad);
        allTripNum = view.findViewById(R.id.allNumberOfTrips);
        allListTitle = view.findViewById(R.id.allTripListTitle);
        allTripListView = view.findViewById(R.id.allTripListView);



        mapViewStat = view.findViewById(R.id.mapViewAllTime);
        mapViewStat.setStyleUrl(Style.MAPBOX_STREETS);
        mapViewStat.onCreate(savedInstanceState);

        mapViewStat.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapStat = mapboxMap;

                mapboxMap.setMyLocationEnabled(true);
                
                db = new TripDatabaseHelper(getContext());
                Cursor res = db.getAllData();


                if(res.getCount()>0) {






            /* Obtaining the Data Base values */
                    while (res.moveToNext()) {
                        idOfTrip.add(res.getString(0));
                        tripDBList.add(res.getString(1));
                        tripTime.add(res.getString(2));
                        pointListLat.add(res.getString(3));
                        pointListLon.add(res.getString(4));
                        averageSpeeds.add(res.getString(5));
                        maxSpeeds.add(res.getString(6));
                        speedPoints.add(res.getString(7));
                        travelDistances.add(res.getString(8));
                        timeMeasurements.add(res.getString(9));
                        date.add(res.getString(10));

                    }





                    int counter = 0;
                    List<String> averages = new ArrayList<>();
                    List<String> maxSpeed = new ArrayList<>();
                    List<String> traveledDistances = new ArrayList<>();
                    List<String> timeOnRoad = new ArrayList<>();
                    String[] latString = new String[pointListLat.size()], lonString = new String[pointListLat.size()];

                    if (StatsByDayFrag.tripList.size() > 0) {
                        StatsByDayFrag.tripList.clear();
                        averages.clear();
                        maxSpeed.clear();
                        timeOnRoad.clear();
                    }
                    for (int j = 0; j < date.size(); j++) {
                        counter++;
                        StatsByDayFrag.tripList.add(tripDBList.get(j));
                        averages.add(averageSpeeds.get(j));
                        maxSpeed.add(maxSpeeds.get(j));
                        timeOnRoad.add(tripTime.get(j));
                        traveledDistances.add(travelDistances.get(j));


                        latString[j] = pointListLat.get(j);
                        lonString[j] = pointListLon.get(j);




                    }

                     mapStat.removeAnnotations();
                    for(int j = 0; j<latString.length; j++) {
                        tripLatLon.clear();
                        String delims = "[|]+";
                        String[] parsedLatArray = latString[j].split(delims);
                        String[] parsedLonArray = lonString[j].split(delims);
                        latVal = new Double[parsedLatArray.length];
                        lonVal = new Double[parsedLatArray.length];

                        for (int i = 0; i < parsedLatArray.length; i++) {
                            latVal[i] = Double.parseDouble(parsedLatArray[i]);
                            lonVal[i] = Double.parseDouble(parsedLonArray[i]);
                            tripLatLon.add(new LatLng(latVal[i], lonVal[i]));

                        }

                                            /*Determing max and min LatLon values for camera bounds */
                        double minLat = latVal[0];
                        double maxLat = latVal[0];
                        double minLon = lonVal[0];
                        double maxLon = lonVal[0];

                        double Lat;
                        double Lon;

                        for (int i = 0; i < latVal.length; i++) {
                            Lat = latVal[i];
                            Lon = lonVal[i];
                            if (Lat <= minLat)
                                minLat = Lat;
                            else if (Lat >= maxLat)
                                maxLat = Lat;

                            if (Lon <= minLon)
                                minLon = Lon;
                            else if (Lon >= maxLon)
                                maxLon = Lon;
                        }
                        // removes any previous annotations that might be on map from prior trips

                            /*Creating camera bounds */
                        List<LatLng> latLonBounds = new ArrayList<>();
                        latLonBounds.add(new LatLng(maxLat, minLon));
                        latLonBounds.add(new LatLng(minLat, maxLon));
                        mapStat.easeCamera(CameraUpdateFactory.newLatLng(Map.latlng));
                        CameraPosition camPosition = new CameraPosition.Builder().target(Map.latlng).zoom(11) // Sets the zoom
                                .bearing(0) // Rotate the camera
                                .tilt(30) // Set the camera tilt
                                .build(); // Creates a CameraPosition from the builder

                        mapStat.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition), 7000);


                        IconFactory iconFactory = IconFactory.getInstance(getContext());
                        Icon icon = iconFactory.fromResource(R.drawable.mapbox_mylocation_icon_default);

//                        MarkerOptions options = new MarkerOptions().position(new LatLng(tripLatLon.get(0))).title("Start of Trip").setIcon(icon);
//
//                        MarkerOptions options2 = new MarkerOptions().position(new LatLng(tripLatLon.get(tripLatLon.size() - 1))).title("Ending of Trip").icon(icon);
//                        mapStat.addMarker(options);
//                        mapStat.addMarker(options2);
                        mapStat.addPolyline(new PolylineOptions().addAll(tripLatLon).color(Color.rgb(60,97,169)).width(9));



                    }

                    if (counter == 0) {
                        StatsByDayFrag.tripList.clear();
                        StatsByDayFrag.tripList.add(getString(R.string.no_trips));
                        listAdapter = new ArrayAdapter<>(getActivity(), R.layout.simplerow, StatsByDayFrag.tripList);
                        allTripListView.setAdapter(listAdapter);

                        String averageSpeedText;
                        String maxSpeedText;
                        String distanceTraveledText;

                        if(!UnitsSettingsFragment.isKPH) {
                            averageSpeedText = "Average Speed: -- mph";
                            maxSpeedText = "Max Speed: -- mph";
                            distanceTraveledText = "Distance Traveled: -- mi";
                        }else{
                            averageSpeedText = "Average Speed: -- kph";
                            maxSpeedText = "Max Speed: -- kph";
                            distanceTraveledText = "Distance Traveled: -- ki";

                        }
                        String numTripText = "Number of Trips: 0";
                        String timeText = "Time On Road: -- ";
                        String tripListTitle = "No Trips Recorded";
                        allAvg.setText(averageSpeedText);
                        allMax.setText(maxSpeedText);
                        allDist.setText(distanceTraveledText);
                        allTripNum.setText(numTripText);
                        allTime.setText(timeText);
                        allListTitle.setText(tripListTitle);
                        Toast.makeText(getContext(), "You Did Not Record A Trip On This Date", Toast.LENGTH_LONG).show();

                    } else {
                        int maxS = 0;
                        int S;
                        double Avg = 0;
                        double totalDistance = 0;
                        int totalHourPassed = 0, totalMinPassed = 0, totalSecPassed = 0;
                        String[] startTime = new String[counter];
                        String[] endTime = new String[counter];
                        String delims = "[|]+";

                        Integer[] startHour = new Integer[counter], endHour = new Integer[counter];
                        Integer[] startMin = new Integer[counter], endMin = new Integer[counter];
                        Integer[] startSec = new Integer[counter], endSec = new Integer[counter];
                        Integer[] hoursPassed = new Integer[counter];
                        Integer[] minPassed = new Integer[counter];
                        Integer[] secPassed = new Integer[counter];
                        String[] startComponents;
                        String[] endComponents;


                        for (int j = 0; j < counter; j++) {
                            String[] totalTime = timeOnRoad.get(j).split(delims);
                            startTime[j] = totalTime[0];
                            endTime[j] = totalTime[1];
                            String startString = startTime[j].substring(0, startTime[j].indexOf("_"));
                            String endString = endTime[j].substring(0, endTime[j].indexOf("_"));

                            startComponents = startString.split("[:]+");
                            endComponents = endString.split("[:]+");

                            startHour[j] = Integer.valueOf(startComponents[0]);
                            startMin[j] = Integer.valueOf(startComponents[1]);
                            startSec[j] = Integer.valueOf(startComponents[2]);
                            endHour[j] = Integer.valueOf(endComponents[0]);
                            endMin[j] = Integer.valueOf(endComponents[1]);
                            endSec[j] = Integer.valueOf(endComponents[2]);

                            if (startHour[j] <= endHour[j])
                                hoursPassed[j] = endHour[j] - startHour[j];
                            else {
                                hoursPassed[j] = endHour[j] + 12 - startHour[j];
                            }

                            if (startMin[j] <= endMin[j])
                                minPassed[j] = endMin[j] - startMin[j];
                            else if(startMin[j] >= endMin[j] && endHour[j] > startHour[j]) {

                                hoursPassed[j] -= 1;
                                minPassed[j] = endMin[j] + 60 - startMin[j];

                            }else{
                                 minPassed[j] = endMin[j] + 60 - startMin[j];
                                }


                            if (startSec[j] <= endSec[j]) {
                                secPassed[j] = endSec[j] - startSec[j];

                            }else if(startSec[j] >= endSec[j] && endMin[j] > startMin[j]) {

                                minPassed[j] -= 1;
                                secPassed[j] = endSec[j] + 60 - startSec[j];

                            } else {
                                secPassed[j] = endSec[j] + 60 - startSec[j];

                            }

                            S = Integer.parseInt(maxSpeed.get(j));
                            if (S > maxS) {
                                maxS = S;
                            }

                            Avg += Double.parseDouble(averages.get(j));
                            totalDistance += Double.parseDouble(traveledDistances.get(j));

                            totalHourPassed += hoursPassed[j];
                            totalMinPassed += minPassed[j];

                            if (totalMinPassed > 60) {
                                totalHourPassed++;
                                totalMinPassed -= 60;
                            }
                            totalSecPassed += secPassed[j];

                            if (totalSecPassed > 60) {
                                totalMinPassed++;
                                totalSecPassed -= 60;
                            }
                        }

                        DecimalFormat df = new DecimalFormat("#.##");
                        Avg /= counter;
                        String averageSpeedText;
                        String maxSpeedText;
                        String distanceTraveledText;

                        if(!UnitsSettingsFragment.isKPH){
                            Avg = Double.valueOf(df.format(Avg));
                            totalDistance = Double.valueOf(df.format(totalDistance));
                            averageSpeedText = "Average Speed: "+ Avg +" mph";
                            maxSpeedText = "Max Speed: "+maxS + " mph";
                            distanceTraveledText = "Distance Traveled: "+totalDistance+" mi";
                        }else{
                            Avg = Double.valueOf(df.format(Avg*1.6094));
                            totalDistance = Double.valueOf(df.format(totalDistance*1.6094));
                            averageSpeedText = "Average Speed: "+ Avg +" kph";
                            DecimalFormat Intform = new DecimalFormat("#");
                            maxSpeedText = "Max Speed: "+Integer.valueOf(Intform.format(maxS*1.6094)) + " kph";
                            distanceTraveledText = "Distance Traveled: "+totalDistance+" km";
                        }
                        String numTripText = "Number of Trips: " + counter;
                        String tripListTitle = "Trips Recorded";
                        String timePassedText = "Time On Road:  " + totalHourPassed + "h:" + totalMinPassed + "m:" + totalSecPassed + "s";

                        Float[] AverageSpeeds = new Float[averages.size()];
                        Float[] maxSpeedsStat = new Float[averages.size()];
                        List<BarEntry> tripStatAvg = new ArrayList<>();
                        List<BarEntry> tripStatMax = new ArrayList<>();

                        for (int j = 0; j < averages.size(); j++) {
                            AverageSpeeds[j] = Float.parseFloat(averages.get(j));
                            maxSpeedsStat[j] = Float.parseFloat(maxSpeed.get(j));

                            if(!UnitsSettingsFragment.isKPH){
                                tripStatAvg.add(new BarEntry(j, AverageSpeeds[j]));
                                tripStatMax.add(new BarEntry(j, maxSpeedsStat[j]));
                            }else{
                                AverageSpeeds[j] = Float.valueOf(df.format(AverageSpeeds[j]*1.6094));
                                maxSpeedsStat[j] = Float.valueOf(df.format(maxSpeedsStat[j]*1.6094));
                                tripStatAvg.add(new BarEntry(j, AverageSpeeds[j]));
                                tripStatMax.add(new BarEntry(j, maxSpeedsStat[j]));
                            }

                        }



                        TripHistoryGraph.createBarGraph(tripStatAvg,tripStatMax);
                        Description desc = new Description();
                        desc.setText("Trip Data");
                        TripHistoryGraph.barChart.setDescription(desc);

                        allAvg.setText(averageSpeedText);
                        allMax.setText(maxSpeedText);
                        allDist.setText(distanceTraveledText);
                        allTripNum.setText(numTripText);
                        allTime.setText(timePassedText);
                        allListTitle.setText(tripListTitle);
                        listAdapter = new ArrayAdapter<>(getActivity(), R.layout.simplerow, StatsByDayFrag.tripList);
                        allTripListView.setAdapter(listAdapter);

                    }
                }


                allTripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(lastSelected != view && lastSelected != null){
                            lastSelected.setBackgroundResource(R.drawable.my_border);
                        }

                        if(marker != null ){
                            marker.remove();
                        }
                        if(marker2 != null){
                            marker2.remove();
                        }
                        if(line != null){
                            line.remove();
                        }
//                        Highlight[] val = new Highlight[2];
//                        val[0] = new Highlight(i,0,0);
//                        val[1] = new Highlight(i,1,1);
//                        TripHistoryGraph.barChart.highlightValues(val);

                        String latString = pointListLat.get(i);
                        String lonString = pointListLon.get(i);

                        String[] parsedLat = latString.split("[|]+");
                        String[] parsedLon = lonString.split("[|]+");
                        Double[] latVal = new Double[parsedLat.length];
                        Double[] lonVal = new Double[parsedLat.length];
                        List<LatLng> MarkerPoints = new ArrayList<>();
                        for(int j = 0; j<parsedLat.length;j++){
                            latVal[j] = Double.valueOf(parsedLat[j]);
                            lonVal[j] = Double.valueOf(parsedLon[j]);
                            MarkerPoints.add(new LatLng(latVal[j],lonVal[j]));
                        }

                                                    /*Determing max and min LatLon values for camera bounds */
                        double minLat = latVal[0];
                        double maxLat = latVal[0];
                        double minLon = lonVal[0];
                        double maxLon = lonVal[0];

                        double Lat;
                        double Lon;

                        for (int k = 0; k < latVal.length; k++) {
                            Lat = latVal[k];
                            Lon = lonVal[k];
                            if (Lat <= minLat)
                                minLat = Lat;
                            else if (Lat >= maxLat)
                                maxLat = Lat;

                            if (Lon <= minLon)
                                minLon = Lon;
                            else if (Lon >= maxLon)
                                maxLon = Lon;
                        }

                        List<LatLng> latLonBounds = new ArrayList<>();
                        latLonBounds.add(new LatLng(maxLat, maxLon));
                        latLonBounds.add(new LatLng(minLat, minLon));
                        mapStat.easeCamera(CameraUpdateFactory.newLatLng(tripLatLon.get(0)));


                        LatLngBounds bounds = new LatLngBounds.Builder().includes(latLonBounds).build();
                        double possibleZoom = bounds.getLongitudeSpan();
                        double possibleZoom2 = bounds.getLatitudeSpan();
                        double zoom;
                        if (possibleZoom >= possibleZoom2) {
                            zoom = (0.8 / possibleZoom);
                        } else {
                            zoom = (0.8 / possibleZoom2);
                        }
                        if (zoom > 14) {
                            zoom = 14;
                        }

                            /*Creating Camera View and Trip PolyLine */
                        CameraPosition camPosition = new CameraPosition.Builder().target(bounds.getCenter()).zoom(zoom) // Sets the zoom
                                .bearing(0) // Rotate the camera
                                .tilt(30) // Set the camera tilt
                                .build();


                        marker = mapStat.addMarker(new MarkerOptions().position(MarkerPoints.get(0)).setTitle("Start of Trip"));
                        marker2 = mapStat.addMarker(new MarkerOptions().position(MarkerPoints.get(MarkerPoints.size()-1)).setTitle("End of Trip"));
                        line = mapStat.addPolyline(new PolylineOptions().addAll(MarkerPoints).color(Color.rgb(18,143,226)).width(5));
                        mapStat.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition), 5000);
                        lastSelected = view;
                        view.setBackgroundResource(R.drawable.on_button_pressed);
                    }
                });




            }
        });




    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
