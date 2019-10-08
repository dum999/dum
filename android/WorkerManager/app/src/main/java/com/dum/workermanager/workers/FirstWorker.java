package com.dum.workermanager.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FirstWorker extends Worker {
    private static final String TAG = FirstWorker.class.getSimpleName();

    public FirstWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "First start");

        WorkerUtils.sleep();

        Log.d(TAG, "First end");
        return Result.success();
    }

}
