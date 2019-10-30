package com.syzible.irishnoungenders.common.languageselection;

import androidx.annotation.NonNull;

class Language implements Comparable<Language> {
    private String languageName, localisedName, languageCode;

    Language(String languageName, String localisedName, String languageCode) {
        this.languageName = languageName;
        this.localisedName = localisedName;
        this.languageCode = languageCode;
    }

    String getLanguageName() {
        return languageName;
    }

    String getLocalisedName() {
        return localisedName;
    }

    String getLanguageCode() {
        return languageCode;
    }

    @Override
    public int compareTo(@NonNull Language language) {
        return this.localisedName.compareTo(language.localisedName);
    }

    @NonNull
    @Override
    public String toString() {
        return "Language{" +
                "languageName='" + languageName + '\'' +
                ", localisedName='" + localisedName + '\'' +
                ", languageCode='" + languageCode + '\'' +
                '}';
    }
}
