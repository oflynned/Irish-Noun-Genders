package com.syzible.irishnoungenders.common.firebase;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseLogger {
    public static void logEvent(Context context, Event event) {
        Bundle bundle = new Bundle();
        bundle.putLong("event_time", System.currentTimeMillis());
        FirebaseAnalytics.getInstance(context).logEvent(event.name().toLowerCase(), bundle);
    }

    public static void logEvent(Context context, Event event, String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        bundle.putLong("event_time", System.currentTimeMillis());
        FirebaseAnalytics.getInstance(context).logEvent(event.name().toLowerCase(), bundle);
    }
}
