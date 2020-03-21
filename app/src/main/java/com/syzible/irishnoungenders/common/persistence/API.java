package com.syzible.irishnoungenders.common.persistence;

import com.syzible.irishnoungenders.common.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class API {
    public static JSONArray domainStore() throws IOException, JSONException {
        return FileLoader.loadJSONArrayFile("domains_multilingual.json");
    }

    public static JSONObject hintStore() throws IOException, JSONException {
        return FileLoader.loadJSONObjectFile("gender_rules.json");
    }

    public static JSONArray nounStore(String domain) throws IOException, JSONException {
        return FileLoader.loadJSONArrayFile("nouns/" + Category.domainToFile(domain) + ".json");
    }
}
