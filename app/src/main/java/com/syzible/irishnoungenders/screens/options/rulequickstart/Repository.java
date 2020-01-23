package com.syzible.irishnoungenders.screens.options.rulequickstart;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.syzible.irishnoungenders.common.models.Noun;
import com.syzible.irishnoungenders.common.persistence.API;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    public interface RuleAPI {
        void onResolve(List<Explanation> explanations, Noun.Gender gender);
    }

    private List<Noun> fetchExampleNouns(Noun.Gender gender, String suffix) throws IOException, JSONException {
        JSONArray nounStore = API.nounStore("accountancy");
        List<Noun> nouns = new Gson().fromJson(nounStore.toString(),
                new TypeToken<List<Noun>>() {
                }.getType());

        List<Noun> relevantNouns = new ArrayList<>();

        for (Noun noun : nouns) {
            if (noun.getTitle().endsWith(suffix)) {
                relevantNouns.add(noun);
            }
        }

        return relevantNouns;
    }

    private List<String> castJsonArrayToList(JSONArray array) throws JSONException {
        List<String> hints = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            hints.add(array.getString(i));
        }

        return hints;
    }

    void fetchRules(Context context, Noun.Gender gender, RuleAPI api) throws JSONException, IOException {
        JSONArray rules = API.hintStore().getJSONArray(gender.name().toLowerCase());
        List<String> suffixes = castJsonArrayToList(rules);
        List<Explanation> explanations = new ArrayList<>();

        for (String suffix : suffixes) {
            List<Noun> exampleNouns = fetchExampleNouns(gender, suffix);
            explanations.add(new Explanation(gender, suffix, exampleNouns));
        }

        api.onResolve(explanations, gender);
    }

}
