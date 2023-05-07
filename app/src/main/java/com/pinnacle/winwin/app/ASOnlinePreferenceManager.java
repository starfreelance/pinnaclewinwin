package com.pinnacle.winwin.app;

import android.content.Context;
import android.content.SharedPreferences;

public class ASOnlinePreferenceManager {

    private static final String PREFERENCE_FILE_NAME = "ASOnlinePreference";

    private static SharedPreferences getASOnlineSharedPreference(Context context) {
        return context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        getASOnlineSharedPreference(context).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getASOnlineSharedPreference(context).getBoolean(key, defaultValue);
    }

    public static void saveInteger(Context context, String key, int value) {
        getASOnlineSharedPreference(context).edit().putInt(key, value).commit();
    }

    public static int getInteger(Context context, String key, int defaultValue) {
        return getASOnlineSharedPreference(context).getInt(key, defaultValue);
    }

    public static void saveString(Context context, String key, String value) {
        getASOnlineSharedPreference(context).edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getASOnlineSharedPreference(context).getString(key, defaultValue);
    }

    public static void clearASOnlinePreferences(Context context) {
        getASOnlineSharedPreference(context).edit().clear().apply();
    }
}
