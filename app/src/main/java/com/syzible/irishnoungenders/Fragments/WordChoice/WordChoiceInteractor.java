package com.syzible.irishnoungenders.Fragments.WordChoice;

import android.content.Context;

import com.syzible.irishnoungenders.Common.Utils.LocalStorage;

public class WordChoiceInteractor {
    public interface Callback {
        void onSuccess(Context context);

        void onFailure();
    }

    public int fetchHighScore(Context context) {
        return LocalStorage.getHighScore(context);
    }

    public void trackHighScore(Context context, int highScore) {
        LocalStorage.setHighScore(context, highScore);
    }
}
