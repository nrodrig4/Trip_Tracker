package com.example.nicrodriguez.seniordesign;


import android.content.Context;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;


public class SpeedGraph extends Fragment {



    private static LineChart chart;
    private static XAxis xl;
    private static LineData data;
    private static ILineDataSet set;
    private static ILineDataSet setAvg;
    private static YAxis yspeed;
    private static YAxis yavg;
    private static GraphFunctions graphFunctions;
    private static List<ILineDataSet> setList;// = new GraphFunctions();
    public SpeedGraph() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        graphFunctions = new GraphFunctions();
        setList = new ArrayList<>();
        data = new LineData();
        chart =  view.findViewById(R.id.chart);
        startGraph(chart);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_speed_graph, container, false);
    }

    private LineChart startGraph(LineChart chart){



        chart = graphFunctions.initilizeChart(chart);
        graphFunctions.setDataTextColor(chart,data);
        graphFunctions.intitializeLegend(chart);
        xl = graphFunctions.initilizeXAxis(chart);
        yspeed = graphFunctions.initilizeYAxisLeft(chart);
        yavg = graphFunctions.initilizeYAxisRight(chart);

        return chart;
    }


    public static void addEntry(float GraphSpeed, int averageSpeed) {
        setList = graphFunctions.addEntryToSpeedSet(chart,data, GraphSpeed, averageSpeed);
        set = setList.get(0);
        setAvg = setList.get(1);


    }


    Thread thread;



//    @Override
//    public void onPause() {
//        super.onPause();
//
//        if (thread != null) {
//            thread.interrupt();
//        }
//    }

    public static boolean graphVisibility(boolean visible){
        if(visible) {
            xl.setEnabled(false);
            yspeed.setEnabled(true);
            yavg.setEnabled(true);
            return true;
        }
        else{
            xl.setEnabled(false);
            yspeed.setEnabled(false);
            yavg.setEnabled(false);
            return false;
        }
    }

    public static void clearGraph(){
        chart.clearValues();

        data.removeDataSet(set);
        data.removeDataSet(setAvg);
        set.clear();
        setAvg.clear();
    }









}
