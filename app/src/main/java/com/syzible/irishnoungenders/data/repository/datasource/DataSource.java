package com.syzible.irishnoungenders.data.repository.datasource;

import com.syzible.irishnoungenders.data.entity.NounEntity;

import java.util.List;

import io.reactivex.Observable;

public interface DataSource {
    Observable<List<NounEntity>> nounEntityList();
}
