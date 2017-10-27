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
    private LiveData<List<LocationInfo>> favoriteList = new MutableLiveData<>();
    private MutableLiveData<LocationInfo> mapLocation = new MutableLiveData<>();

    public LocationViewModel(Application application) {
        super(application);

        db = DBHelper.getDBInstance(application);

        favoriteList = db.locationInfoDAO().getLocationList();
    }

    public LiveData<List<LocationInfo>> getFavoriteList() {
        return favoriteList;
    }

    public void setMapLocation(LocationInfo location) {
        mapLocation.setValue(location);
    }

    public LiveData<LocationInfo> getMapLocation() {
        return mapLocation;
    }

    public void setFavoriteLocation(LocationInfo locationInfo) {

        Runnable runnable = () -> db.locationInfoDAO().insert(locationInfo);

        new Thread(runnable).start();
    }
}
