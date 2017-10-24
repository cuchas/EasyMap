package br.com.cucha.easymap;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by eduardo on 10/9/17.
 */

public class DBTypeConverters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
