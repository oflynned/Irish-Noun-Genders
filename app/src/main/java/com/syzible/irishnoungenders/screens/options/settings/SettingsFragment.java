package com.syzible.irishnoungenders.screens.options.settings;

import android.content.Intent;
import android.net.Uri;
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

        Preference leaveReview = findPreference("settings_leave_review");
        leaveReview.setOnPreferenceClickListener(v -> {
            openAppRating();
            return false;
        });

        Preference moreApps = findPreference("settings_more_apps");
        moreApps.setOnPreferenceClickListener(v -> {
            openDeveloperApps();
            return false;
        });

        SwitchPreferenceCompat nounHints = findPreference("settings_show_hints");
        nounHints.setChecked(GameRules.wordHintsEnabled(getContext()));
        nounHints.setOnPreferenceChangeListener((preference, newValue) -> {
            LocalStorage.setBooleanPref(getContext(), LocalStorage.Pref.SHOW_HINTS, (Boolean) newValue);
            return false;
        });
    }

    private void openAppRating() {
        final String appPackageName = getContext().getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void openDeveloperApps() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Syzible")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Syzible")));
        }
    }
}
