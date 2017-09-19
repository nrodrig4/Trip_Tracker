package com.example.nicrodriguez.seniordesign;


import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicrodriguez on 8/9/17.
 */

public class GraphFunctions {

    public  Legend legend;
    public LineChart chart;
    public LineData data = new LineData();

    public GraphFunctions(){

    }

    public LineChart initilizeChart(LineChart chart){

        chart.setDescription(new Description());
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);

        return chart;
    }

    public Legend intitializeLegend(LineChart chart){
        legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.WHITE);
        return this.legend;
    }

    public LineData setDataTextColor(LineChart chart,LineData data){

        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        return data;
    }

    public XAxis initilizeXAxis(LineChart chart){
        XAxis xl = chart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        return xl;
    }

    public YAxis initilizeYAxisLeft(LineChart chart){
        YAxis Left = chart.getAxisLeft();
        Left.setTextColor(Color.WHITE);
        Left.setDrawGridLines(true);

        Left.setAxisMinimum(0f);
        return Left;
    }


    public YAxis initilizeYAxisRight(LineChart chart){
        YAxis Right = chart.getAxisLeft();
        Right.setTextColor(Color.WHITE);
        Right.setDrawGridLines(false);

        Right.setAxisMinimum(0f);
        return Right;
    }

    public List<ILineDataSet> addEntryToSpeedSet(LineChart chart, LineData data, float Speed, int averageSpeed){
        ILineDataSet set, setAvg;
        data = chart.getData();
        if (data != null) {

            set = data.getDataSetByIndex(0);
            setAvg = data.getDataSetByIndex(1);
            // set.addEntry(...); // can be called as well

            if (set == null)  {
                set = createSet();
                data.addDataSet(set);
            }

            if (setAvg == null)  {
                setAvg = createSetAvg();
                data.addDataSet(setAvg);
            }

            data.addEntry(new Entry(set.getEntryCount(),averageSpeed),1);
            data.addEntry(new Entry(set.getEntryCount(), Speed), 0);

            data.notifyDataChanged();


            // let the chart know it's data has changed
            chart.notifyDataSetChanged();

            // limit the number of visible entries
            chart.setVisibleXRangeMaximum(200);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            chart.moveViewToX(data.getEntryCount());

            List<ILineDataSet> setList = new ArrayList<>();
            setList.add(set);
            setList.add(setAvg);

            return setList;
            //return set;
        }




        return null;
       // return null;
    }


    public static LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null, "Speed");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(2f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);

        return set;
    }

    public static LineDataSet createSetAvg(){
        LineDataSet setAvg = new LineDataSet(null,"Average Speed");
        //set.setAxisDependency(YAxis.AxisDependency.LEFT);
        setAvg.setColor(Color.GREEN);
        setAvg.setCircleColor(Color.YELLOW);
        setAvg.setLineWidth(2f);
        setAvg.setCircleRadius(2f);
        setAvg.setFillAlpha(65);
        setAvg.setFillColor(Color.GREEN);
        setAvg.setHighLightColor(Color.rgb(244, 117, 117));
        setAvg.setValueTextColor(Color.YELLOW);
        setAvg.setValueTextSize(9f);
        setAvg.setDrawValues(false);

        return setAvg;
    }





}
