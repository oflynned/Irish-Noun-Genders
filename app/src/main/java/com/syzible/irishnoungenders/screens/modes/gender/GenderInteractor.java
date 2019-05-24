package com.syzible.irishnoungenders.screens.modes.gender;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.syzible.irishnoungenders.common.models.Noun;
import com.syzible.irishnoungenders.common.persistence.API;
import com.syzible.irishnoungenders.common.persistence.DomainNotFoundException;
import com.syzible.irishnoungenders.common.persistence.MalformedFileException;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class GenderInteractor {
    List<String> fetchHints(Noun.Gender gender) throws MalformedFileException {
        JSONArray hints;
        try {
            hints = API.hintStore().getJSONArray(gender.name().toLowerCase(Locale.US));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            throw new MalformedFileException();
        }

        List<String> hintsList = new ArrayList<>();
        for (int i = 0; i < hints.length(); i++) {
            try {
                hintsList.add(hints.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return hintsList;
    }

    List<String> fetchDomains() throws MalformedFileException {
        JSONArray domains;
        try {
            domains = API.domainStore();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new MalformedFileException();
        }

        List<String> domainList = new ArrayList<>();
        for (int i = 0; i < domains.length(); i++) {
            try {
                String domain = domains.getString(i);
                domainList.add(domain);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return domainList;
    }

    List<Noun> fetchNouns(String domain) throws DomainNotFoundException, MalformedFileException {
        JSONArray response;
        try {
            response = API.nounStore(domain);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DomainNotFoundException();
        } catch (JSONException e) {
            e.printStackTrace();
            throw new MalformedFileException();
        }

        return new Gson().fromJson(response.toString(),
                new TypeToken<List<Noun>>() {
                }.getType());
    }
}
