package com.example.hansol.sosohanhan.di.modules;

import com.example.hansol.sosohanhan.model.EntertainmentItem;
import com.example.hansol.sosohanhan.adapter.*;

import dagger.Module;
import dagger.Provides;

@Module
public class ListActivityModule {

    @Provides
    EntertainmentItem provideEntertainment(){
        return new EntertainmentItem();
    }
}
