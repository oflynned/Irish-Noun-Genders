package com.syzible.irishnoungenders.screens.modes.common.interactors;

import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

class ExperimentHttpHandler extends BaseJsonHttpResponseHandler<JSONObject> {
    private ExperimentInteractor.ExperimentCallback callback;

    ExperimentHttpHandler(ExperimentInteractor.ExperimentCallback callback) {
        super();
        this.callback = callback;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, JSONObject response) {
        callback.onSuccess();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, JSONObject errorResponse) {
        callback.onFailure(statusCode, errorResponse);
    }

    @Override
    protected JSONObject parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        return new JSONObject(rawJsonData);
    }
}
