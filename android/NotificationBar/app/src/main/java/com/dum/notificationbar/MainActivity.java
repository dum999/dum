package com.dum.notificationbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int NOTIFICATION_ID = 1;

    private NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder = NotificationUtils.createBuilder(getBaseContext());
        builder.setContentTitle("Title");
        builder.setContentText("Message");
        //builder.setStyle(new NotificationCompat.MessagingStyle("Me"));

        //builder.setAutoCancel(true);
        //builder.setTimeoutAfter(0);

        Button btnShow = findViewById(R.id.btn_show);
        btnShow.setOnClickListener(view -> {
            builder.setProgress(0, 0, false)
                    .setOngoing(true)
                    .setContentText("Message");
            NotificationUtils.notify(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnAction = findViewById(R.id.btn_action);
        btnAction.setOnClickListener(view -> {
            if (builder.mActions.isEmpty()) {
                Intent buttonIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                buttonIntent.putExtra("notificationId", NOTIFICATION_ID);
                PendingIntent actionIntent = PendingIntent.getBroadcast(getBaseContext(), 0, buttonIntent, 0);

                builder.addAction(android.R.drawable.ic_delete, "Cancel", actionIntent);
            }

            NotificationUtils.update(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnPreprogress = findViewById(R.id.btn_preprogress);
        btnPreprogress.setOnClickListener(view -> {
            builder.setProgress(0, 0, true)
                    .setOngoing(true)
                    .setContentText(null);
            NotificationUtils.update(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnProgress = findViewById(R.id.btn_progress);
        btnProgress.setOnClickListener(view -> {
            builder.setProgress(100, 50, false)
                    .setOngoing(true)
                    .setContentText("50/100");
            NotificationUtils.update(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnSuccess = findViewById(R.id.btn_success);
        btnSuccess.setOnClickListener(view -> {
            builder.setProgress(0, 0, false)
                    .setOngoing(false)
                    .setContentText("Success");
            NotificationUtils.update(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> {
            NotificationUtils.close(getBaseContext(), NOTIFICATION_ID);
        });
    }

}
