package com.syzible.irishnouns.screens.common.domainchoice;

import android.content.Context;

import com.syzible.irishnouns.common.persistence.API;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DomainChoiceInteractor {
    interface DomainCallback {
        void onSuccess(Context context, List<String> domainList);

        void onFailure(String message);
    }

    void fetchDomains(Context context, DomainCallback callback) throws JSONException, IOException {
        List<String> categoryList = new ArrayList<>();
        JSONArray domains = API.domainStore();

        for (int i = 0; i < domains.length(); i++) {
            String domain = domains.getString(i);
            categoryList.add(domain);
        }

        Collections.sort(categoryList);
        callback.onSuccess(context, categoryList);
    }
}
