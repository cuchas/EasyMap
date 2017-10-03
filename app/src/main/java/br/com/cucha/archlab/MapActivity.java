package br.com.cucha.archlab;

import android.Manifest;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MapActivity extends AppCompatActivity implements
        CustomLocationListener.LocationCallback {

    private static final int REQUEST_LOCATION_CODE = 1001;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        textView = findViewById(R.id.text);

        new CustomLocationListener(this, getLifecycle(), this);
    }

    @Override
    public void noLocationPermission() {
        String[] permissions = new String[] { Manifest.permission.ACCESS_FINE_LOCATION };

        ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_CODE);
    }

    @Override
    public void newLocationReceived(Location location) {
        String formattedDate = SimpleDateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

        String format = String.format("lat: %s\nlong: %s\ndate: %s",
                location.getLatitude(),
                location.getLongitude(),
                formattedDate);

        textView.setText(format);
    }
}
