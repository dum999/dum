package com.dum.notificationbar;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class ThisApplication extends Application implements LifecycleObserver {
    private static final String TAG = ThisApplication.class.getSimpleName();

    private static Context appContext;
    public static boolean wasInBackground = true;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        appContext = this;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public static Context getAppContext() {
        return appContext;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        Log.d(TAG, "onAppForegrounded");
        wasInBackground = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        Log.d(TAG, "onAppBackgrounded");
        wasInBackground = true;
    }

}
