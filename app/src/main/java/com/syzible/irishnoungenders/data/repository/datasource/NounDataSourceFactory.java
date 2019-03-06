package com.syzible.irishnoungenders.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.syzible.irishnoungenders.data.local.LocalApiImpl;
import com.syzible.irishnoungenders.data.repository.datasource.mapper.NounEntityJsonMapper;

import javax.inject.Inject;

public class NounDataSourceFactory {
    private final Context context;

    @Inject
    public NounDataSourceFactory(@NonNull Context context) {
        this.context = context;
    }

    public DataSource createDataSource() {
        NounEntityJsonMapper nounEntityJsonMapper = new NounEntityJsonMapper();
        LocalApiImpl localImpl = new LocalApiImpl(context, nounEntityJsonMapper);
        return new NounsLocalApiDataSource(localImpl);
    }
}
