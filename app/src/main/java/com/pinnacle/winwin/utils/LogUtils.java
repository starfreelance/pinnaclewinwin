package com.pinnacle.winwin.utils;

import android.util.Log;

import com.pinnacle.winwin.BuildConfig;

/* Should be used instead of using the Log class directly
 * Will display log only if the build is debug else not
 */
public class LogUtils {

    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable t) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, t);
        }
    }

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void logException(Exception e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }
}
