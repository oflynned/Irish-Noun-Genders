package com.syzible.irishnouns.common.persistence;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

class FileLoader {
    static JSONArray loadFile(String path) throws IOException, JSONException {
        InputStream is = Objects.requireNonNull(FileLoader.class.getClassLoader())
                .getResourceAsStream("assets/" + path);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();
        return new JSONArray(new String(buffer, StandardCharsets.UTF_8));
    }
}
