package br.com.cucha.easymap;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by eduardo on 10/9/17.
 */

public class DBHelper {

    static String DBNAME = "location";
    private static LocationDB mDB;

    public static LocationDB getDBInstance(Context context) {

        if(mDB == null) {
            mDB = Room.databaseBuilder(context, LocationDB.class, DBNAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return mDB;
    }
}
