package com.wuzp.commonlib.Utils;

import android.location.Location;

/**
 * 位置相关的工具类
 *
 * @author wuzhenpeng03
 */
public final class LocationUtils {

    private LocationUtils() {
    }

    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        try {
            float[] results = new float[1];
            Location.distanceBetween(lat1, lng1, lat2, lng2, results);
            return (double) results[0];
        } catch (IllegalArgumentException var9) {
            return 0.0D;
        }
    }

    public static boolean isCoordinateValid(Location location) {
        return location == null ? false : isCoordinateValid(location.getLongitude(), location.getLatitude());
    }

    public static boolean isCoordinateValid(double longitude, double latitude) {
        return Math.abs(longitude - 0.0D) >= 1.0E-6D || Math.abs(latitude - 0.0D) >= 1.0E-6D;
    }
}
