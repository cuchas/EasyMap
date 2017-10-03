package br.com.cucha.archlab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

/**
 * Created by eduardo on 10/3/17.
 */

public class CustomLocationListener implements LifecycleObserver, LocationListener {

    private final LocationManager mLocationManager;
    private final LocationCallback mCallback;
    private final Context mContext;
    private final Lifecycle mLifeCycle;
    private static String finLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    interface LocationCallback {

        void noLocationPermission();

        void newLocationReceived(Location location);
    }

    public CustomLocationListener(Context context, Lifecycle lifecycle, LocationCallback callback) {
        mContext = context;
        mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        mCallback = callback;
        mLifeCycle = lifecycle;
        mLifeCycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start() {
        int permission = ContextCompat.checkSelfPermission(mContext, finLocationPermission);

        if(permission == PackageManager.PERMISSION_DENIED && mCallback != null) {
            mCallback.noLocationPermission();
            return;
        }

        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location == null || mCallback == null)
            return;

        mCallback.newLocationReceived(location);
    }

    @SuppressLint("MissingPermission")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void resume() {
        int permission = ContextCompat.checkSelfPermission(mContext, finLocationPermission);

        if(permission == PackageManager.PERMISSION_GRANTED)
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1f, this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void pause() {
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(mCallback == null) return;

        mCallback.newLocationReceived(location);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        mLifeCycle.removeObserver(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
