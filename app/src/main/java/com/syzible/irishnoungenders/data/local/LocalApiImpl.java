package com.syzible.irishnoungenders.data.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.syzible.irishnoungenders.data.entity.NounEntity;
import com.syzible.irishnoungenders.data.repository.datasource.mapper.NounEntityJsonMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.reactivex.Observable;

public class LocalApiImpl implements LocalApi {

    private final Context context;
    private final NounEntityJsonMapper nounEntityJsonMapper;

    public LocalApiImpl(@NonNull Context context, @NonNull NounEntityJsonMapper nounEntityJsonMapper) {
        this.context = context;
        this.nounEntityJsonMapper = nounEntityJsonMapper;
    }

    @Override
    public Observable<List<NounEntity>> nounEntityList() {
        return Observable.create(emitter -> {
            List<NounEntity> nounEntityList = getAll();
            if (nounEntityList != null) {
                emitter.onNext(nounEntityList);
                emitter.onComplete();
            } else {
                emitter.onError(
                        // TODO add noun list as type param
                        new Throwable("noun list could not be loaded!")
                );
            }
        });
    }

    private List<NounEntity> getAll() {
        return nounEntityJsonMapper.transformNounEntity(getResponseFromLocalJson());
    }

    private String getResponseFromLocalJson() {
        // TODO allow domain to be injected
        final String JSON_FILE_NAME = "nouns/accounting.json";
        String response = "";
        try {
            StringBuilder builder = new StringBuilder();
            InputStream json = context.getAssets().open(JSON_FILE_NAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(json));

            while((response = in.readLine()) != null) {
                builder.append(response);
            }
            in.close();
            response = builder.toString();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
