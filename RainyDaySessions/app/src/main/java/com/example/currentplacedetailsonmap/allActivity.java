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
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
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

/**
 * An activity showing the user's current location along with every indoor skatepark in Scotland.
 */
public class allActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = allActivity.class.getSimpleName();
    private GoogleMap gMap;
    private CameraPosition Camera;

    private GeoDataClient GeoDataClient;
    private PlaceDetectionClient placeDetectionClient;

    private FusedLocationProviderClient fusedLocationProviderClient;

    // The camera is centered on Colinton road if location permission is not granted.
    private final LatLng defaultLocation = new LatLng(55.933033, -3.213820);
    private static final int DEFAULT_ZOOM = 10;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    //The last known location of the handset is saved.
    private Location lastKnownLocation;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    //The skatepark markers are set here so that an onClick function can be used
    private  Marker Trans;
    private  Marker Zone;
    private  Marker Unit;
    private  Marker Shred;
    private  Marker Ryze;
    private  Marker Factory;
    private  Marker TX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            Camera = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a GeoDataClient.
        GeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        placeDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
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

        LatLng trans = new LatLng(55.979768, -3.179149);
        Trans = map.addMarker(new MarkerOptions().position(trans).title("Transgression"));

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

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
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
 //request permission to use location services
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
                // If request is cancelled, the result arrays are empty.
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

            Log.d("Distance", "Distance = " + unit_distance + "km");

            Intent unitWindow = new Intent(allActivity.this, unitActivity.class);
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

            Log.d("Distance", "Distance = " + zone_distance + "km");

            Intent zoneWindow = new Intent(allActivity.this, ZoneActivity.class);
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

            Log.d("Distance", "Distance = " + ryze_distance + "km");

            Intent ryzeWindow = new Intent(allActivity.this, ryzeActivity.class);
            ryzeWindow.putExtra("ryzeDistance", ryze_distance);
            startActivity(ryzeWindow);
        }

        if (marker.equals(Trans))
        {
            Location locationA = new Location("point A");

            locationA.setLatitude(55.979768);
            locationA.setLongitude( -3.179149);

            Location locationB = lastKnownLocation;

            float distance = (locationA.distanceTo(locationB)/1000);
            String trans_distance = String.valueOf(distance);

            Log.d("Distance", "Distance = " + trans_distance + "km");

            Intent transWindow = new Intent(allActivity.this, transActivity.class);
            transWindow.putExtra("transDistance", trans_distance);
            startActivity(transWindow);

        }

        if (marker.equals(Shred))
        {
            Location locationA = new Location("point A");

            locationA.setLatitude(55.480964);
            locationA.setLongitude(-4.603481);

            Location locationB = lastKnownLocation;

            float distance = (locationA.distanceTo(locationB)/1000);
            String shred_distance = String.valueOf(distance);

            Log.d("Distance", "Distance = " + shred_distance + "km");

            Intent shredWindow = new Intent(allActivity.this, shredActivity.class);
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

            Log.d("Distance", "Distance = " + tx_distance + "km");

            Intent txWindow = new Intent(allActivity.this, txActivity.class);
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

            Log.d("Distance", "Distance = " + factory_distance + "km");

            Intent factoryWindow = new Intent(allActivity.this, factoryActivity.class);
            factoryWindow.putExtra("factoryDistance", factory_distance);
            startActivity(factoryWindow);

        }
        return false;
    }
}
