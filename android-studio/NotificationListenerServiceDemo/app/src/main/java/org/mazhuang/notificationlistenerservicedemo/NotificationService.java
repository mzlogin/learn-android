package org.mazhuang.notificationlistenerservicedemo;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by mazhuang on 2016/8/9.
 */
public class NotificationService extends NotificationListenerService {
    public static final String MSG_KEY = "msg";
    public static final String STATUS_KEY = "status";
    public static final String MSG_RECEIVER = "org.mazhuang.notificationlistenerservicedemo.MSG_RECEIVER";
    public static final String STATUS_RECEIVER = "org.mazhuang.notificationlistenerservicedemo.STATUS_RECEIVER";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        if (null == notification) {
            return;
        }

        Bundle extras = notification.extras;
        if (null == extras) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("packagename: ");
        String packageName = sbn.getPackageName();
        if (!TextUtils.isEmpty(packageName)) {
            sb.append(packageName);
        }

        sb.append("\ntitle: ");
        String title = extras.getString("android.title");
        if (!TextUtils.isEmpty(title)) {
            sb.append(title);
        }

        sb.append("\ntext: ");
        String text = extras.getString("android.text");
        if (!TextUtils.isEmpty(text)) {
            sb.append(text);
        }

        sb.append("\nsubText: ");
        String subText = extras.getString("android.subText");
        if (!TextUtils.isEmpty(subText)) {
            sb.append(subText);
        }

        sb.append("\ninfoText: ");
        String infoText = extras.getString("android.infoText");
        if (!TextUtils.isEmpty(infoText)) {
            sb.append(infoText);
        }

        Intent intent = new Intent(MSG_RECEIVER);
        intent.putExtra(MSG_KEY, sb.toString());
        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("mzzz", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("mzzz", "onDestroy");
    }

    @Override
    public void onListenerConnected() {
        Log.d("mzzz", "onListenerConnected");
        Intent intent = new Intent(STATUS_RECEIVER);
        intent.putExtra(STATUS_KEY, true);
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("mzzz", "onBind");
        return super.onBind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("mzzz", "onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("mzzz", "onUnbind");
        return super.onUnbind(intent);
    }
}
