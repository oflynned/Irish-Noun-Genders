package com.syzible.irishnoungenders.common.persistence;

import android.content.Context;
import android.preference.PreferenceManager;

public class LocalStorage {
    public enum Pref {
        HIGH_SCORE,
        CURRENT_CATEGORY,
        SHOW_HINTS,
        FIRST_RUN_COMPLETE
    }

    public static String getStringPref(Context context, Pref pref) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(pref.name(), null);
    }

    public static void setStringPref(Context context, Pref pref, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(pref.name(), value).apply();
    }

    public static int getIntegerPref(Context context, Pref pref) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(pref.name(), 0);
    }

    public static void setIntegerPref(Context context, Pref pref, int value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putInt(pref.name(), value).apply();
    }

    public static boolean getBooleanPref(Context context, Pref pref) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(pref.name(), false);
    }

    public static void setBooleanPref(Context context, Pref pref, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putBoolean(pref.name(), value).apply();
    }
}
