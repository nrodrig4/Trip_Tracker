package com.example.nicrodriguez.seniordesign;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;

import android.util.SparseBooleanArray;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;


public class TripHistoryList extends Fragment {

    /*Array Adaptor used for ListView*/
    ArrayAdapter<String> listAdapter;


    /*Lists*/
    List<View> deletedTrips = new ArrayList<>();
    List<LatLng> tripLatLon = new ArrayList<>();
    List<PolylineOptions>  coloredPoly = new ArrayList<>();
    List<Integer> tripSpeedPoints = new ArrayList<>();
    List<Integer> deletedPositions = new ArrayList<>();
    List<String> idOfTrip = new ArrayList<>();
    List<String> tripList = new ArrayList<>();
    List<String> timeOfPoints = new ArrayList<>();



    /*Literals and Variables*/
    boolean update = false;
    String ID,tripName,trip;
    String[] timeVal;
    Double[] latVal,lonVal;
    Integer[] speedVal;

    /*Views*/
    ListView tripListView;
    View lastSelected;
    Marker marker;
    Polyline tripPoly;
   // PolylineOptions optionColor1 = new PolylineOptions(),optionColor2 = new PolylineOptions(),optionColor3 = new PolylineOptions();

    /*Class instances*/
    TripDatabaseHelper tripDB;
    TravelHistory th;







    public TripHistoryList() {
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

        tripListView =  view.findViewById( R.id.tripListView );

        tripDB = new TripDatabaseHelper(getContext());
        th = new TravelHistory();




        Cursor res = tripDB.getAllData();
        if(res.getCount()>0) {


            /* declaring variables used to store Data Base information*/
            final List<String> tripDBList = new ArrayList<>();
            final List<String> tripTime = new ArrayList<>();
            final List<String> pointListLat = new ArrayList<>();
            final List<String> pointListLon = new ArrayList<>();
            final List<String> averageSpeeds = new ArrayList<>();
            final List<String> maxSpeeds = new ArrayList<>();
            final List<String> speedPoints = new ArrayList<>();
            final List<String> travelDistances = new ArrayList<>();
            final List<String> timeMeasurements = new ArrayList<>();



            /* Obtaining the Data Base values */
            while (res.moveToNext()){
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
            }


                tripList.addAll(tripDBList);

                // Create ArrayAdapter using the trip list.
                listAdapter = new ArrayAdapter<>(getActivity(), R.layout.simplerow, tripList);
                tripListView.setAdapter(listAdapter);


            /* Edit Name Toggle Button Effects */

            TripHistoryMap.updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TripHistoryMap.updateButton.isChecked()) {
                        update = true;
                        TripHistoryMap.deleteButton.setBackgroundResource(R.drawable.large_edit_button);
                        TripHistoryMap.deleteButton.setVisibility(View.VISIBLE);
                        TripHistoryMap.deleteButton.setEnabled(false);
//                        TripHistoryMap.deleteTrip.setEnabled(false);

                    }else{
                        update = false;
                        TripHistoryMap.deleteButton.setBackgroundResource(R.drawable.large_trash_button);
                        TripHistoryMap.deleteButton.setVisibility(View.INVISIBLE);
                        TripHistoryMap.deleteButton.setEnabled(true);
//                        TripHistoryMap.deleteTrip.setEnabled(true);

                    }

                }
            });

//            TripHistoryMap.deleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(deletedTrips.size()>0) {
//                        deleteTripCheck(idOfTrip, deletedPositions, deletedTrips);
//
//
//                    }else{
//                        Toast.makeText(getContext(),"Please Select A Trip To Delete",Toast.LENGTH_LONG).show();
//                    }
//                }
//
//            });






            /*OnClickListener that gathers information based on what item is pressed in the ListView*/
            tripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                public void onItemClick(AdapterView<?> adapter, View v, final int position, long id) {







                    //final int Pos = position;

                    /*Operation executes if neither the delete or update buttons are pressed*/
                    if(!update) {
                        if (lastSelected != null && lastSelected != v){
                            lastSelected.setBackgroundResource(R.drawable.my_border);
                        }


                        /*Aquiring database values based on which item is pressed in the ListView*/

                        v.setBackgroundResource(R.drawable.on_button_pressed);

//                        for (int i = 0; i < tripListView.getChildCount(); i++) {
//                            if(position == i ){
//                                tripListView.getChildAt(i).setBackgroundResource(R.drawable.on_button_pressed);
//                            }else{
//                                tripListView.getChildAt(i).setBackgroundResource(R.drawable.my_border);
//                            }
//                        }
                        tripName = tripList.get(position);
                        String tripTimes = tripTime.get(position);
                        String latString = pointListLat.get(position);
                        String lonString = pointListLon.get(position);
                        String averageSpeed = averageSpeeds.get(position);
                        String maxSpeed = maxSpeeds.get(position);
                        String speedString = speedPoints.get(position);
                        String distance = travelDistances.get(position);
                        String timeOfPoint = timeMeasurements.get(position).replace("_"," ");
                        String delims = "[|]+";

                        /*Parsing info from "Lat|Lon", Speed, and "Start|End" strings */
                        if(latString.length()>0 || lonString.length()>0) {
                            String[] parsedTripTime = tripTimes.split(delims);
                            String[] parsedLatArray = latString.split(delims);
                            String[] parsedLonArray = lonString.split(delims);
                            String[] parsedSpeedArray = speedString.split(delims);
                            String[] parsedTimeArray = timeOfPoint.split(delims);

                            /*clearing stored latLonvalues after each run ensures that trips aren't displayed over each other*/
                            tripLatLon.clear();
                            tripSpeedPoints.clear();
                            timeOfPoints.clear();

                            /*Declaring arrays and preallocating memory*/
                            latVal = new Double[parsedLatArray.length];
                            lonVal = new Double[parsedLatArray.length];
                            speedVal = new Integer[parsedSpeedArray.length];
                            timeVal = new String[parsedTimeArray.length];
                            for (int i = 0; i < parsedLatArray.length; i++) {
                                latVal[i] = Double.parseDouble(parsedLatArray[i]);
                                lonVal[i] = Double.parseDouble(parsedLonArray[i]);
                                timeVal[i] = parsedTimeArray[i];
                                speedVal[i] = Integer.parseInt(parsedSpeedArray[i]);
                                tripLatLon.add(new LatLng(latVal[i], lonVal[i]));
                                tripSpeedPoints.add(speedVal[i]);
                                timeOfPoints.add(timeVal[i]);
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

                                                        /*Determining distance traveled*/
                            double travelDistance = 0;
                            DecimalFormat df = new DecimalFormat("#.##");
                            double sumDis = Double.parseDouble(distance);
                            if(sumDis < 1) {
                                for (int i = 0; i < latVal.length - 1; i++) {
                                    travelDistance += tripDistance(latVal[i], latVal[i + 1], lonVal[i], lonVal[i + 1], 0, 0) / 1000 / 1.60943;
                                    travelDistance = Double.valueOf(df.format(travelDistance));
                                }
                                if(!UnitsSettingsFragment.isKPH) {
                                    String distanceTraveled = "Trip Length: " + travelDistance + " mi ";
                                    TripHistoryMap.tripLengthLabel.setText(distanceTraveled);
                                }else{
                                    travelDistance = travelDistance*1.609;
                                    travelDistance = Double.valueOf(df.format(travelDistance));
                                    String distanceTraveled = "Trip Length: " + travelDistance + " km ";
                                    TripHistoryMap.tripLengthLabel.setText(distanceTraveled);
                                }
                            }else{
                                if(!UnitsSettingsFragment.isKPH) {
                                    String distanceTraveled = "Trip Length: " + distance + " mi ";
                                    TripHistoryMap.tripLengthLabel.setText(distanceTraveled);
                                }else{
                                    double Distance = Double.valueOf(distance)*1.609;
                                    Distance = Double.valueOf(df.format(Distance));
                                    String distanceTraveled = "Trip Length: " + Distance + " km ";
                                    TripHistoryMap.tripLengthLabel.setText(distanceTraveled);
                                }
                            }

                            /*Setting Labels on Map*/

                            String startTimeLabel = "Start Time: "+parsedTripTime[0];
                            String startTimeLabel2 = startTimeLabel.replace("_"," ");
                            String endTimeLabel = "End Time: "+parsedTripTime[1];
                            String endTimeLabel2 = endTimeLabel.replace("_"," ");
                            String averageSpeedLabel;
                            String maxSpeedLabel;

                            if(!UnitsSettingsFragment.isKPH) {
                                averageSpeedLabel = "Average Speed: " + averageSpeed + " mph";
                                maxSpeedLabel = "Max Speed: " + maxSpeed + " mph";
                            }else{

                                double AverageSpeed = Double.valueOf(averageSpeed)*1.609;
                                AverageSpeed = Double.valueOf(df.format(AverageSpeed));

                                double MaxSpeed = Double.valueOf(maxSpeed)*1.609;
                                MaxSpeed = Double.valueOf(df.format(MaxSpeed));
                                averageSpeedLabel = "Average Speed: " + AverageSpeed + " kph";
                                maxSpeedLabel = "Max Speed: " + MaxSpeed + " kph";
                            }

                            TripHistoryMap.startTime.setText(startTimeLabel2);
                            TripHistoryMap.endTime.setText(endTimeLabel2);
                            TripHistoryMap.tripLabel.setText(tripName);
                            TripHistoryMap.averageSpeedLabel.setText(averageSpeedLabel);
                            TripHistoryMap.maxSpeedLabel.setText(maxSpeedLabel);
                            TripHistoryMap.map.removeAnnotations(); // removes any previous annotations that might be on map from prior trips

                            /*Creating camera bounds */
                            List<LatLng> latLonBounds = new ArrayList<>();
                            latLonBounds.add(new LatLng(maxLat, maxLon));
                            latLonBounds.add(new LatLng(minLat, minLon));
                            TripHistoryMap.map.easeCamera(CameraUpdateFactory.newLatLng(tripLatLon.get(0)));
                            TripHistoryMap.map.setMinZoomPreference(11);
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
                                    .build(); // Creates a CameraPosition from the builder


                            MarkerOptions options = new MarkerOptions().position(new LatLng(tripLatLon.get(0))).title("Start of Trip");
                            IconFactory iconFactory = IconFactory.getInstance(getContext());
                            Icon icon = iconFactory.fromResource(R.drawable.mapbox_marker_icon_default);

                            MarkerOptions options2 = new MarkerOptions().position(new LatLng(tripLatLon.get(tripLatLon.size() - 1))).title("Ending of Trip").icon(icon);
                            TripHistoryMap.map.addMarker(options);
                            TripHistoryMap.map.addMarker(options2);
                            TripHistoryMap.map.addPolyline(new PolylineOptions().addAll(tripLatLon).color(Color.rgb(60,97,169)).width(9));
                            tripPoly = TripHistoryMap.map.addPolyline(new PolylineOptions().addAll(tripLatLon).color(Color.rgb(18,143,226)).width(5));

                            TripHistoryMap.map.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition), 7000);



                            TripHistoryMap.map.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(@NonNull LatLng point) {


//
                                    Double clickedLat = point.getLatitude();
                                    Double clickedLon = point.getLongitude();
//                                    MarkerOptions optionstest = new MarkerOptions().setPosition(point).snippet(""+point);
//                                    TripHistoryMap.map.addMarker(optionstest);
//                                    Toast.makeText(getContext(),"" + point,Toast.LENGTH_LONG).show();
                                    for(int j = 0; j < latVal.length; j++){


                                        // LatLng latlng = new LatLng(Double.parseDouble(pointListLat.get(j)),Double.parseDouble(pointListLon.get(j)));
                                        Double lat = latVal[j];
                                        Double lon = lonVal[j];

                                        String time = timeOfPoints.get(j);
                                        Integer speed = tripSpeedPoints.get(j);
                                        DecimalFormat df = new DecimalFormat("#.##");


                                        if(Math.abs(clickedLat - lat)<0.0006 && Math.abs(clickedLon - lon)<0.0006) {
                                            String snippetString;
                                            if(!UnitsSettingsFragment.isKPH) {
                                                snippetString = " Time: " + time + "\n" + " Latitude: " + lat + "\n" + " Longitude: " + lon + "\n" + " Speed: " + speed + " mph";
                                            }else{

                                                DecimalFormat Intform = new DecimalFormat("#");
                                                snippetString = " Time: " + time + "\n" + " Latitude: " + lat + "\n" + " Longitude: " + lon + "\n" + " Speed: " + Integer.valueOf(Intform.format(speed*1.6093)) + " kph";
                                            }
                                            IconFactory iconFactory = IconFactory.getInstance(getContext());
                                            Icon icon = iconFactory.fromResource(R.drawable.mapbox_mylocation_icon_default);
                                            MarkerOptions options = new MarkerOptions().setPosition(new LatLng(lat, lon)).setIcon(icon).setTitle(snippetString);

                                            TripHistoryMap.map.addMarker(options);
                                            //marker.showInfoWindow(TripHistoryMap.map,TripHistoryMap.mapViewPop).update();
                                            break;

                                        }
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(getContext(),"No Points Were Recorded On This Trip",Toast.LENGTH_LONG).show();
                        }
                        lastSelected = v;
                    /*if delete button is pressed*/
  //                  }//else if(!update){
//                        if (hasClicked.get(position, false)){
//                            deletedTrips.add(v);
//                            deletedPositions.add(position);
//                           // tripName = tripList.get(position);
//                            v.setBackgroundResource(R.drawable.on_button_pressed);
//                            hasClicked.put(position, false);
//
//                        } else {
//
//
//                            v.setBackgroundResource(R.drawable.my_border);
//                            for(int j = 0; j<deletedTrips.size();j++){
//                                if(v == deletedTrips.get(j)){
//                                    deletedTrips.remove(j);
//                                }
//                                if(position == deletedPositions.get(j)){
//                                    deletedPositions.remove(j);
//                                }
//                            }
//
//
//                            hasClicked.put(position, true);
//                        }
//
//                        TripHistoryMap.deleteButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if(deletedTrips.size()>0) {
//                                    deleteTripCheck(idOfTrip, deletedPositions, deletedTrips);
//
//
//                                }else{
//                                    Toast.makeText(getContext(),"Please Select A Trip To Delete",Toast.LENGTH_LONG).show();
//                                }
//                            }
//
//                        });

                        /*if edit button is pressed*/
                    }else if(update){
                        updateTripName(getContext(), position, tripList.get(position));
                    }

                }
            });




            // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
        }else{
            ArrayList<String> noTripList = new ArrayList<>();
            noTripList.add("No Trips Recorded Yet");

            // Create ArrayAdapter using the planet list.
            listAdapter = new ArrayAdapter<>(getActivity(), R.layout.simplerow, noTripList);

            tripListView.setAdapter(listAdapter);

            tripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                    String s = tripListView.getItemAtPosition(position).toString();
                    TripHistoryMap.tripLabel.setText(s);
                    Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    TripHistoryMap.map.easeCamera(CameraUpdateFactory.newLatLng(Map.latlng));
                    CameraPosition camPosition = new CameraPosition.Builder().target(Map.latlng).zoom(15) // Sets the zoom
                            .bearing(0) // Rotate the camera
                            .tilt(30) // Set the camera tilt
                            .build(); // Creates a CameraPosition from the builder

                    TripHistoryMap.map.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition), 7000);
                    TripHistoryMap.map.setMyLocationEnabled(true);

                }
            });
        }


        tripListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setBackgroundResource(R.drawable.on_delete_press);
                view.setEnabled(false);
                deleteTripCheck(i,view);
                return true;
            }
        });

        // Set the ArrayAdapter as the ListView's adapter.

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_history_list, container, false);
    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//    }



    public void deleteTripCheck(final int position, final View v){



        TravelHistoryPopup.deleteDialog.setMessage("Are you sure you want to delete trips: \n "+tripList.get(position));
        TravelHistoryPopup.deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                DeleteDate(idOfTrip.get(position),position,v);

            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        v.setBackgroundResource(R.drawable.my_border);
                        v.setEnabled(true);
                    }
                }).create();
        TravelHistoryPopup.deleteDialog.show();


    }
    public void DeleteDate(String ID,int position, View v){

                Integer deletedRows = tripDB.deleteData(ID);

                tripDB.close();
                if(deletedRows>0){
//                    v.setBackgroundColor(Color.parseColor("#222222"));
//                    v.setEnabled(false);

                    v.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Data in row "+tripList.get(position)+" Successfully Deleted",Toast.LENGTH_SHORT).show();

                    //listAdapter.//(tripList.get(position-1));

                    listAdapter.notifyDataSetChanged();
                    startActivity(new Intent(getActivity(),TravelHistoryPopup.class));

                }else{
                    v.setBackgroundResource(R.drawable.my_border);
                    Toast.makeText(getContext(), "Data in row "+position+" Unsuccessfully Deleted",Toast.LENGTH_SHORT).show();
                }


    }


    private void updateTripName(final Context context, final Integer position, String tripName){



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //.getVisibleRegion().latLngBounds;
        final EditText tripNameEdit = new EditText(getContext());
        tripNameEdit.setText(tripName);


        // Build the dialog box
        builder.setTitle("Name Updater")
                .setView(tripNameEdit)
                .setMessage("Update Name of Trip: " + tripName)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trip = tripNameEdit.getText().toString();
                        // Require a region name to begin the download.
                        // If the user-provided string is empty, display
                        // a toast message and do not begin download.
                        if (trip.length() == 0) {
                            Toast.makeText(context, "Please Enter A Trip Name", Toast.LENGTH_SHORT).show();
                        } else {
                            // Begin download process
                            int isUpdated = tripDB.updateData(idOfTrip.get(position), trip);
                            if (isUpdated>0){

                                Toast.makeText(context, "Trip Name Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), TravelHistoryPopup.class));
                        }
                            else {
                                Toast.makeText(getContext(), "Error Updating Trip Name", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                })
                .setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // Display the dialog
        builder.show();

    }

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





}
