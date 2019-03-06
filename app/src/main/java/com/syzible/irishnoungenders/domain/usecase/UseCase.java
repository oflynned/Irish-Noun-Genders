package com.syzible.irishnoungenders.domain.usecase;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

abstract class UseCase<T> {

    private final CompositeDisposable compositeDisposable;
    private final Scheduler executorScheduler;
    private final Scheduler uiScheduler;

    UseCase(Scheduler executorScheduler, Scheduler uiScheduler) {
        this.executorScheduler = executorScheduler;
        this.uiScheduler = uiScheduler;
        this.compositeDisposable = new CompositeDisposable();
    }

    public void execute(DisposableObserver<T> disposableObserver) {
        if (disposableObserver == null) {
            throw new IllegalArgumentException("disposable observer must not be null");
        }

        final Observable<T> observable = this.createObservableUseCase()
                .subscribeOn(executorScheduler)
                .observeOn(uiScheduler);

        DisposableObserver observer = observable.subscribeWith(disposableObserver);
        compositeDisposable.add(observer);
    }

    protected abstract Observable<T> createObservableUseCase();
}
