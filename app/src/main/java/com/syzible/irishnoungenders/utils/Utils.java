package com.syzible.irishnoungenders.utils;

import android.content.Context;

import com.syzible.irishnoungenders.objects.Noun;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by ed on 15/08/2017.
 */

public class Utils {
    public static JSONArray loadNounList(Context context) {
        try {
            InputStream is = context.getAssets().open("nouns.json");
            int size = is.available();
            System.out.println(size);
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static JSONObject loadNounHints(Context context) {
        try {
            InputStream is = context.getAssets().open("gender_rules.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new JSONObject(new String(buffer, "UTF-8"));
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String getHint(Noun noun, JSONObject hints) {
        try {
            // get first noun if there are spaces
            JSONArray endings = hints.getJSONArray(noun.getGender());
            String groomedNoun = noun.getIrishWord().split(" ")[0];
            for (int i = 0; i < endings.length(); i++) {
                String ending = endings.getString(i);
                if (groomedNoun.endsWith(ending))
                    return ending;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean shouldShowNounHint(Noun noun, JSONObject hints) {
        try {
            // get first noun if there are spaces
            JSONArray endings = hints.getJSONArray(noun.getGender());
            String groomedNoun = noun.getIrishWord().split(" ")[0];
            for (int i = 0; i < endings.length(); i++) {
                String ending = endings.getString(i);
                if (groomedNoun.endsWith(ending))
                    return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Noun getRandomNoun(JSONArray nouns) {
        try {
            int count = nouns.length();
            int randomIndex = new Random().nextInt(count);
            return new Noun(nouns.getJSONObject(randomIndex));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
