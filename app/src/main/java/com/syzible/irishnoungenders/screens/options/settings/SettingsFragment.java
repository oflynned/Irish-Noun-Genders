package com.syzible.irishnoungenders.screens.options.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.syzible.irishnoungenders.App;
import com.syzible.irishnoungenders.BuildConfig;
import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.common.languageselection.LocaleManager;
import com.syzible.irishnoungenders.common.persistence.GameRules;
import com.syzible.irishnoungenders.common.persistence.LocalStorage;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {
    public SettingsFragment() {
    }

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);

        SwitchPreferenceCompat nounHints = findPreference("settings_show_hints");
        if (nounHints != null) {
            nounHints.setChecked(GameRules.wordHintsEnabled(getContext()));
            nounHints.setOnPreferenceChangeListener((preference, newValue) -> {
                LocalStorage.setBooleanPref(getContext(), LocalStorage.Pref.SHOW_HINTS, (Boolean) newValue);
                return false;
            });
        }

        SwitchPreferenceCompat irishLanguageEnabled = findPreference("settings_enable_irish_language");
        if (irishLanguageEnabled != null) {
            boolean enableIrishLanguage = LocalStorage.getStringPref(getContext(), LocalStorage.Pref.DISPLAY_LANGUAGE)
                    .equals(LocaleManager.LANGUAGE_IRISH);
            irishLanguageEnabled.setChecked(enableIrishLanguage);

            irishLanguageEnabled.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean forced = (boolean) newValue;
                setNewLocale(forced ? LocaleManager.LANGUAGE_IRISH : LocaleManager.LANGUAGE_ENGLISH, true);
                return false;
            });
        }

        Preference leaveReview = findPreference("settings_leave_review");
        if (leaveReview != null) {
            leaveReview.setOnPreferenceClickListener(v -> {
                openAppRating();
                return false;
            });
        }

        Preference moreApps = findPreference("settings_more_apps");
        if (moreApps != null) {
            moreApps.setOnPreferenceClickListener(v -> {
                openDeveloperApps();
                return false;
            });
        }

        Preference buildVersion = findPreference("settings_build_version");
        if (buildVersion != null) {
            buildVersion.setSummary(getString(R.string.made_in_ireland, BuildConfig.VERSION_NAME));
        }
    }

    private void setNewLocale(String language, boolean restartProcess) {
        App.localeManager.setNewLocale(getActivity(), language);

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

        if (restartProcess) {
            System.exit(0);
        }
    }

    private void openAppRating() {
        final String appPackageName = Objects.requireNonNull(getContext()).getPackageName();
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
