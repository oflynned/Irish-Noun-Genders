package com.syzible.irishnoungenders.utils;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ed on 15/08/2017.
 */

public class PulseAnimation {
    private static final int REPEAT_COUNT = 5;

    public static void animateView(View view) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 1.5f),
                PropertyValuesHolder.ofFloat("scaleY", 1.5f));
        scaleDown.setDuration(500);

        scaleDown.setRepeatCount(REPEAT_COUNT);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
    }
}
