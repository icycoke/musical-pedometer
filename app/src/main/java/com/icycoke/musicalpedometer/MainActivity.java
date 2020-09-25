package com.icycoke.musicalpedometer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;

    protected boolean locationPermissionGranted;

    private SensorManager sensorManager;
    private Sensor stepCounter;
    private boolean isRunning;
    private StartOrStopOnClickListener startOrStopOnClickListener;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private FragmentManager fragmentManager;
    private GoogleMap googleMap;

    public void startOrStopOnClick(View view) {
        Button button = (Button) view;
        if (isRunning) {
            button.setText(R.string.start);
            stop();
            isRunning = false;
        } else {
            button.setText(R.string.stop);
            start();
            isRunning = true;
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
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        showCurrentLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocationPermission();
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(500);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
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

    private void start() {
        startOrStopOnClickListener.showCurrentLocation();
        // TODO
    }

    private void stop() {
        // TODO
    }

    @SuppressLint("MissingPermission")
    private void showCurrentLocation() {
        getLocationPermission();
        if (locationPermissionGranted) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback(), null);

            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.d(TAG, "onSuccess: current location found");
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                        googleMap.setMyLocationEnabled(true);
                    } else {
                        Log.d(TAG, "onSuccess: current location is null");
                    }
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: get current location failed");
                }
            });
        } else {
            Log.d(TAG, "showCurrentLocation: location permission not granted");
            Toast.makeText(this, "Location permission not granted!", Toast.LENGTH_SHORT).show();
        }
    }

    protected interface StartOrStopOnClickListener {
        void showCurrentLocation();
    }
}