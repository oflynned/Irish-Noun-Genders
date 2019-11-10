package com.syzible.irishnoungenders.common.firebase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.syzible.irishnoungenders.common.persistence.LocalStorage;

public class Achievements {
    public enum Achievement {
        FIRST_STEPS {
            @NonNull
            @Override
            public String toString() {
                return "First Steps";
            }
        }
    }

    private static int getGuessCount(Context context) {
        return LocalStorage.getIntegerPref(context, LocalStorage.Pref.GUESSES_MADE);
    }

    public static void incrementGuessCount(Context context) {
        int guessCount = LocalStorage.getIntegerPref(context, LocalStorage.Pref.GUESSES_MADE);
        LocalStorage.setIntegerPref(context, LocalStorage.Pref.GUESSES_MADE, guessCount + 1);
    }

    public static boolean hasPassedFirstSteps(Context context) {
        return getGuessCount(context) == 5;
    }

    public static void checkForAchievements() {

    }
}
