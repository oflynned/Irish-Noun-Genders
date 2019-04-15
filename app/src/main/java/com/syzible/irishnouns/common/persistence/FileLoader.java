package com.syzible.irishnouns.common.persistence;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

class FileLoader {
    static JSONArray loadFile(String path) throws IOException, JSONException {
        InputStream is = FileLoader.class.getClassLoader().getResourceAsStream("assets/" + path);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();
        return new JSONArray(new String(buffer, "UTF-8"));
    }
}
