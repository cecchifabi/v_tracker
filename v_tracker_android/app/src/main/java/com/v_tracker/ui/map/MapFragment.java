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
import android.widget.TextView;
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

    // Member variables for the map
    private GoogleMap myMap;
    private MapView mapView;
    private View mView;
    private Marker userLocation;
    private Circle userCircle;
    private float currentZoomLevel;

    // Member variables for the location
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult){
            super.onLocationResult(locationResult);

            currentLocation = locationResult.getLastLocation();
            currentZoomLevel = myMap.getCameraPosition().zoom;
            updateMapPosition(false);

            Log.i(MainActivity.V_TRACKER_INFO, "New location (foreground): (" + currentLocation.getLatitude() + ", " +
                    currentLocation.getLongitude() + ")");
        }
    };

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize the graphical elements
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        FloatingActionButton fab = mView.findViewById(R.id.fab);

        // Set a clockListener on the FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check permissions
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

                // Get the first location
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                currentLocation = location;
                                if (currentLocation != null) {
                                    currentZoomLevel = 19;
                                    // Logic to handle location object
                                    Log.i(MainActivity.V_TRACKER_INFO, "New location: (" + currentLocation.getLatitude() +
                                            ", " + currentLocation.getLongitude() + ")");
                                    updateMapPosition(true);
                                }
                                else {
                                    Log.i(MainActivity.V_TRACKER_INFO, "currentLocation = null");
                                }
                            }
                        });

                // Prepare the location request to access the position from now on
                locationRequest = LocationRequest.create()
                        .setInterval(10000)
                        .setFastestInterval(1000)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                requestLocationUpdates();
            }
        });

        return mView;
    }

    private void requestLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void updateMapPosition(boolean moveTarget) {

        if(moveTarget) {
            CameraPosition newPos = CameraPosition.builder().target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                    .bearing(0)
                    .zoom(currentZoomLevel)
                    .tilt(0)
                    .build();

            myMap.moveCamera(CameraUpdateFactory.newCameraPosition(newPos));
        }

        if(userLocation != null) {
            userLocation.remove();
        }
        userLocation = myMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user))
                .rotation(currentLocation.getBearing())
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

    @Override
    public void onPause() {
        super.onPause();

        Log.i(MainActivity.V_TRACKER_INFO, "Fragment paused");

        // Stop getting the position in foreground (the background is going to start)
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(MainActivity.V_TRACKER_INFO, "Fragment resumed");

        // Start getting the position in foreground
        if (fusedLocationClient != null) {
            requestLocationUpdates();
        }
    }
}
