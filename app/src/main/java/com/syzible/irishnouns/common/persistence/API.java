package com.syzible.irishnouns.common.persistence;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class API {
    public static JSONArray domainStore() throws IOException, JSONException {
        return FileLoader.loadFile("domains.json");
    }

    public static JSONArray hintStore() throws IOException, JSONException {
        return FileLoader.loadFile("hints.json");
    }

    public static JSONArray nounStore(String domain) throws IOException, JSONException {
        return FileLoader.loadFile("nouns/" + domain + ".json");
    }
}
