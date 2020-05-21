package com.v_tracker.ui.map;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.v_tracker.BuildConfig;

public class MyLocationBackgroundService extends Service {

    public static final String CHANNEL_ID =
            BuildConfig.APPLICATION_ID + ".BACKGROUND_LOCATION";

    private final IBinder mBinder = new LocalBinder();

    private boolean mChangingConfiguration = false;
    private NotificationManager mNotificationManager;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Handler mServiceHandler;

    // https://www.youtube.com/watch?v=o5gdrUliM70 min 6.31

    public class LocalBinder extends Binder {
        MyLocationBackgroundService getService() {
            return MyLocationBackgroundService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
