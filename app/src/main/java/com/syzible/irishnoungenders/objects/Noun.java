package com.syzible.irishnoungenders.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ed on 15/08/2017.
 */

public class Noun {
    private enum Gender {
        masculine, feminine
    }

    private Gender gender;
    private String irishWord;
    private ArrayList<String> englishTranslation = new ArrayList<>();
    private ArrayList<String> domains = new ArrayList<>();

    public Noun(JSONObject o) throws JSONException {
        this(Gender.valueOf(o.getString("gender")), o.getString("ga"), o.getJSONArray("en"));
    }

    private Noun(Gender gender, String irishWord, JSONArray englishTranslation) throws JSONException {
        this.gender = gender;
        this.irishWord = irishWord;

        for (int i = 0; i < englishTranslation.length(); i++)
            this.englishTranslation.add(englishTranslation.getString(i));
    }

    public String getGender() {
        return gender.name();
    }

    public boolean isMasculine() {
        return gender.equals(Gender.masculine);
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
