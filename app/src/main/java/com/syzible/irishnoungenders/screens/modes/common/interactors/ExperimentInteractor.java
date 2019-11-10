package com.syzible.irishnoungenders.screens.modes.common.interactors;

import android.content.Context;

import com.syzible.irishnoungenders.common.http.Endpoints;
import com.syzible.irishnoungenders.common.http.RestClient;
import com.syzible.irishnoungenders.common.models.Noun;
import com.syzible.irishnoungenders.common.persistence.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ExperimentInteractor {
    public interface ExperimentCallback {
        void onSuccess();

        void onFailure(int status, JSONObject response);
    }

    public void requestExperiment(Context context, JSONObject payload, ExperimentCallback callback) {
        String userId = LocalStorage.getStringPref(context, LocalStorage.Pref.USER_ID);
        try {
            // users do not need to be registered in order to use the game and report experiments
            payload.put("userId", userId == null ? JSONObject.NULL : userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            RestClient.post(context, Endpoints.REPORT_EXPERIMENT, payload, new ExperimentHttpHandler(callback));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // TODO replace with factory or builder pattern when more modes are added
    public JSONObject buildGenderExperimentPayload(String category, Noun noun, Noun.Gender gender) {
        JSONObject object = new JSONObject();
        try {
            object.put("category", category);
            object.put("posedNoun", noun.getTitle());
            object.put("correctAnswer", noun.getGender().name());
            object.put("answerAttempt", gender.name());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
