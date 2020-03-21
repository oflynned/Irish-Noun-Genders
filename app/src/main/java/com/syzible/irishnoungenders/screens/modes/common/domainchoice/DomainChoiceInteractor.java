package com.syzible.irishnoungenders.screens.modes.common.domainchoice;

import android.content.Context;

import com.syzible.irishnoungenders.common.models.Domain;
import com.syzible.irishnoungenders.common.persistence.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DomainChoiceInteractor {
    public interface DomainCallback {
        void onSuccess(Context context, List<Domain> domainList);

        void onFailure(String message);
    }

    public void fetchDomains(String locale, Context context, DomainCallback callback) throws JSONException, IOException {
        List<Domain> categoryList = new ArrayList<>();
        JSONArray domains = API.domainStore();

        for (int i = 0; i < domains.length(); i++) {
            JSONObject data = domains.getJSONObject(i);
            String filename = data.getString("filename");
            String en = data.getString("en");
            String ga = data.getString("ga");

            categoryList.add(new Domain(filename, en, ga, locale));
        }

        Collections.sort(categoryList);
        callback.onSuccess(context, categoryList);
    }
}
