package com.example.nicrodriguez.seniordesign;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicrodriguez.seniordesign.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class TripHistoryGraph extends Fragment {

    public static BarChart barChart;
    ArrayList<String> dates = new ArrayList<>();
    Random random;
    public static ArrayList<BarEntry> barEntries = new ArrayList<>();
    public static BarDataSet barDataSet;
    public static BarData theData;

    public TripHistoryGraph() {
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
        return inflater.inflate(R.layout.fragment_trip_history_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        barChart = view.findViewById(R.id.historyChart);
        barChart.setHighlightPerTapEnabled(false);
        barChart.setHighlightFullBarEnabled(false);
        barChart.setHighlightPerDragEnabled(false);



    }

    public static void createBarGraph(List<BarEntry> average, List<BarEntry> max){
        BarDataSet set1;
        BarDataSet set2;

        if(!UnitsSettingsFragment.isKPH){
                set1 = new BarDataSet(average, "Average (mph)");
                set2 = new BarDataSet(max, "Max (mph)");
            }else{
                set1 = new BarDataSet(average, "Average (kph)");
                set2 = new BarDataSet(max, "Max (kph)");
            }
                set1.setColor(Color.rgb(230, 235, 237));
                set2.setColor(Color.rgb(66, 206, 244));
                float groupSpace = 0.06f;
                float barSpace = 0.02f; // x2 dataset
                float barWidth = 0.45f; // x2 dataset
        // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"

                BarData data = new BarData(set1,set2);
                data.setValueTextSize(10f);
                data.setBarWidth(barWidth); // set the width of each bar
                barChart.setData(data);
                barChart.groupBars(0f, groupSpace, barSpace);
                // perform the "explicit" grouping
                barChart.invalidate();

                XAxis xAxis = barChart.getXAxis();

                xAxis.setCenterAxisLabels(true);
                String[] xAxisString;
                xAxisString = StatsByDayFrag.tripList.toArray(new String[0]);
                MyXAxisValueFormatter xf = new MyXAxisValueFormatter(xAxisString);
                xAxis.setValueFormatter(xf);
                xAxis.setGranularity(1f);
                xAxis.setAxisMinimum(0);
                xAxis.setTextColor(Color.BLACK);
                xAxis.setAxisMaximum(average.size());



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
