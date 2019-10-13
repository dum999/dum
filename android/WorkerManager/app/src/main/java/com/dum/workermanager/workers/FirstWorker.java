package com.dum.workermanager.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
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

        Data.Builder builder = new Data.Builder()
                .putInt("max", 160);

        setProgressAsync(builder.putInt("progress", 0).build());
        WorkerUtils.sleep(60000);
        setProgressAsync(builder.putInt("progress", 10).build());
        WorkerUtils.sleep(60000);
        setProgressAsync(builder.putInt("progress", 50).build());
        WorkerUtils.sleep(60000);
        setProgressAsync(builder.putInt("progress", 120).build());
        WorkerUtils.sleep(4 * 60000);
        setProgressAsync(builder.putInt("progress", 160).build());

        Log.d(TAG, "First end");
        return Result.success();
    }

}
