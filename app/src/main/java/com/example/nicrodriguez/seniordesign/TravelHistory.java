package com.example.nicrodriguez.seniordesign;

import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicrodriguez on 8/4/17.
 */

public class TravelHistory {


    public final List<LatLng> points = new ArrayList<>();
    public final List<Double> lat = new ArrayList<>();
    public final List<Double> lon = new ArrayList<>();
    public final List<LatLng> pointsHistory = new ArrayList<>();

    public final List<PolylineOptions> Line = new ArrayList<>();
    public final ArrayList<Integer> speedHistory = new ArrayList<>();


    public final List<String> Time = new ArrayList<>();
    public final List<String> tripHistory = new ArrayList<>();


    public TravelHistory(){

    }

    public List<LatLng> Points(){
        return this.points;
    }

    public List<Double> Lat(){
        return this.lat;
    }
    public List<Double> Lon(){
        return this.lon;
    }

    public List<LatLng> PointsHistory(){
        return this.pointsHistory;
    }

    public List<PolylineOptions> Line(){
        return this.Line;
    }

    public List<String> Time(){
        return this.Time;
    }

    public List<String> tripHstory(){
        return this.tripHistory;
    }

    public List<Integer> speedHistory(){
        return this.speedHistory;
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
