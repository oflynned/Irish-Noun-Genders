package com.syzible.irishnoungenders.common.persistence;

import android.content.Context;

import com.syzible.irishnoungenders.common.models.Category;

public class Cache {
    public static void setNewHighScore(Context context, int value) {
        LocalStorage.setIntegerPref(context, LocalStorage.Pref.HIGH_SCORE, value);
    }

    public static int getHighScore(Context context) {
        return LocalStorage.getIntegerPref(context, LocalStorage.Pref.HIGH_SCORE);
    }

    public static void setLastChosenCategoryFileName(Context context, String filename) {
        LocalStorage.setStringPref(context, LocalStorage.Pref.CURRENT_CATEGORY, filename);
    }

    public static String getLastChosenCategory(Context context) {
        return LocalStorage.getStringPref(context, LocalStorage.Pref.CURRENT_CATEGORY);
    }
}
