package com.syzible.irishnoungenders.data.repository.datasource.mapper;

import android.support.annotation.NonNull;

import com.syzible.irishnoungenders.data.repository.Repository;
import com.syzible.irishnoungenders.data.repository.NounToNounEntityMapper;
import com.syzible.irishnoungenders.data.repository.datasource.DataSource;
import com.syzible.irishnoungenders.data.repository.datasource.NounDataSourceFactory;
import com.syzible.irishnoungenders.domain.model.Noun;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class NounsRepository implements Repository {

    private final DataSource dataSource;
    private final NounToNounEntityMapper mapper;

    @Inject
    NounsRepository(@NonNull NounDataSourceFactory factory, @NonNull NounToNounEntityMapper mapper) {
        this.dataSource = factory.createDataSource();
        this.mapper = mapper;
    }

    @Override
    public Observable<List<Noun>> nounList() {
        return dataSource.nounEntityList().map(mapper::reverseMap);
    }
}
