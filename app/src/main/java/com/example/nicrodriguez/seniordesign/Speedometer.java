package com.example.nicrodriguez.seniordesign;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Speedometer extends Fragment {

    public static TextView LatView;
    public static TextView LonView;
    public static TextView SpeedView;
    public static TextView SpeedLimitView;
    public static TextView SpeedAverageView;
    public static TextView AccuracyView;
    public static TextView DistanceTraveled;

    public Speedometer() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SpeedView = view.findViewById(R.id.SpeedometerSpeedView);
        LatView = view.findViewById(R.id.SpeedometerSpeedLatView);
        LonView = view.findViewById(R.id.SpeedometerSpeedLonView);
//        SpeedLimitView = view.findViewById(R.id.SpeedometerSpeedLimitView);
        AccuracyView = view.findViewById(R.id.SpeedometerAccuracyView);
        SpeedAverageView = view.findViewById(R.id.SpeedometerSpeedAverageView);
        DistanceTraveled = view.findViewById(R.id.distanceTraveledText);
//        LatView = LatLabel;
//        LonView = LonLabel;
//        SpeedView = SpeedLabel;
//        SpeedLimitView = SpeedLimitLabel;
//        AccuracyView = AccuracyLabel;
//        SpeedAverageView = AverageSpeedLabel;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speedometer, container, false);
        // Inflate the layout for this fragment
        return view ;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
