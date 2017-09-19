package com.example.nicrodriguez.seniordesign;

import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    Button profileButton, unitButton, arduinoButton;
    Fragment ProfileFrag, UnitFrag, ArduinoFrag, currentFrag;
    View lastPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.activity_app_bar));
        setTitle(UserLogin.selectedUser+"'s Settings");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        profileButton = (Button) findViewById(R.id.profileSettingsButton);
        unitButton = (Button) findViewById(R.id.unitSettingsButton);
        unitButton.setBackground(getDrawable(R.drawable.left_button_pressed));
        lastPressed = unitButton;
        arduinoButton = (Button) findViewById(R.id.arduinoSettingsButton);
        ProfileFrag = new ProfileSettings();
        UnitFrag = new UnitsSettingsFragment();
        ArduinoFrag = new BlankFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment2, UnitFrag);

        currentFrag = UnitFrag;
        transaction.commit();


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment2, ProfileFrag);

                transaction.commit();
                if(lastPressed != view){
                    if(lastPressed == unitButton){
                        lastPressed.setBackground(getDrawable(R.drawable.left_button));
                    }else if(lastPressed == arduinoButton){
                        lastPressed.setBackground(getDrawable(R.drawable.right_button));

                    }
                view.setBackground(getDrawable(R.drawable.middle_button_pressed));

                lastPressed = view;
                currentFrag = ProfileFrag;
                }
            }
        });

        unitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment2, UnitFrag);
                transaction.commit();
                if(lastPressed != view) {
                    if(lastPressed == profileButton){
                        lastPressed.setBackground(getDrawable(R.drawable.middle_button));
                    }else if(lastPressed == arduinoButton){
                        lastPressed.setBackground(getDrawable(R.drawable.right_button));

                    }
                    view.setBackground(getDrawable(R.drawable.left_button_pressed));

                    lastPressed = view;
                    currentFrag = UnitFrag;
                }
            }
        });

        arduinoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment2, ArduinoFrag);
                transaction.commit();
                    if(lastPressed != view) {
                        if(lastPressed == unitButton){
                            lastPressed.setBackground(getDrawable(R.drawable.left_button));
                        }else if(lastPressed == arduinoButton){
                            lastPressed.setBackground(getDrawable(R.drawable.right_button));

                        }
                        view.setBackground(getDrawable(R.drawable.right_button_pressed));
                        lastPressed.setBackground(getDrawable(R.drawable.middle_button));
                        lastPressed = view;
                        currentFrag = ArduinoFrag;
                    }

            }
        });

    }





}
