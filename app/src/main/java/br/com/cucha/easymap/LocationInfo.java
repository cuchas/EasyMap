package br.com.cucha.easymap;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.util.StringUtil;
import android.location.Location;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

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

    @ColumnInfo(name = "creation_date")
    private Date creationDate;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "id_on_google")
    private String googleID;

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

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

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
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
        locationInfo.setGoogleID(place.getId());
        locationInfo.setCreationDate(Calendar.getInstance().getTime());

        return locationInfo;
    }

    public static LocationInfo of(Location location) {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setLat(String.valueOf(location.getLatitude()));
        locationInfo.setLng(String.valueOf(location.getLongitude()));
        locationInfo.setCreationDate(Calendar.getInstance().getTime());

        return locationInfo;
    }

    public static LocationInfo of(Marker marker) {
        LocationInfo locationInfo = new LocationInfo();
        LatLng position = marker.getPosition();
        locationInfo.setLat(String.valueOf(position.latitude));
        locationInfo.setLng(String.valueOf(position.longitude));
        locationInfo.setName(String.valueOf(marker.getTitle()));
        locationInfo.setGoogleID(String.valueOf(marker.getTag()));
        locationInfo.setCreationDate(Calendar.getInstance().getTime());

        return locationInfo;
    }

    @Ignore
    public boolean isValid() {
        return !StringUtils.isNullOrEmpty(name) &&
                !StringUtils.isNullOrEmpty(lat) &&
                !StringUtils.isNullOrEmpty(lng) &&
                creationDate != null;
    }
}
