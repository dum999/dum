package com.dum.notificationbar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationUtils {
    private static final String CHANNEL_ID_1 = "TEST_CHANNEL_ID_1";
    private static final CharSequence NOTIFICATION_CHANNEL_NAME_1 = "1-Channel Name";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION_1 = "1-Description";

    private static final String CHANNEL_ID_2 = "TEST_CHANNEL_ID_2";
    private static final CharSequence NOTIFICATION_CHANNEL_NAME_2 = "2-Channel Name";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION_2 = "2-Description";

    private static void checkInit(Context context) {
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (notificationManager != null) {
                NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID_1);
                if (channel == null) {
                    channel = new NotificationChannel(CHANNEL_ID_1, NOTIFICATION_CHANNEL_NAME_1, NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION_1);

                    // Add the channel
                    notificationManager.createNotificationChannel(channel);
                }
                channel = notificationManager.getNotificationChannel(CHANNEL_ID_2);
                if (channel == null) {
                    channel = new NotificationChannel(CHANNEL_ID_2, NOTIFICATION_CHANNEL_NAME_2, NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION_2);

                    // Add the channel
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }
    }

    public static NotificationCompat.Builder createBuilder(Context context) {
        checkInit(context);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_1)
                //.setPriority(NotificationCompat.PRIORITY_HIGH)
                //.setVibrate(new long[]{0})
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
