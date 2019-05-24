package com.syzible.irishnoungenders.common.persistence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

class FileLoader {
    private static String loadFile(String path) throws IOException {
        InputStream is = Objects.requireNonNull(FileLoader.class.getClassLoader())
                .getResourceAsStream("assets/" + path);
        InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader, 8);
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    static JSONArray loadJSONArrayFile(String path) throws IOException, JSONException {
        String contents = loadFile(path);
        return new JSONArray(contents);
    }

    static JSONObject loadJSONObjectFile(String path) throws IOException, JSONException {
        String contents = loadFile(path);
        return new JSONObject(contents);
    }
}
