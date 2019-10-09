package com.dum.notificationbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = NotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getIntExtra("requestCode", 0);
        Log.d(TAG, "onReceive - " + requestCode);

        int notificationId = intent.getIntExtra("notificationId", 0);
        NotificationUtils.close(context, notificationId);
    }
}
