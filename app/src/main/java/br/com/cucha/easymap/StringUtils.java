package br.com.cucha.easymap;

/**
 * Created by eduardocucharro on 29/10/17.
 */

public class StringUtils {
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty() || s.equals("null");
    }

}
