package com.syzible.irishnoungenders;

import android.app.Application;

import com.syzible.irishnoungenders.di.components.DaggerMainComponent;
import com.syzible.irishnoungenders.di.components.MainComponent;
import com.syzible.irishnoungenders.di.modules.MainModule;

public class IrishNounsApplication extends Application {
    private MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initialiseInjector();
    }

    private void initialiseInjector() {
        MainModule mainModule = new MainModule(this);
        mainComponent = DaggerMainComponent.builder()
                .mainModule(mainModule)
                .build();
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
