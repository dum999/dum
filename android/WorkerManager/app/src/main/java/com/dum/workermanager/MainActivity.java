package com.dum.workermanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.dum.workermanager.workers.FirstWorker;
import com.dum.workermanager.workers.SecondWorker;
import com.dum.workermanager.workers.ThirdWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String WORKER_NAME = "test_worker";
    private static final String WORKER_TAG_OUTPUT = "test_output";

    private WorkManager workManager;

    private Button btnGo;
    private Button btnCancel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workManager = WorkManager.getInstance(getApplicationContext());
        workManager.getWorkInfosByTagLiveData(WORKER_TAG_OUTPUT).observe(this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                if (workInfos == null || workInfos.isEmpty()) {
                    return;
                }
                Log.d(TAG, "LiveData - count : " + workInfos.size());

                for (WorkInfo workInfo : workInfos) {
                    switch (workInfo.getState()) {
                        case ENQUEUED:
                            Log.d(TAG, "ENQUEUED");
                            break;
                        case RUNNING:
                            Log.d(TAG, "RUNNING");
                            break;
                        case SUCCEEDED:
                            Log.d(TAG, "SUCCEEDED");
                            break;
                        case FAILED:
                            Log.d(TAG, "FAILED");
                            break;
                        case BLOCKED:
                            Log.d(TAG, "BLOCKED");
                            break;
                        case CANCELLED:
                            Log.d(TAG, "CANCELLED");
                            break;
                    }
                }

                int finishedCount = 0;
                for (WorkInfo workInfo : workInfos) {
                    if (workInfo.getState().isFinished()) {
                        finishedCount++;
                    } else if (workInfo.getState() == WorkInfo.State.RUNNING) {
                        int progress = 0;
                        int max = 0;

                        Data progressData = workInfo.getProgress();
                        if (progressData != null) {
                            progress = progressData.getInt("progress", 0);
                            max = progressData.getInt("max", 0);

                            Log.d(TAG, "Progress - " + progress + "/" + max);
                        }
                        showWorkInProgress(progress, max);
                        break;
                    } else if (workInfo.getState() == WorkInfo.State.ENQUEUED) {
                        Log.d(TAG, "Wait");
                        showWorkInWait();
                    }
                }
                if (finishedCount == workInfos.size()) {
                    Log.d(TAG, "Finished");
                    showWorkFinished();
                }
            }
        });

        btnGo = findViewById(R.id.btn_go);
        btnGo.setOnClickListener(view -> {
            go();
        });

        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(view -> {
            workManager.cancelUniqueWork(WORKER_NAME);
        });

        progressBar = findViewById(R.id.progress_bar);
    }

    private void go() {
        Log.d(TAG, "Go");

        OneTimeWorkRequest.Builder firstBuilder = new OneTimeWorkRequest.Builder(FirstWorker.class)
                .addTag(WORKER_TAG_OUTPUT)
                .setInitialDelay(10, TimeUnit.SECONDS);
        WorkContinuation continuation = workManager.beginUniqueWork(WORKER_NAME,
                ExistingWorkPolicy.REPLACE,
                firstBuilder.build());

        OneTimeWorkRequest.Builder secondBuilder = new OneTimeWorkRequest.Builder(SecondWorker.class)
                .addTag(WORKER_TAG_OUTPUT);
        continuation = continuation.then(secondBuilder.build());

        OneTimeWorkRequest.Builder thirdBuilder = new OneTimeWorkRequest.Builder(ThirdWorker.class)
                .addTag(WORKER_TAG_OUTPUT);
        continuation = continuation.then(thirdBuilder.build());

        continuation.enqueue();
    }

    private void showWorkInWait() {
        progressBar.setVisibility(View.GONE);
        btnCancel.setVisibility(View.VISIBLE);
        btnGo.setVisibility(View.GONE);
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private void showWorkInProgress(int progress, int max) {
        progressBar.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnGo.setVisibility(View.GONE);
    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private void showWorkFinished() {
        progressBar.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        btnGo.setVisibility(View.VISIBLE);
    }

}
