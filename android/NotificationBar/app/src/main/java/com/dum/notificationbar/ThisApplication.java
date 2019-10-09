package com.dum.notificationbar;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class ThisApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
