package com.syzible.irishnoungenders.screens.options.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ShareCompat;
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

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);

        SwitchPreferenceCompat nounHints = findPreference("settings_show_hints");
        if (nounHints != null) {

            nounHints.setChecked(GameRules.isGenderHintEnabled(getContext()));
            nounHints.setOnPreferenceChangeListener((preference, newValue) -> {
                LocalStorage.setBooleanPref(getContext(), LocalStorage.Pref.SHOW_HINTS, !GameRules.isGenderHintEnabled(getContext()));
                nounHints.setChecked(GameRules.isGenderHintEnabled(getContext()));
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

        Preference shareApp = findPreference("settings_share_app");
        if (shareApp != null) {
            shareApp.setOnPreferenceClickListener(v -> {
                openShareAppIntent();
                return false;
            });
        }

        Preference buildVersion = findPreference("settings_build_version");
        if (buildVersion != null) {
            buildVersion.setSummary(getString(R.string.made_in_ireland, BuildConfig.VERSION_NAME));
        }
    }

    private String getPlayStoreUrl() {
        return "http://play.google.com/store/apps/details?id=" + requireActivity().getPackageName();
    }

    private void openAppRating() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getPlayStoreUrl())));
    }

    private void openDeveloperApps() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Syzible")));
    }

    private void openShareAppIntent() {
        ShareCompat.IntentBuilder.from(requireActivity())
                .setType("text/plain")
                .setChooserTitle(getString(R.string.share_app))
                .setText(getString(R.string.share_app_body, getPlayStoreUrl()))
                .startChooser();
    }
}
