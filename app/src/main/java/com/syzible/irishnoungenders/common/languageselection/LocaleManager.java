package com.syzible.irishnoungenders.common.languageselection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.preference.PreferenceManager;

import com.syzible.irishnoungenders.common.persistence.LocalStorage;

import java.util.Locale;

import static android.os.Build.VERSION_CODES.N;

public class LocaleManager {

    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_IRISH = "ga";

    private SharedPreferences sharedPreferences;

    public LocaleManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= N) {
            return config.getLocales().get(0);
        } else {
            return config.locale;
        }
    }

    public Context setLocale(Context c) {
        return updateResources(c, getLanguage());
    }

    public Context setNewLocale(Context c, String language) {
        persistLanguage(language);
        return updateResources(c, language);
    }

    public String getLanguage() {
        return sharedPreferences.getString(LocalStorage.Pref.DISPLAY_LANGUAGE.toString(), LANGUAGE_ENGLISH);
    }

    @SuppressLint("ApplySharedPref")
    private void persistLanguage(String language) {
        sharedPreferences.edit()
                .putString(LocalStorage.Pref.DISPLAY_LANGUAGE.toString(), language)
                .commit();
    }

    private Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context;
    }
}
