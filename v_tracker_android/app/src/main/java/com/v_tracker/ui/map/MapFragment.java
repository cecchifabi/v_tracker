package com.v_tracker.ui.map;

// https://androidwave.com/foreground-service-android-example/

import android.Manifest;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.v_tracker.LocationForegroundService;
import com.v_tracker.MainActivity;
import com.v_tracker.R;

import org.greenrobot.eventbus.EventBus;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private final String V_TRACKER_INFO = "V_TRACKER_INFO";

    // Map
    private GoogleMap myMap;
    private MapView mapView;
    private View mView;
    private Marker userLocation;
    private Circle userCircle;
    private float currentZoomLevel;

    // Location
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private LocationRequest locationRequest;

    // Motion detection
    private float xCurr, yCurr, zCurr, magnitude;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            magnitude = (float) Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
            xCurr = event.values[0];
            yCurr = event.values[1];
            zCurr = event.values[2];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_map, container, false);

        FloatingActionButton fab = mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean coarseLocationDenied = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED;
                boolean fineLocationDenied = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED;
                boolean backgroundLocationDenied = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED;

                if (coarseLocationDenied || fineLocationDenied || backgroundLocationDenied) {
                    // Ask the user to allow location access
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }


                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                currentLocation = location;
                                if (currentLocation != null) {
                                    currentZoomLevel = 19;
                                    // Logic to handle location object
                                    Log.i(V_TRACKER_INFO, "New location: (" + currentLocation.getLatitude() +
                                            ", " + currentLocation.getLongitude() + ")");
                                    updateMapPosition();
                                }
                                else {
                                    Log.i(V_TRACKER_INFO, "currentLocation = null");
                                }
                            }
                        });

                // Prepare the location request to access the position from now on
                locationRequest = LocationRequest.create()
                        .setInterval(10000)
                        .setFastestInterval(1000)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                requestLocationUpdates();

                xCurr = 0;
                yCurr = 0;
                zCurr = 0;
                magnitude = 0;
                sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        });

        return mView;
    }

    private void requestLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult){
                super.onLocationResult(locationResult);

                if(magnitude > 10){
                    currentLocation = locationResult.getLastLocation();
                    Log.i(V_TRACKER_INFO, "New location: (" + currentLocation.getLatitude() + ", " +
                            currentLocation.getLongitude() + ") with magnitude = " + magnitude);
                    //Toast.makeText(getContext(), "Location updated", Toast.LENGTH_SHORT);
                }
            }
        }, Looper.getMainLooper());
    }

    public void updateMapPosition() {
        CameraPosition newPos = CameraPosition.builder().target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                    .bearing(0)
                    .zoom(currentZoomLevel)
                    .tilt(0)
                    .build();

        myMap.moveCamera(CameraUpdateFactory.newCameraPosition(newPos));

        if(userLocation != null) {
            userLocation.remove();
        }
        userLocation = myMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user))
        );

        if(userCircle != null) {
            userCircle.remove();
        }
        userCircle = myMap.addCircle(new CircleOptions()
                .center(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .radius(currentLocation.getAccuracy())
                .fillColor(0x80FFFFFF)
                .strokeColor(0x50FFFFFF)
                .strokeWidth(1)
        );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) mView.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
    }

    @Override
    public void onMapReady(GoogleMap map){
        MapsInitializer.initialize(getContext());

        myMap = map;
        CameraPosition initPos = CameraPosition.builder().target(
                new LatLng(0, 0)).zoom(3).bearing(0).tilt(90).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(initPos));
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}
