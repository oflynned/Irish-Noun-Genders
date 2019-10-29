package com.syzible.irishnoungenders.screens.options.settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.syzible.irishnoungenders.BuildConfig;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.common.persistence.GameRules;
import com.syzible.irishnoungenders.common.persistence.LocalStorage;

public class SettingsFragment extends PreferenceFragmentCompat {
    public SettingsFragment() {
    }

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);

        Preference buildVersion = findPreference("settings_build_version");
        buildVersion.setSummary(getString(R.string.made_in_ireland, BuildConfig.VERSION_NAME));

        SwitchPreferenceCompat nounHints = findPreference("settings_show_hints");
        nounHints.setChecked(GameRules.wordHintsEnabled(getContext()));
        nounHints.setOnPreferenceChangeListener((preference, newValue) -> {
            LocalStorage.setBooleanPref(getContext(), LocalStorage.Pref.SHOW_HINTS, (Boolean) newValue);
            return true;
        });
    }
}
