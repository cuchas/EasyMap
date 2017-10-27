package br.com.cucha.easymap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by eduardo on 10/27/17.
 */

public class PermissionUtils {

    public static boolean hasLocationPermission(Context context) {
        int permission =
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        return permission == PackageManager.PERMISSION_GRANTED;
    }
}
