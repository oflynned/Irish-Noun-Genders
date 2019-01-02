package com.syzible.irishnoungenders.utils;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.syzible.irishnoungenders.R;

/**
 * Created by ed on 15/08/2017.
 */

public class AnimationsHelper {
    private static final float SCALING_FACTOR = 1.5f;
    private static final int REPEAT_COUNT = 5;
    private static final int DURATION_MS = 500;

    public static void shakeAnimation(View view) {
        Animation shake = AnimationUtils.loadAnimation(view.getContext(), R.anim.shake);
        view.startAnimation(shake);
    }

    public static void pulseAnimation(View view) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", SCALING_FACTOR),
                PropertyValuesHolder.ofFloat("scaleY", SCALING_FACTOR));
        scaleDown.setDuration(DURATION_MS);

        scaleDown.setRepeatCount(REPEAT_COUNT);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
    }
}
