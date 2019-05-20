package com.syzible.irishnouns.games.gender;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.syzible.irishnouns.common.models.Noun;
import com.syzible.irishnouns.common.persistence.API;
import com.syzible.irishnouns.common.persistence.DomainNotFoundException;
import com.syzible.irishnouns.common.persistence.MalformedFileException;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class GenderInteractor {
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
