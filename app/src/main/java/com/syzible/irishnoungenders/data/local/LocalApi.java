package com.syzible.irishnoungenders.data.local;

import com.syzible.irishnoungenders.data.entity.NounEntity;

import java.util.List;

import io.reactivex.Observable;

public interface LocalApi {
    Observable<List<NounEntity>> nounEntityList();
}
