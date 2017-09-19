package com.example.nicrodriguez.seniordesign;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserStats extends AppCompatActivity {
    Button dayButton,allTimeButton;
    Fragment dayStatsFrag, graphFrag, allTimeStatsFrag;
    View lastPressed;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_user_stats);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.activity_app_bar));
        setTitle(UserLogin.selectedUser + "'s Stats");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        dayButton = (Button) findViewById(R.id.dayButton);
        allTimeButton = (Button) findViewById(R.id.allTimeButton);

        dayStatsFrag = new StatsByDayFrag();
        graphFrag = new TripHistoryGraph();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();


        transaction.replace(R.id.mainStatsFrag, dayStatsFrag);
        transaction.replace(R.id.graphFrag, graphFrag);
        dayButton.setBackgroundResource(R.drawable.left_button_pressed);
        lastPressed = dayButton;
        transaction.commit();

        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastPressed != view){
                    if(lastPressed == allTimeButton){
                        lastPressed.setBackground(getDrawable(R.drawable.right_button));
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        dayStatsFrag = new StatsByDayFrag();
                        transaction.replace(R.id.mainStatsFrag, dayStatsFrag);
                        transaction.commit();
                    }
                    view.setBackground(getDrawable(R.drawable.left_button_pressed));

                    lastPressed = view;

                }

            }
        });



        allTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastPressed != view){
                    if(lastPressed == dayButton) {
                        lastPressed.setBackground(getDrawable(R.drawable.left_button));
                        allTimeStatsFrag = new AllTimeStatsFrag();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.mainStatsFrag, allTimeStatsFrag);

                        transaction.commit();
                    }
                    view.setBackground(getDrawable(R.drawable.right_button_pressed));

                    lastPressed = view;

                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }
}
