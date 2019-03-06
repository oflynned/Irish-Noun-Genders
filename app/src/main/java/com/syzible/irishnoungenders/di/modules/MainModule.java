package com.syzible.irishnoungenders.di.modules;

import android.content.Context;

import com.syzible.irishnoungenders.IrishNounsApplication;
import com.syzible.irishnoungenders.data.repository.Repository;
import com.syzible.irishnoungenders.data.repository.datasource.mapper.NounsRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class MainModule {
    private final IrishNounsApplication application;

    public MainModule(IrishNounsApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    Repository provideRepository(NounsRepository nounsRepository) {
        return nounsRepository;
    }

    @Provides
    @Named("executor_thread")
    Scheduler provideExecutorThread() {
        return Schedulers.io();
    }

    @Provides
    @Named("ui_thread")
    Scheduler provideUiThread() {
        return AndroidSchedulers.mainThread();
    }
}
