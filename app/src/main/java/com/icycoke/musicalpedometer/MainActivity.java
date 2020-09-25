package com.icycoke.musicalpedometer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 17;
    private static final int MARK_NOTHING = 0;
    private static final int MARK_START_POINT = 1;
    private static final int MARK_END_POINT = 2;

    protected boolean locationPermissionGranted;

    private SensorManager sensorManager;
    private Sensor stepCounter;
    private boolean isRunning;
    private StartOrStopOnClickListener startOrStopOnClickListener;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private FragmentManager fragmentManager;
    private GoogleMap googleMap;
    private LocationCallback locationCallback;
    private LatLng lastLatLng;

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

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d(TAG, "onLocationResult: location result received");
                if (locationResult != null) {
                    Log.d(TAG, "onLocationResult: location result is null");
                    Location lastLocation = locationResult.getLastLocation();
                    LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    System.out.println(latLng);
                    if (lastLatLng != null) {
                        googleMap.addPolyline(new PolylineOptions()
                                .color(Color.RED)
                                .add(lastLatLng, latLng));
                    }
                    lastLatLng = latLng;
                    MainActivity.this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        showCurrentLocation(MARK_NOTHING);
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//        Log.d(TAG, "onLocationChanged: location changed");
//        updateMapFragment();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocationPermission();
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(500);

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

    @SuppressLint("MissingPermission")
    private void updateMapFragment() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

    }

    @SuppressLint("MissingPermission")
    private void start() {
        googleMap.clear();
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        showCurrentLocation(MARK_START_POINT);
        // TODO
    }

    private void stop() {
        showCurrentLocation(MARK_END_POINT);
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        // TODO
    }

    @SuppressLint("MissingPermission")
    private void showCurrentLocation(final int markCode) {
        if (locationPermissionGranted) {
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.d(TAG, "onSuccess: current location found");
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                        googleMap.setMyLocationEnabled(true);

                        switch (markCode) {
                            case MARK_START_POINT: {
                                googleMap.addMarker(new MarkerOptions()
                                        .title(getResources().getString(R.string.start_point))
                                        .position(latLng));
                                lastLatLng = latLng;
                            }
                            case MARK_END_POINT: {
                                googleMap.addMarker(new MarkerOptions()
                                        .title(getResources().getString(R.string.end_point))
                                        .position(latLng));
                            }
                        }
                        Log.d(TAG, "onSuccess: current location is shown");
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