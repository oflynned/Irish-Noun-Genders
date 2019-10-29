package com.syzible.irishnoungenders.common.persistence;

import android.content.Context;

public class GameRules {
    public static boolean wordHintsEnabled(Context context) {
        return LocalStorage.getBooleanPref(context, LocalStorage.Pref.SHOW_HINTS);
    }
}
