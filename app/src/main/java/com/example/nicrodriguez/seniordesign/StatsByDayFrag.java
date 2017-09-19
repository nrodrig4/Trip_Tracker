package com.example.nicrodriguez.seniordesign;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class StatsByDayFrag extends Fragment {
    TextView dayAverageSpeed, dayMaxSpeed, dayDistanceTraveled, dayTimeOnRoad, dayNumberOfTrips
            ,dayTripListTitle;
    CalendarView dayCalender;
    ListView dayTripListView;
    TripDatabaseHelper db;

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
    static List<String> tripList = new ArrayList<>();

    String selDate;


    public StatsByDayFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats_by_day, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dayAverageSpeed = view.findViewById(R.id.dayAverageSpeed);
        dayMaxSpeed = view.findViewById(R.id.dayMaxSpeed);
        dayDistanceTraveled = view.findViewById(R.id.dayDistanceTraveledText);
        dayTimeOnRoad = view.findViewById(R.id.dayTimeOnRoad);
        dayNumberOfTrips = view.findViewById(R.id.dayNumberOfTrips);
        dayCalender = view.findViewById(R.id.dayCalenderView);
        dayTripListTitle = view.findViewById(R.id.dayTripListTitle);
        dayTripListView = view.findViewById(R.id.dayTripListView);

        db = new TripDatabaseHelper(getContext());
        Cursor res = db.getAllData();

        if(res.getCount()>0) {


            Calendar c = Calendar.getInstance();
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            int year = c.get(Calendar.YEAR);

            String firstDate = month+"/"+day+"/"+year;




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
            if(tripList.size()>0){
                tripList.clear();
                averages.clear();
                maxSpeed.clear();
                timeOnRoad.clear();
            }

            for (int j = 0; j < date.size(); j++) {
                if (date.get(j).equals(firstDate)) {
                    counter++;
                    tripList.add(tripDBList.get(j));
                    averages.add(averageSpeeds.get(j));
                    maxSpeed.add(maxSpeeds.get(j));
                    timeOnRoad.add(tripTime.get(j));
                    traveledDistances.add(travelDistances.get(j));

                }

            }



            if (counter == 0) {
                tripList.clear();
                tripList.add("No Trips On " + firstDate);
                listAdapter = new ArrayAdapter<>(getActivity(), R.layout.simplerow, tripList);
                dayTripListView.setAdapter(listAdapter);

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
                    distanceTraveledText = "Distance Traveled: -- km";

                }
                String numTripText = "Number of Trips: 0";
                String timeText = "Time On Road: -- ";
                String tripListTitle = "Trips Recorded On "+firstDate;
                dayAverageSpeed.setText(averageSpeedText);
                dayMaxSpeed.setText(maxSpeedText);
                dayDistanceTraveled.setText(distanceTraveledText);
                dayNumberOfTrips.setText(numTripText);
                dayTimeOnRoad.setText(timeText);
                dayTripListTitle.setText(tripListTitle);
                Toast.makeText(getContext(), "You Did Not Record A Trip On This Date", Toast.LENGTH_LONG).show();

            }else{
                int maxS = 0;
                int S;
                double Avg = 0;
                double totalDistance = 0;
                int totalHourPassed = 0, totalMinPassed =0, totalSecPassed=0;
                String[] startTime = new String[counter];
                String[] endTime = new String[counter];
                String delims = "[|]+";

                Integer[] startHour = new Integer[counter],endHour = new Integer[counter];
                Integer[] startMin = new Integer[counter],endMin = new Integer[counter];
                Integer[] startSec = new Integer[counter],endSec = new Integer[counter];
                Integer[] hoursPassed = new Integer[counter];
                Integer[] minPassed = new Integer[counter];
                Integer[] secPassed = new Integer[counter];
                String[] startComponents;
                String[] endComponents;



                for(int j = 0; j<counter;j++) {
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
                Avg /=counter;
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
//                Avg = Double.valueOf(df.format(Avg));
//                totalDistance = Double.valueOf(df.format(totalDistance));
//
//
//                String averageSpeedText = "Average Speed: "+ Avg +" mph";
//                String maxSpeedText = "Max Speed: "+maxS + " mph";
//                String distanceTraveledText = "Distance Traveled: "+totalDistance+" mi";
                String numTripText = "Number of Trips: "+counter;
                String tripListTitle = "Trips Recorded On "+firstDate;
                String timePassedText = "Time On Road:  "+totalHourPassed+"h:"+totalMinPassed+"m:"+totalSecPassed+"s";
                dayAverageSpeed.setText(averageSpeedText);
                dayMaxSpeed.setText(maxSpeedText);
                dayDistanceTraveled.setText(distanceTraveledText);
                dayNumberOfTrips.setText(numTripText);
                dayTripListTitle.setText(tripListTitle);
                dayTimeOnRoad.setText(timePassedText);
                listAdapter = new ArrayAdapter<>(getActivity(), R.layout.simplerow, tripList);
                dayTripListView.setAdapter(listAdapter);

                Float[] AverageSpeeds = new Float[averages.size()];
                Float[] maxSpeedsStat = new Float[averages.size()];
                List<BarEntry> tripStatAvg = new ArrayList<>();
                List<BarEntry> tripStatMax = new ArrayList<>();

                for (int j = 0; j < averages.size(); j++) {
                    AverageSpeeds[j] = Float.parseFloat(averages.get(j));
                    maxSpeedsStat[j] = Float.parseFloat(maxSpeed.get(j));
                    //Toast.makeText(getContext(), ""+averages.size(), Toast.LENGTH_LONG).show();

                    //sb.append(maxSpeedsStat[j] + "\n");
                    if(!UnitsSettingsFragment.isKPH){
                        tripStatAvg.add(new BarEntry(j, AverageSpeeds[j]));
                        tripStatMax.add(new BarEntry(j, maxSpeedsStat[j]));
                    }else{
                        AverageSpeeds[j] = Float.valueOf(df.format(AverageSpeeds[j]*1.6094));
                        maxSpeedsStat[j] = Float.valueOf(df.format(maxSpeedsStat[j]*1.6094));
                        tripStatAvg.add(new BarEntry(j, AverageSpeeds[j]));
                        tripStatMax.add(new BarEntry(j, maxSpeedsStat[j]));
                    }

                if(TripHistoryGraph.barChart != null) {

                    TripHistoryGraph.createBarGraph(tripStatAvg, tripStatMax);
                    Description desc = new Description();
                    desc.setText("Trip Data For Trips On " + firstDate);
                    TripHistoryGraph.barChart.setDescription(desc);
                }

                }

            }




            dayCalender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                    if(tripList.size()>0){
                        tripList.clear();
                    }
                    int month = i1 + 1;
                    int day = i2;
                    int year = i;
                    String selectedDate = month + "/" + day + "/" + year;
                    int counter = 0;
                    List<String> averages = new ArrayList<>();
                    List<String> maxSpeed = new ArrayList<>();
                    List<String> traveledDistances = new ArrayList<>();
                    List<String> timeOnRoad = new ArrayList<>();
                    for (int j = 0; j < date.size(); j++) {
                        if (date.get(j).equals(selectedDate)) {
                            counter++;
                            tripList.add(tripDBList.get(j));
                            averages.add(averageSpeeds.get(j));
                            maxSpeed.add(maxSpeeds.get(j));
                            traveledDistances.add(travelDistances.get(j));
                            timeOnRoad.add(tripTime.get(j));
                        }
                    }



                    if (counter == 0) {
                        tripList.clear();
                        tripList.add("No Trips On " + selectedDate);
                        listAdapter = new ArrayAdapter<>(getActivity(), R.layout.simplerow, tripList);
                        dayTripListView.setAdapter(listAdapter);

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
                            distanceTraveledText = "Distance Traveled: -- km";
                        }

                        String numTripText = "Number of Trips: 0";
                        String timeText = "Time On Road: -- ";
                        String tripListTitle = "Trips Recorded On "+selectedDate;

                        dayAverageSpeed.setText(averageSpeedText);
                        dayMaxSpeed.setText(maxSpeedText);
                        dayDistanceTraveled.setText(distanceTraveledText);
                        dayNumberOfTrips.setText(numTripText);
                        dayTripListTitle.setText(tripListTitle);
                        dayTimeOnRoad.setText(timeText);
                        TripHistoryGraph.barChart.clear();
                        Toast.makeText(getContext(), "You Did Not Record A Trip On This Date", Toast.LENGTH_LONG).show();

                    }else{


                        int maxS = 0;
                        int S;
                        double Avg = 0;
                        double totalDistance = 0;
                        int totalHourPassed = 0, totalMinPassed =0, totalSecPassed=0;

                        String[] startTime = new String[counter];
                        String[] endTime = new String[counter];
                        String[] totalTime;

                        Integer[] startHour = new Integer[counter ],endHour = new Integer[counter];
                        Integer[] startMin = new Integer[counter],endMin = new Integer[counter];
                        Integer[] startSec = new Integer[counter],endSec = new Integer[counter];
                        Integer[] hoursPassed = new Integer[counter];
                        Integer[] minPassed = new Integer[counter];
                        Integer[] secPassed = new Integer[counter];
                        String[] startComponents;
                        String[] endComponents;



                        for(int j = 0; j<counter;j++){
                            totalTime = timeOnRoad.get(j).split("[|]+");
                            startTime[j] = totalTime[0];
                            endTime[j] = totalTime[1];

                            String startString = startTime[j].substring(0,startTime[j].indexOf("_"));
                            String endString = endTime[j].substring(0,endTime[j].indexOf("_"));

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

                            // Create ArrayAdapter using the trip list.

                            S = Integer.parseInt(maxSpeed.get(j));
                            if(S > maxS){
                                maxS = S;
                            }
                            Avg += Double.parseDouble(averages.get(j));
                            totalDistance += Double.parseDouble(traveledDistances.get(j));
                            totalHourPassed += hoursPassed[j];
                            totalMinPassed += minPassed[j];
                            if(totalMinPassed>60){
                                totalHourPassed++;
                                totalMinPassed -= 60;
                            }
                            totalSecPassed += secPassed[j];
                            if(totalSecPassed>60){
                                totalMinPassed++;
                                totalSecPassed -= 60;
                            }
                        }

                        DecimalFormat df = new DecimalFormat("#.##");
                        Avg /=counter;
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

                        String numTripText = "Number of Trips: "+counter;
                        String tripListTitle = "Trips Recorded On "+selectedDate;
                        String timePassedText = "Time On Road:  "+totalHourPassed+"h:"+totalMinPassed+"m:"+totalSecPassed+"s";



                            Float[] AverageSpeeds = new Float[averages.size()];
                            Float[] maxSpeedsStat = new Float[averages.size()];
                            List<BarEntry> tripStatAvg = new ArrayList<>();
                            List<BarEntry> tripStatMax = new ArrayList<>();

                            for (int j = 0; j < averages.size(); j++) {
                                AverageSpeeds[j] = Float.parseFloat(averages.get(j));
                                maxSpeedsStat[j] = Float.parseFloat(maxSpeed.get(j));
                                //Toast.makeText(getContext(), ""+averages.size(), Toast.LENGTH_LONG).show();

                                //sb.append(maxSpeedsStat[j] + "\n");

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
                        desc.setText("Trip Data For Trips On "+selectedDate);
                        TripHistoryGraph.barChart.setDescription(desc);
//                            String s = sb.toString();
//                            Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
//                            for (int j = 0; j < maxSpeedsStat.length; j++) {
//                                //Toast.makeText(getContext(), ""+averages.size(), Toast.LENGTH_LONG).show();
//

//
//
//                            }




                        dayAverageSpeed.setText(averageSpeedText);
                        dayMaxSpeed.setText(maxSpeedText);
                        dayDistanceTraveled.setText(distanceTraveledText);
                        dayNumberOfTrips.setText(numTripText);
                        dayTripListTitle.setText(tripListTitle);
                        dayTimeOnRoad.setText(timePassedText);
                        listAdapter = new ArrayAdapter<>(getActivity(), R.layout.simplerow, tripList);
                        dayTripListView.setAdapter(listAdapter);

                    }


                }
            });
        }else{

            Toast.makeText(getContext(), "No Trips Recorded", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
