package com.icycoke.musicalpedometer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    protected boolean locationPermissionGranted;

    private SensorManager sensorManager;
    private Sensor stepCounter;
    private boolean isRunning;
    private StartOrStopOnClickListener startOrStopOnClickListener;

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: request permission result got");
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission denied");
                locationPermissionGranted = false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocationPermission();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        isRunning = false;
        startOrStopOnClickListener = findViewById(R.id.map);


        assignDataProviders();
    }

    protected void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: granting location permission");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocationPermission: permission has been gotten");
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
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

    protected interface StartOrStopOnClickListener {
        void showCurrentLocation();
    }

}