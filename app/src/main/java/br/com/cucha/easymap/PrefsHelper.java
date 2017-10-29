package br.com.cucha.easymap;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by eduardocucharro on 29/10/17.
 */

public class PrefsHelper {

    private static final String MAP_PREFS = "MAP_PREFS";
    private static final String LAST_LOCATION_NAME = "LAST_LOCATION_NAME";
    private static final String LAST_LOCATION_LAT = "LAST_LOCATION_LAT";
    private static final String LAST_LOCATION_LNG = "LAST_LOCATION_LNG";
    SharedPreferences preferences;

    public PrefsHelper(Context context) {
        preferences = context.getSharedPreferences(MAP_PREFS, Context.MODE_PRIVATE);
    }

    public void setLastLocation(LocationInfo location) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(LAST_LOCATION_NAME, String.valueOf(location.getName()));
        edit.putString(LAST_LOCATION_LAT, String.valueOf(location.getLat()));
        edit.putString(LAST_LOCATION_LNG, String.valueOf(location.getLng()));

        edit.apply();
    }

    public LocationInfo getLastLocation() {
        String lat = preferences.getString(LAST_LOCATION_LAT, null);

        if(lat == null) return null;

        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setLat(lat);
        locationInfo.setLng(preferences.getString(LAST_LOCATION_LNG, null));
        locationInfo.setName(preferences.getString(LAST_LOCATION_NAME, null));

        return locationInfo;
    }
}
