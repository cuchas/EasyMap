package br.com.cucha.easymap;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
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

    @Delete
    int deleteLocation(LocationInfo locationInfo);

    @Insert
    long insert(LocationInfo locationInfo);
}
