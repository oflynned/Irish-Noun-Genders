package com.syzible.irishnoungenders.di.components;

import android.content.Context;

import com.syzible.irishnoungenders.di.modules.MainModule;
import com.syzible.irishnoungenders.presentation.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);

    Context context();
}
