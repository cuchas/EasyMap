package br.com.cucha.easymap;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;

import java.util.Calendar;
import java.util.List;

/**
 * Created by eduardo on 10/4/17.
 */

public class LocationViewModel extends AndroidViewModel {
    private final LocationDB db;
    private LiveData<List<LocationInfo>> mLocationList = new MutableLiveData<>();

    public LocationViewModel(Application application) {
        super(application);

        db = DBHelper.getDBInstance(application);

        mLocationList = db.locationInfoDAO().getLocationList();
    }

    public LiveData<List<LocationInfo>> getLocationList() {
        return mLocationList;
    }

    public void setLocation(Location location) {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setLat(String.valueOf(location.getLatitude()));
        locationInfo.setLng(String.valueOf(location.getLongitude()));
        locationInfo.setAccuracy(String.valueOf(location.getAccuracy()));
        locationInfo.setCreationDate(Calendar.getInstance().getTime());

        Runnable runnable = () -> db.locationInfoDAO().insert(locationInfo);

        new Thread(runnable).start();
    }
}
