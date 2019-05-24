package com.syzible.irishnoungenders.common.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Noun {
    public enum Gender {
        @SerializedName("masculine")
        MASCULINE,
        @SerializedName("feminine")
        FEMININE
    }

    @SerializedName("declension")
    private int declension;

    @SerializedName("ga")
    private String title;

    @SerializedName("en")
    private List<String> translations;

    @SerializedName("gender")
    private Gender gender;

    public Noun(int declension, String title, List<String> translations, Gender gender) {
        this.declension = declension;
        this.title = title;
        this.translations = translations;
        this.gender = gender;
    }

    public int getDeclension() {
        return declension;
    }

    public String getTitle() {
        return title;
    }

    public String getTranslations() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < translations.size(); i++) {
            builder.append(translations.get(i));
            if (i < translations.size() - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    public Gender getGender() {
        return gender;
    }

    @NonNull
    @Override
    public String toString() {
        return "Noun{" +
                "declension=" + declension +
                ", title='" + title + '\'' +
                ", translations='" + getTranslations() + "\'" +
                ", gender='" + gender.name() + "\'" +
                '}';
    }
}