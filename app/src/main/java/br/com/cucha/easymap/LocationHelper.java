package br.com.cucha.easymap;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by eduardo on 10/3/17.
 */

public class LocationHelper implements LifecycleObserver, LocationListener {

    private final LocationManager locationManager;
    private final LocationCallback callback;
    private final Context context;
    private final Lifecycle lifecycle;
    private final LocationViewModel viewModel;

    interface LocationCallback {
        void noLocationPermission();
    }

    public LocationHelper(Context context,
                          Lifecycle lifecycle,
                          LocationCallback callback,
                          LocationViewModel viewModel) {

        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.callback = callback;
        this.viewModel = viewModel;
        this.lifecycle = lifecycle;
        this.lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start() {
        findCurrentLocation();
    }

    @SuppressLint("MissingPermission")
    public void findCurrentLocation() {

        if (!PermissionUtils.hasLocationPermission(context) && callback != null) {
            callback.noLocationPermission();
            return;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location == null)
            return;

        viewModel.setMapLocation(LocationInfo.of(location));
    }

    @SuppressLint("MissingPermission")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void resume() {
        if (PermissionUtils.hasLocationPermission(context))  {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, .1f, this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void pause() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        viewModel.setMapLocation(LocationInfo.of(location));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        lifecycle.removeObserver(this);
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
