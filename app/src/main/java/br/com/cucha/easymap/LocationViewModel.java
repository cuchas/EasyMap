package br.com.cucha.easymap;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

/**
 * Created by eduardo on 10/4/17.
 */

public class LocationViewModel extends AndroidViewModel {
    private final LocationDB db;
    private final PrefsHelper prefsHelper;
    private LiveData<List<LocationInfo>> favoriteList = new MutableLiveData<>();
    private MutableLiveData<LocationInfo> mapLocation = new MutableLiveData<>();

    public LocationViewModel(Application application) {
        super(application);

        db = DBHelper.getDBInstance(application);

        favoriteList = db.locationInfoDAO().getLocationList();

        prefsHelper = new PrefsHelper(application);


        LocationInfo lastLocation = prefsHelper.getLastLocation();

        if(lastLocation != null)
            mapLocation.postValue(lastLocation);
    }

    public LiveData<List<LocationInfo>> getFavoriteList() {
        return favoriteList;
    }

    public void setMapLocation(LocationInfo location) {
        mapLocation.setValue(location);

        prefsHelper.setLastLocation(location);
    }

    public LiveData<LocationInfo> getMapLocation() {
        return mapLocation;
    }

    public void setFavoriteLocation(LocationInfo locationInfo) {

        if(locationInfo.isValid()) {
            Runnable runnable = () -> db.locationInfoDAO().insert(locationInfo);

            new Thread(runnable).start();
        }
    }
}
