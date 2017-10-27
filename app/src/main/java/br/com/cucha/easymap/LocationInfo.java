package br.com.cucha.easymap;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

import com.google.android.gms.location.places.Place;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by eduardo on 10/4/17.
 */

@Entity(tableName = "location")
public class LocationInfo {
    @PrimaryKey(autoGenerate = true)
    private int uuid;

    @ColumnInfo(name = "lat")
    private String lat;

    @ColumnInfo(name = "lng")
    private String lng;

    @ColumnInfo(name = "accuracy")
    private String accuracy;

    @ColumnInfo(name = "creation_date")
    private Date creationDate;

    @ColumnInfo(name = "name")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public static LocationInfo of(Place place) {

        if(place.getLatLng() == null || place.getName().length() == 0) return null;

        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setLat(String.valueOf(place.getLatLng().latitude));
        locationInfo.setLng(String.valueOf(place.getLatLng().longitude));
        locationInfo.setName(String.valueOf(place.getName()));
        locationInfo.setCreationDate(Calendar.getInstance().getTime());

        return locationInfo;
    }

    public static LocationInfo of(Location location) {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setLat(String.valueOf(location.getLatitude()));
        locationInfo.setLng(String.valueOf(location.getLongitude()));
        locationInfo.setAccuracy(String.valueOf(location.getAccuracy()));
        locationInfo.setCreationDate(Calendar.getInstance().getTime());

        return locationInfo;
    }
}
