package br.com.cucha.archlab;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by eduardo on 10/4/17.
 */

@Entity(tableName = "location")
public class LocationInfo {
//    @PrimaryKey(autoGenerate = true)
//    private int uuid;

    @ColumnInfo(name = "lat")
    private String lat;

    @ColumnInfo(name = "lng")
    private String lng;

    @ColumnInfo(name = "accuracy")
    private String accuracy;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "creation_date")
    private Date creationDate;

//    public int getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(int uuid) {
//        this.uuid = uuid;
//    }

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
}
