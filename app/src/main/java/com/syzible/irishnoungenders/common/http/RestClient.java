package com.syzible.irishnoungenders.common.http;

import android.content.Context;
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RestClient {
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private static SyncHttpClient syncHttpClient = new SyncHttpClient();

    public static void post(Context context, String url, JSONObject payload, AsyncHttpResponseHandler handler) throws UnsupportedEncodingException {
        getClient().post(context, url, new StringEntity(payload.toString(), ContentType.APPLICATION_JSON), "application/json", handler);
    }

    private static AsyncHttpClient getClient() {
        if (Looper.myLooper() == null) {
            return syncHttpClient;
        }

        return asyncHttpClient;
    }
}
