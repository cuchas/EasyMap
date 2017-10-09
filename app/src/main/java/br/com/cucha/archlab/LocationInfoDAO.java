package br.com.cucha.archlab;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by eduardo on 10/4/17.
 */

@Dao
public interface LocationInfoDAO {
    @Query("SELECT * FROM location ORDER BY creation_date DESC")
    LiveData<List<LocationInfo>> getLocationList();

    @Insert
    long insert(LocationInfo locationInfo);
}
