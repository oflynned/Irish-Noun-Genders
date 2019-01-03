package com.syzible.irishnoungenders.Common.Pojo;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ed on 15/08/2017.
 */

public class Noun {
    private enum Gender {
        MASCULINE, FEMININE
    }

    private Gender gender;
    private String irishWord;
    private ArrayList<String> englishTranslation = new ArrayList<>();

    public Noun(@NonNull JSONObject o) throws JSONException {
        this(Gender.valueOf(o.getString("gender")),
                o.getString("ga"),
                o.getJSONArray("en")
        );
    }

    private Noun(@NonNull Gender gender, @NonNull String irishWord, @NonNull JSONArray englishTranslation) throws JSONException {
        this.gender = gender;
        this.irishWord = irishWord;

        for (int i = 0; i < englishTranslation.length(); i++)
            this.englishTranslation.add(englishTranslation.getString(i));
    }

    public String getGender() {
        return gender.name();
    }

    public boolean isMasculine() {
        return gender.equals(Gender.MASCULINE);
    }

    public String getIrishWord() {
        return irishWord;
    }

    public String getEnglishTranslation() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < englishTranslation.size(); i++) {
            output.append(englishTranslation.get(i));
            // second last noun should be the limit for adding commas
            if (i < englishTranslation.size() - 1)
                output.append(", ");
        }

        return output.toString();
    }
}
