package com.dum.notificationbar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationUtils {
    private static final String CHANNEL_ID = "TEST_CHANNEL_ID";
    private static final CharSequence NOTIFICATION_CHANNEL_NAME = "Test Channel Name";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Test Description";

    private static void checkInit(Context context) {
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (notificationManager != null) {
                NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID);
                if (channel == null) {
                    channel = new NotificationChannel(CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);

                    // Add the channel
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }
    }

    public static NotificationCompat.Builder createBuilder(Context context) {
        checkInit(context);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notification); //need for under api 26

        return builder;
    }

    public static void notify(Context context, int id, NotificationCompat.Builder builder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (notificationManager != null) {
            notificationManager.notify(id, builder.build());;
        }
    }

    public static void close(Context context, int id) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (notificationManager != null) {
            notificationManager.cancel(id);
        }
    }
}
