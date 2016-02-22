package org.mazhuang.boottimemeasure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PrefsManager.setBootTime(context);

        if (PrefsManager.getShowOnBoot(context, true)) {
            Intent i = new Intent();
            i.setClassName("org.mazhuang.boottimemeasure", "org.mazhuang.boottimemeasure.MainActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
