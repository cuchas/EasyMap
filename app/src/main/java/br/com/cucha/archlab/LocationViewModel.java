package br.com.cucha.archlab;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.location.Location;
import android.renderscript.Allocation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by eduardo on 10/4/17.
 */

public class LocationViewModel extends AndroidViewModel {
    private final LocationDB db;
    private LiveData<List<LocationInfo>> mLocationList = new MutableLiveData<>();
    static String DBNAME = "location";

    public LocationViewModel(Application application) {
        super(application);

        db = Room.databaseBuilder(application, LocationDB.class, DBNAME)
                .fallbackToDestructiveMigration()
                .build();

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

//        List<Location> locationList = getLocationList().getValue();

        //noinspection ConstantConditions
//        locationList.add(location);

//        getLocationList().setValue(locationList);
    }
}
