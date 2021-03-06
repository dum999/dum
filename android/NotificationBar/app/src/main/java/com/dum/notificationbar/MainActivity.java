package com.dum.notificationbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
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

        Button btnShowTop = findViewById(R.id.btn_show_top);
        btnShowTop.setOnClickListener(view -> {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationUtils.notify(getBaseContext(), NOTIFICATION_ID, builder);
        });
        Button btnShowBackground = findViewById(R.id.btn_show_background);
        btnShowBackground.setOnClickListener(view -> {
            builder.setPriority(NotificationCompat.PRIORITY_LOW);
            NotificationUtils.notify(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnOngoinOn = findViewById(R.id.btn_ongoing_on);
        btnOngoinOn.setOnClickListener(view -> {
            builder.setOngoing(true);
            NotificationUtils.notify(getBaseContext(), NOTIFICATION_ID, builder);
        });
        Button btnOngoinOff = findViewById(R.id.btn_ongoing_off);
        btnOngoinOff.setOnClickListener(view -> {
            builder.setOngoing(false);
            NotificationUtils.notify(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnAddAction = findViewById(R.id.btn_add_action);
        btnAddAction.setOnClickListener(view -> {
            if (builder.mActions.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                PendingIntent googleIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
                builder.addAction(android.R.drawable.ic_menu_view, "Google", googleIntent);

                Intent buttonIntent = new Intent(getBaseContext(), NotificationReceiver.class);
                buttonIntent.putExtra("notificationId", NOTIFICATION_ID);

                buttonIntent.putExtra("requestCode", 1);
                PendingIntent pauseIntent = PendingIntent.getBroadcast(getBaseContext(), 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.addAction(android.R.drawable.ic_menu_add, "Pause", pauseIntent);

                buttonIntent.putExtra("requestCode", 2);
                PendingIntent stopIntent = PendingIntent.getBroadcast(getBaseContext(), 2, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.addAction(android.R.drawable.ic_menu_call, "Stop", stopIntent);
            }

            NotificationUtils.notify(getBaseContext(), NOTIFICATION_ID, builder);
        });
        Button btnClearAction = findViewById(R.id.btn_clear_action);
        btnClearAction.setOnClickListener(view -> {
            builder.mActions.clear();
            NotificationUtils.notify(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnDismissedCallback = findViewById(R.id.btn_dismissed_callback);
        btnDismissedCallback.setOnClickListener(view -> {
            Intent dismissedIntent = new Intent(getBaseContext(), NotificationDismissedReceiver.class);
            dismissedIntent.putExtra("notificationId", NOTIFICATION_ID);
            PendingIntent deleteIntent = PendingIntent.getBroadcast(getBaseContext(), 0, dismissedIntent, 0);
            builder.setDeleteIntent(deleteIntent);
            NotificationUtils.notify(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnPreprogress = findViewById(R.id.btn_preprogress);
        btnPreprogress.setOnClickListener(view -> {
            builder.setProgress(0, 0, true)
                    .setOngoing(true)
                    .setContentText(null);
            NotificationUtils.notify(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnProgress = findViewById(R.id.btn_progress);
        btnProgress.setOnClickListener(view -> {
            builder.setProgress(100, 50, false)
                    .setOngoing(true)
                    .setContentText("50/100");
            NotificationUtils.notify(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnSuccess = findViewById(R.id.btn_success);
        btnSuccess.setOnClickListener(view -> {
            builder.mActions.clear();
            builder.setProgress(0, 0, false)
                    .setOngoing(false)
                    .setAutoCancel(true)
                    .setContentText("Success");
            NotificationUtils.notify(getBaseContext(), NOTIFICATION_ID, builder);
        });

        Button btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> {
            NotificationUtils.close(getBaseContext(), NOTIFICATION_ID);
        });
    }

}
