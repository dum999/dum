package com.dum.workermanager.workers;

import android.util.Log;

public class WorkerUtils {
    private static final String TAG = WorkerUtils.class.getSimpleName();


    public static void sleep(long millis) {
        try {
            Log.d(TAG, "sleep - " + millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
