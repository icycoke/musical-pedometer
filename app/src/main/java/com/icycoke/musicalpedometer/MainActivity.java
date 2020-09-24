package com.icycoke.musicalpedometer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    protected interface StartOrStopOnClickListener {
        void showCurrentLocation();
    }

    private SensorManager sensorManager;
    private Sensor stepCounter;

    private boolean isRunning;
    private StartOrStopOnClickListener startOrStopOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        isRunning = false;
        startOrStopOnClickListener = findViewById(R.id.map);


        assignDataProviders();
    }

    public void startOrStopOnClick(View view) {
        Button button = (Button) view;
        if (isRunning) {
            button.setText(R.string.start);
            stop();
        } else {
            button.setText(R.string.stop);
            start();
        }
    }

    public void downloadReportOnClick() {
        // TODO
    }

    public void setStartOrStopOnClickListener(StartOrStopOnClickListener startOrStopOnClickListener) {
        this.startOrStopOnClickListener = startOrStopOnClickListener;
    }

    private void assignDataProviders() {
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        locationProvider = locationManager.getProvider(locationManager.getBestProvider(new Criteria(), true));
        // TODO
    }

    private void start() {
        startOrStopOnClickListener.showCurrentLocation();
        // TODO
    }

    private void stop() {
        // TODO
    }
}