package org.mazhuang.hookdemo.plugin;

import android.app.Instrumentation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by mazhuang on 2016/11/18.
 */

public class ContextStartActivityHook {

    public static boolean processed = false;

    public static void process() throws Exception {
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);

        Instrumentation fakeInstrumentation = new FakeInstrumentation(mInstrumentation);

        mInstrumentationField.set(currentActivityThread, fakeInstrumentation);

        processed = true;
    }
}
