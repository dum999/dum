package com.dum.workermanager.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ThirdWorker extends Worker {
    private static final String TAG = ThirdWorker.class.getSimpleName();

    public ThirdWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Third start");

        WorkerUtils.sleep();

        Log.d(TAG, "Third end");
        return Result.success();
    }

}
