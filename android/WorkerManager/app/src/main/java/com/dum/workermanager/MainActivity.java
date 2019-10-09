package com.dum.workermanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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
                WorkInfo workInfo = workInfos.get(0);
                if (workInfo.getState().isFinished()) {
                    Log.d(TAG, "Finished");
                    showWorkFinished();
                } else {
                    Log.d(TAG, "Progress");
                    showWorkInProgress();
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
        WorkContinuation continuation = workManager
                .beginUniqueWork(WORKER_NAME,
                        ExistingWorkPolicy.REPLACE,
                        OneTimeWorkRequest.from(FirstWorker.class));

        OneTimeWorkRequest.Builder secondBuilder = new OneTimeWorkRequest.Builder(SecondWorker.class);
        continuation = continuation.then(secondBuilder.build());

        OneTimeWorkRequest.Builder thirdBuilder = new OneTimeWorkRequest.Builder(ThirdWorker.class);
        thirdBuilder.addTag(WORKER_TAG_OUTPUT);
        continuation = continuation.then(thirdBuilder.build());

        continuation.enqueue();
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private void showWorkInProgress() {
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
