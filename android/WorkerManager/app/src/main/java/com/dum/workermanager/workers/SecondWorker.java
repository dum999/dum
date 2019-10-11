package com.dum.workermanager.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SecondWorker extends Worker {
    private static final String TAG = SecondWorker.class.getSimpleName();

    public SecondWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Second start");

        WorkerUtils.sleep(3000);

        Log.d(TAG, "Second end");
        return Result.success();
        //return Result.retry();
    }

}
