package com.v_tracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.v_tracker.ui.Database.Database;
import com.v_tracker.ui.map.MapFragment;
import com.v_tracker.ui.models.Position;

import java.util.Date;

public class LocationForegroundService extends Service {

    public final static double MAGNITUDE_THRESHOLD = 9.85;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    // Member variable for the database
    Database db;
    Position position;

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

    // Location
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult){
            super.onLocationResult(locationResult);

            if(magnitude > MAGNITUDE_THRESHOLD){
                currentLocation = locationResult.getLastLocation();
                Log.i(MainActivity.V_TRACKER_INFO,
                        "New location (background): (" +
                                currentLocation.getLatitude() + ", " +
                                currentLocation.getLongitude() +
                                ") with magnitude = " + magnitude +
                                " and timestamp = " + new Date(currentLocation.getTime()).toString());
                position = new Position(new Date(
                        currentLocation.getTime()).toString(),
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude());
                db.addNewPosition(position);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        if(fusedLocationClient != null) {
            requestLocationUpdates();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(MainActivity.V_TRACKER_INFO, "Foreground service: working in background mode");

        // Initialize the database
        db = new Database();

        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.notification_header))
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        // Prepare the location request to access the position from now on
        locationRequest = LocationRequest.create()
                .setInterval(1200000) // 10 min
                .setFastestInterval(600000) // 5 min
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestLocationUpdates();

        xCurr = 0;
        yCurr = 0;
        zCurr = 0;
        magnitude = 0;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void requestLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }
}