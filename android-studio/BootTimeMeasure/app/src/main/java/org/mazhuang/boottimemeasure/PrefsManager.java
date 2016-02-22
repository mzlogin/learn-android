package org.mazhuang.boottimemeasure;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

/**
 * Created by mazhuang on 2/22.
 */
public class PrefsManager {
    public static String APP_PREFERENCE = "app_prefs";

    public static String PREFS_KEY_BOOT_TIME = "boot_time";
    public static String PREFS_KEY_SHOW_ON_BOOT = "show_on_boot";

    public static void setBootTime(Context context) {
        long bootTime = SystemClock.elapsedRealtime();
        putLong(context, PREFS_KEY_BOOT_TIME, bootTime);
    }

    public static long getBootTime(Context context, long defValue) {
        return getLong(context, PREFS_KEY_BOOT_TIME, defValue);
    }

    public static void setShowOnBoot(Context context, boolean value) {
        putBoolean(context, PREFS_KEY_SHOW_ON_BOOT, value);
    }

    public static boolean getShowOnBoot(Context context, boolean defValue) {
        return getBoolean(context, PREFS_KEY_SHOW_ON_BOOT, defValue);
    }

    private static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static boolean getBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    private static void putLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private static long getLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getApplicationContext().getSharedPreferences(APP_PREFERENCE,
                Context.MODE_PRIVATE);
    }
}
