package br.com.cucha.archlab;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;

/**
 * Created by eduardo on 10/4/17.
 */

public class LocationViewModel extends ViewModel {
    MutableLiveData<Location> mLocation = new MutableLiveData<>();

    public MutableLiveData<Location> getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation.setValue(location);
    }
}
