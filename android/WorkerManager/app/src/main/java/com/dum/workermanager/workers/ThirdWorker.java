package com.dum.workermanager.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.dum.workermanager.ThisApplication;

public class ThirdWorker extends Worker {
    private static final String TAG = ThirdWorker.class.getSimpleName();

    public ThirdWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Third start");
        Log.d(TAG, "Background - " + ThisApplication.wasInBackground);

        WorkerUtils.sleep(3 * 60000);

        Log.d(TAG, "Third end");
        return Result.success();
    }

}
