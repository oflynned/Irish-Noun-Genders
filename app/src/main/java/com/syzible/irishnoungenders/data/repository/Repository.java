package com.syzible.irishnoungenders.data.repository;

import com.syzible.irishnoungenders.domain.model.Noun;

import java.util.List;

import io.reactivex.Observable;

public interface Repository {
    Observable<List<Noun>> nounList();
}
