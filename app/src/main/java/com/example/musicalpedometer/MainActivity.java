package com.example.musicalpedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private LocationManager locationManager;
    private Sensor stepCounter;
    private LocationProvider locationProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assignDataProviders();
    }

    public void startOrStopOnClick(View view) {
        Button button = (Button) view;
        CharSequence curText = button.getText();
        if (curText.equals(getResources().getString(R.string.start))) {
            button.setText(R.string.stop);
            startCollectingData();
        } else {
            System.out.println();
            button.setText(R.string.start);
            stopCollectingData();
        }
    }

    public void downloadReportOnClick() {
        // TODO
    }

    private void assignDataProviders() {
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        locationProvider = locationManager.getProvider(locationManager.getBestProvider(new Criteria(), true));
        // TODO
    }

    private void startCollectingData() {
        // TODO
    }

    private void stopCollectingData() {
        // TODO
    }
}