package com.example.nicrodriguez.seniordesign;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.nicrodriguez.seniordesign.R;


public class UnitsSettingsFragment extends Fragment {

    public Button unitSettings;
    public TextView SettingsTitle,unitSwitchLabel;
    public Switch unitSwitch;
    public static boolean isKPH = false;
    public UnitsSettingsFragment() {
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
        return inflater.inflate(R.layout.fragment_units_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unitSettings = view.findViewById(R.id.unitSettingsButton);
        SettingsTitle = view.findViewById(R.id.settings_title);
        unitSwitch = view.findViewById(R.id.UnitSwitch);
        unitSwitchLabel = view.findViewById(R.id.UnitSwitchLabel);

        if(isKPH){
            unitSwitch.setChecked(false);
            unitSwitchLabel.setText("KPH");
        }else{
            unitSwitch.setChecked(true);
            unitSwitchLabel.setText("MPH");
        }

        unitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton s, boolean b) {
                if(s.isChecked()){
                    isKPH = false;
                    unitSwitchLabel.setText("MPH");
                }else{
                    isKPH = true;
                    unitSwitchLabel.setText("KPH");
                }

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
