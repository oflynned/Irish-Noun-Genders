package com.syzible.irishnoungenders.domain.usecase;

import com.syzible.irishnoungenders.data.repository.Repository;
import com.syzible.irishnoungenders.domain.model.Noun;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetNounsByDomain extends UseCase<List<Noun>> {

    private final Repository repository;

    @Inject
    public GetNounsByDomain(
            @Named("executor_thread") Scheduler executorThread,
            @Named("ui_thread") Scheduler uiThread,
            Repository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Observable<List<Noun>> createObservableUseCase() {
        return this.repository.nounList();
    }
}
