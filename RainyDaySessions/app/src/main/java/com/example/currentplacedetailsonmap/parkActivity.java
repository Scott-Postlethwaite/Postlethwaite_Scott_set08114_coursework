package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class parkActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = parkActivity.class.getSimpleName();
    private GoogleMap gMap;
    private CameraPosition Camera;

    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Colinton road) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(55.933033, -3.213820);
    private static final int DEFAULT_ZOOM = 10;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";


    //The skatepark markers are set here so that an onClick function can be used
    private  Marker Zone;
    private  Marker Unit;
    private  Marker Shred;
    private  Marker Ryze;
    private  Marker Factory;
    private  Marker TX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            Camera = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_maps);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (gMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, gMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        gMap = map;

        map.setOnMarkerClickListener(this);

        LatLng zone = new LatLng(55.781739, -4.173332);
        Zone = map.addMarker(new MarkerOptions().position(zone).title("Zone 74"));

        LatLng ryze = new LatLng(55.866139, -3.054392);
        Ryze = map.addMarker(new MarkerOptions().position(ryze).title("Ryze & Roll"));

        LatLng unit = new LatLng(55.938488, -4.554420);
        Unit = map.addMarker(new MarkerOptions().position(unit).title("Unit 23"));

        LatLng shred = new LatLng(55.480964, -4.603481);
        Shred =  map.addMarker(new MarkerOptions().position(shred).title("Shred Skatepark"));

        LatLng factory = new LatLng(56.480952, -2.920681);
        Factory = map.addMarker(new MarkerOptions().position(factory).title("The Factory Skatepark"));

        LatLng tx = new LatLng(57.154086, -2.081484);
        TX = map.addMarker(new MarkerOptions().position(tx).title("Transition eXtreme Skatepark"));

        getLocationPermission();

        updateLocationUI();

        getDeviceLocation();
    }

    private void getDeviceLocation() {
        /*
         * if the user has granted location permission, the map is centered on their location
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            gMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            gMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
            asks for location permission in order to get the devices location
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    private void updateLocationUI() {
        if (gMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                gMap.setMyLocationEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(Unit))
        {
            Location locationA = new Location("point A");

            locationA.setLatitude(55.938488);
            locationA.setLongitude( -4.554420);

            Location locationB = lastKnownLocation;

            float distance = (locationA.distanceTo(locationB)/1000);
            String unit_distance = String.valueOf(distance);

            Intent unitWindow = new Intent(parkActivity.this, unitActivity.class);
            unitWindow.putExtra("unitDistance", unit_distance);
            startActivity(unitWindow);

        }

        if (marker.equals(Zone))
        {
            Log.d("Zone Marker", "It Works!");

            Location locationA = new Location("point A");

            locationA.setLatitude(55.781739);
            locationA.setLongitude( -4.173332);

            Location locationB = lastKnownLocation;

            float distance = (locationA.distanceTo(locationB)/1000);
            String zone_distance = String.valueOf(distance);

            Intent zoneWindow = new Intent(parkActivity.this, ZoneActivity.class);
            zoneWindow.putExtra("zoneDistance", zone_distance);
            startActivity(zoneWindow);



        }

        if (marker.equals(Ryze))
        {

            Location locationA = new Location("point A");

            locationA.setLatitude(55.866139);
            locationA.setLongitude( -3.054392);

            Location locationB = lastKnownLocation;

            float distance = (locationA.distanceTo(locationB)/1000);
            String ryze_distance = String.valueOf(distance);

            Intent ryzeWindow = new Intent(parkActivity.this, ryzeActivity.class);
            ryzeWindow.putExtra("ryzeDistance", ryze_distance);
            startActivity(ryzeWindow);
        }

        if (marker.equals(Shred))
        {
            Location locationA = new Location("point A");

            locationA.setLatitude(55.480964);
            locationA.setLongitude(-4.603481);

            Location locationB = lastKnownLocation;

            float distance = (locationA.distanceTo(locationB)/1000);
            String shred_distance = String.valueOf(distance);

            Intent shredWindow = new Intent(parkActivity.this, shredActivity.class);
            shredWindow.putExtra("shredDistance", shred_distance);
            startActivity(shredWindow);
        }

        if (marker.equals(TX))
        {
            Location locationA = new Location("point A");
            locationA.setLatitude(57.154086);
            locationA.setLongitude(-2.081484);

            Location locationB = lastKnownLocation;

            float distance = (locationA.distanceTo(locationB)/1000);
            String tx_distance = String.valueOf(distance);

            Intent txWindow = new Intent(parkActivity.this, txActivity.class);
            txWindow.putExtra("txDistance", tx_distance);
            startActivity(txWindow);

        }

        if (marker.equals(Factory))
        {
            Location locationA = new Location("point A");

            locationA.setLatitude(56.480952);
            locationA.setLongitude( -2.920681);

            Location locationB = lastKnownLocation;

            float distance = (locationA.distanceTo(locationB)/1000);
            String factory_distance = String.valueOf(distance);

            Intent factoryWindow = new Intent(parkActivity.this, factoryActivity.class);
            factoryWindow.putExtra("factoryDistance", factory_distance);
            startActivity(factoryWindow);

        }
        return false;
    }
}
