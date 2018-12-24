package com.example.hansol.sosohanhan.di.modules;


import com.example.hansol.sosohanhan.utility.SocketUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SocketModule {
    SocketUtils socketUtils = new SocketUtils();

    @Provides
    @Singleton
    public SocketUtils provideSocketUtils(){
        return socketUtils;
    }
}
