package com.syzible.irishnoungenders.Fragments.WordChoice;

import android.content.Context;

import com.syzible.irishnoungenders.Common.Exceptions.JsonFileExistenceException;
import com.syzible.irishnoungenders.Common.Pojo.Noun;
import com.syzible.irishnoungenders.Common.Utils.LocalStorage;
import com.syzible.irishnoungenders.Common.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WordChoiceInteractor {
    public interface NounCallback {
        void onHintsLoaded(List<String> masculineHints, List<String> feminineHints);

        void onDomainLoaded(List<Noun> nouns, String domain);

        void onNounLoaded(Noun noun, String domain);

        void onFailure();
    }

    public int fetchHighScore(Context context) {
        return LocalStorage.getHighScore(context);
    }

    public void trackHighScore(Context context, int highScore) {
        LocalStorage.setHighScore(context, highScore);
    }

    public void fetchNounsFromDomain(Context context, String domain, NounCallback callback) {
        JSONArray nounList = null;
        try {
            nounList = Utils.loadNounList(context, domain);
        } catch (JsonFileExistenceException e) {
            e.printStackTrace();
            callback.onFailure();
        }

        List<Noun> nouns = new ArrayList<>();
        try {
            for (int i = 0; i < Objects.requireNonNull(nounList).length(); i++) {
                nouns.add(new Noun(nounList.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure();
        }

        callback.onDomainLoaded(nouns, domain);
    }

    private String[] retrieveHints(JSONObject hints, String gender) throws JSONException {
        
        JSONArray masculineHints = hints.getJSONArray(gender);
        for (int i=0;i<masculineHints.length();i++) {

        }
    }

    public void fetchHints(Context context, Noun.Gender gender, NounCallback callback) {
        JSONObject hints;
        try {
            hints = Utils.loadNounHints(context);
        } catch (JsonFileExistenceException e) {
            e.printStackTrace();
            callback.onFailure();
            return;
        }

        List<String> wordHints = new ArrayList<>();
        try {
            JSONArray masculineHints = hints.getJSONArray("masculine");
            for (int i=0;i<masculineHints.length();i++) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callback.onHintsLoaded(masculineHints, feminineHints);
    }

    public void fetchDomainNames() {

    }
}
