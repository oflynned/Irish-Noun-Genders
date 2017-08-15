package com.syzible.irishnoungenders;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by ed on 15/08/2017.
 */

public class LocalStorage {
    public static int getHighScore(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("high_score", 0);
    }

    public static void setHighScore(Context context, int score) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("high_score", score).apply();
    }
}
