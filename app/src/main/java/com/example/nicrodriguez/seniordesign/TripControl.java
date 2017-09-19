package com.example.nicrodriguez.seniordesign;



import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.util.List;


class TripControl {


    TripControl(){

    }

    void startNewTrip(final ToggleButton Follow, final ToggleButton Record) {



        Main2Activity.tripDialog.setMessage("Would you like to start a new trip on " + Map.date + " at " + Map.time + " ?");
        Main2Activity.tripDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Follow.setVisibility(View.VISIBLE);
               Main2Activity.tripStart = true;
                SpeedGraph.graphVisibility(true);
                Record.setChecked(false);
                Map.RecordCheck = false;

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Record.setChecked(true);
                        Map.RecordCheck = true;
                    }
                }).create();
        Main2Activity.tripDialog.show();

    }


    void endTrip(final Context context, final ToggleButton Follow, final ToggleButton Record,  final String startHour,  final TripDatabaseHelper tripDB
            , final TravelHistory th, final float averageSpeed, final int maxSpeed, final Double tripDistance, final String date) {

        Main2Activity.tripDialog.setMessage("Would you like end trip on " + Map.date + " at " + Map.time + " ?");
        Main2Activity.tripDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Follow.setVisibility(View.INVISIBLE);
                Record.setChecked(true);
                Map.RecordCheck = true;
                Map.averageSpeed = 0;
                Map.totalSpeed = 0;
                Map.map.removeAnnotations();
                Main2Activity.tripStart = false;
                SpeedGraph.graphVisibility(false);

                if(Map.counter > 0) {
                    SpeedGraph.clearGraph();

                }
                Map.counter = 0;

                Record.setChecked(false);
                Record.setText(R.string.recordText0);
                Map.TripList.add(Map.TripTitle);
                Map.EndOfTrip = Map.dateAndTime;


                StringBuilder stringBuilderLat = new StringBuilder();
                StringBuilder stringBuilderLon = new StringBuilder();
                StringBuilder stringBuilderSpeed = new StringBuilder();
                StringBuilder stringBuilderTime = new StringBuilder();
                for(int k = 0;k<th.Points().size();k++){
                    stringBuilderLat.append(th.lat.get(k)).append("|");
                    stringBuilderLon.append(th.lon.get(k)).append("|");
                    stringBuilderSpeed.append(th.speedHistory.get(k)).append("|");
                    stringBuilderTime.append(th.Time.get(k)).append("|");
                }

                String TripPointsLat = stringBuilderLat.toString();
                String TripPointsLon = stringBuilderLon.toString();
               // String TripLength = numOfHourPassed+"hours";
                String AverageSpeed = Float.toString(averageSpeed);
                String MaxSpeed = Integer.toString(maxSpeed);
                String SpeedPoints = stringBuilderSpeed.toString();
                String timeMeasurements = stringBuilderTime.toString();
                //Toast.makeText(context,TripPointsLat,Toast.LENGTH_LONG).show();
                addTripData(tripDB, context, Map.TripTitle, startHour, TripPointsLat, TripPointsLon,AverageSpeed,MaxSpeed,SpeedPoints,tripDistance.toString(), timeMeasurements ,date);

                //addTripData(TripDatabaseHelper tripDB, Context context, String tripName, String tripLength, String tripPath)


                //Toast.makeText(,"Trip Saved", Toast.LENGTH_SHORT).show();


            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Record.setChecked(false);
                        Map.RecordCheck = false;
                    }
                }).create();
        Main2Activity.tripDialog.show();
    }

    private void addTripData(TripDatabaseHelper tripDB, Context context, String tripName, String tripLength, String tripPathLat
            , String tripPathLon,String averageSpeed, String maxSpeed, String speedPoints, String tripDistance, String timeOfMeasurements, String date){

        Boolean isInserted = tripDB.insertData(tripName,tripLength,tripPathLat, tripPathLon,averageSpeed,maxSpeed,speedPoints,tripDistance, timeOfMeasurements, date);
        if(isInserted){
            Toast.makeText(context, "Trip Succesfully Added To Database",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Error Adding Trip To Database",Toast.LENGTH_SHORT).show();
        }
    }


}
