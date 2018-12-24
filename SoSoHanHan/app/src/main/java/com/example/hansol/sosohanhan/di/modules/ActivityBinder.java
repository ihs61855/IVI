package com.example.hansol.sosohanhan.di.modules;

import com.example.hansol.sosohanhan.view.ListActivity;
import com.example.hansol.sosohanhan.view.MainActivity;
import com.example.hansol.sosohanhan.view.PopupActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBinder {

    //그래프 추가
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = ListActivityModule.class)
    abstract ListActivity bindListActivity();

    @ContributesAndroidInjector(modules = PopupActivityModule.class)
    abstract PopupActivity bindPopupActivity();
}
