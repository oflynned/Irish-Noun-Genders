package com.syzible.irishnoungenders.common.firebase

import android.content.Context

import com.syzible.irishnoungenders.common.persistence.LocalStorage

object Achievements {
    enum class Achievement {
        FIRST_STEPS {
            override fun toString(): String {
                return "First Steps"
            }
        }
    }

    fun getGuessCount(context: Context): Int {
        return LocalStorage.getIntegerPref(context, LocalStorage.Pref.GUESSES_MADE)
    }

    fun incrementGuessCount(context: Context) {
        val guessCount = LocalStorage.getIntegerPref(context, LocalStorage.Pref.GUESSES_MADE)
        LocalStorage.setIntegerPref(context, LocalStorage.Pref.GUESSES_MADE, guessCount + 1)
    }

    fun hasPassedFirstSteps(context: Context): Boolean {
        return getGuessCount(context) == 5
    }

    fun checkForAchievements() {

    }
}
