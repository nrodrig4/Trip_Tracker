package com.example.nicrodriguez.seniordesign;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by nicrodriguez on 7/9/17.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        if(value < 0){
            value = 0;
        }
        if(value >= mValues.length){
            value = mValues.length-1;
        }
        return mValues[(int) value];
    }

}