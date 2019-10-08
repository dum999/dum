package com.dum.workermanager.workers;

import android.util.Log;

public class WorkerUtils {
    private static final String TAG = WorkerUtils.class.getSimpleName();

    private static final long DELAY_TIME_MILLIS = 3000;

    public static void sleep() {
        try {
            Log.d(TAG, "sleep");
            Thread.sleep(DELAY_TIME_MILLIS, 0);
        } catch (InterruptedException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
