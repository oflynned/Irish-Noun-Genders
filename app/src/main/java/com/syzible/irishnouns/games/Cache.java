package com.syzible.irishnouns.games;

import android.content.Context;

import com.syzible.irishnouns.common.persistence.DomainNotFoundException;
import com.syzible.irishnouns.common.persistence.LocalStorage;

public class Cache {
    public static void setNewHighScore(Context context, int value) {
        LocalStorage.setIntegerPref(context, LocalStorage.Pref.HIGH_SCORE, value);
    }

    public static int getHighScore(Context context) {
        return LocalStorage.getIntegerPref(context, LocalStorage.Pref.HIGH_SCORE);
    }

    public static void setLastChosenCategory(Context context, String category) {
        LocalStorage.setStringPref(context, LocalStorage.Pref.LAST_CHOSEN_CATEGORY, category);
    }

    public static String getLastChosenCategory(Context context) throws DomainNotFoundException {
        String lastChosenCategory = LocalStorage.getStringPref(context, LocalStorage.Pref.LAST_CHOSEN_CATEGORY);
        if (lastChosenCategory == null) {
            throw new DomainNotFoundException();
        }

        return lastChosenCategory;
    }
}
