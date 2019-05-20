package com.syzible.irishnouns.common.persistence;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

class FileLoader {
    static JSONArray loadFile(String path) throws IOException, JSONException {
        InputStream is = Objects.requireNonNull(FileLoader.class.getClassLoader())
                .getResourceAsStream("assets/" + path);
        InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader, 8);
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        return new JSONArray(builder.toString());
    }
}
