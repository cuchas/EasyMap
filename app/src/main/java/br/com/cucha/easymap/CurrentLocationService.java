package br.com.cucha.easymap;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Calendar;

public class CurrentLocationService extends Service implements LocationListener {

    public static final int LOCATION_ID = 1;
    public static final int REQUEST_STOP = 1001;
    public static final String STOP_TRACKING = "br.com.cucha.archlab." + "STOP_TRACKING";
    private LocationManager mLocationManager;

    public static final Intent newIntent(Context context) {
        Intent intent = new Intent(context, CurrentLocationService.class);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public CurrentLocationService() {
    }

    BroadcastReceiver stopTrackingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);

            notificationManager.cancel(LOCATION_ID);

            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);

            mLocationManager.removeUpdates(CurrentLocationService.this);

            stopSelf();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(PackageManager.PERMISSION_DENIED == permission) {
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        Intent stopIntent = newStopIntent(this);

        int cancelCurrent = PendingIntent.FLAG_CANCEL_CURRENT;

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, REQUEST_STOP, stopIntent, cancelCurrent);

        String title = getString(R.string.close);

        NotificationCompat.Action action =
                new NotificationCompat.Action(R.drawable.ic_clear_black_24dp, title, pendingIntent);

        Notification notification =
                new NotificationCompat.Builder(this.getApplicationContext(), NotificationCompat.CATEGORY_STATUS)
                        .setContentTitle(getString(R.string.location_tracking))
                        .setContentText(getString(R.string.running))
                        .addAction(action)
                        .build();

        IntentFilter filter = new IntentFilter(STOP_TRACKING);

        LocalBroadcastManager.getInstance(this).registerReceiver(stopTrackingReceiver, filter);

        startForeground(LOCATION_ID, notification);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);

        return super.onStartCommand(intent, flags, startId);
    }

    @NonNull
    public static Intent newStopIntent(Context context) {
        Intent stopIntent = new Intent(context, CurrentLocationService.class);
        stopIntent.setAction(STOP_TRACKING);
        return stopIntent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setLat(String.valueOf(location.getLatitude()));
        locationInfo.setLng(String.valueOf(location.getLongitude()));
        locationInfo.setAccuracy(String.valueOf(location.getAccuracy()));
        locationInfo.setCreationDate(Calendar.getInstance().getTime());

        LocationDB db = DBHelper.getDBInstance(this);

        Runnable runnable = () -> db.locationInfoDAO().insert(locationInfo);

        new Thread(runnable).start();
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
