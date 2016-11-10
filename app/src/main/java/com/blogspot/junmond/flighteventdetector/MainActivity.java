package com.blogspot.junmond.flighteventdetector;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static FlightEventFinder eventFinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventFinder = new FlightEventFinder(this, getApplicationContext());
        eventFinder.RefreshEvents();
    }
}
