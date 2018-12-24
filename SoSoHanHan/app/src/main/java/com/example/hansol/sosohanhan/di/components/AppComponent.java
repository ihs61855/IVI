package com.example.hansol.sosohanhan.di.components;

import android.app.Application;

import com.example.hansol.sosohanhan.di.App;
import com.example.hansol.sosohanhan.di.modules.ActivityBinder;
import com.example.hansol.sosohanhan.di.modules.AppModule;
import com.example.hansol.sosohanhan.di.modules.SocketModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBinder.class,
        SocketModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication>{

    void inject(App app);

    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
}
