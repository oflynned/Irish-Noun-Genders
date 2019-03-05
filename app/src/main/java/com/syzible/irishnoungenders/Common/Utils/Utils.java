package com.syzible.irishnoungenders.Common.Utils;

import android.content.Context;
import android.os.Handler;

import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.Common.Pojo.Noun;

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

    public static void returnToMainFrag(Context context) {
        ((MainActivity) context).getViewPager().setCurrentItem(1);
    }

    public static void delay(final Context context) {
        final Handler handler = new Handler();

        final Runnable returnAction = new Runnable() {
            @Override
            public void run() {
                returnToMainFrag(context);
            }
        };

        Runnable background = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(AnimationsHelper.DURATION_MS);
                    handler.post(returnAction);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(background).start();
    }

    public static JSONArray loadDomainCategories(Context context) {
        try {
            InputStream is = context.getAssets().open("domains.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            return new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String getFileName(String rawName) {
        if (rawName.equals("All Categories"))
            return "*";

        return rawName.toLowerCase().replace(" ", "_").replace("&", "and");
    }

    public static JSONArray loadNounList(Context context, String listName) {
        try {
            InputStream is = context.getAssets().open("nouns/" + getFileName(listName) + ".json");
            byte[] buffer = new byte[is.available()];
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
