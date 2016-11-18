package org.mazhuang.hookdemo.plugin;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by mazhuang on 2016/11/18.
 */

public class FakeInstrumentation extends Instrumentation {

    private final String TAG = getClass().getSimpleName();

    Instrumentation mOrigin;

    public FakeInstrumentation(Instrumentation origin) {
        mOrigin = origin;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode, Bundle options) {
        Log.d(TAG, "Hook method executed");

        try {
            Method execStartActivityMethod = Instrumentation.class.getDeclaredMethod(
                    "execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class,
                    Intent.class, int.class, Bundle.class);
            execStartActivityMethod.setAccessible(true);
            return (ActivityResult)execStartActivityMethod.invoke(mOrigin, who, contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            throw new RuntimeException("rom don't support");
        }
    }
}
