package com.syzible.irishnouns.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.syzible.irishnouns.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    public SettingsFragment() {
    }

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings_preferences);
    }
}
