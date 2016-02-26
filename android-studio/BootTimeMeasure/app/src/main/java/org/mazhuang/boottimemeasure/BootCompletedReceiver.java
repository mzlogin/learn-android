package org.mazhuang.boottimemeasure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PrefsManager.setBootTime(context);
        saveBootEvents(context);

        if (PrefsManager.getShowOnBoot(context, true)) {
            Intent i = new Intent();
            i.setClassName("org.mazhuang.boottimemeasure", "org.mazhuang.boottimemeasure.MainActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public static void saveBootEvents(Context context) {
        String output = runShellCmd(context, "logcat -d -b events");
        if (output != null) {
            File events = new File(context.getFilesDir(), "events.log");
            if (events != null) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(events);
                    fos.write(output.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static String runShellCmd(Context context, String cmd) {
        Process process = null;
        int result = -1;
        BufferedReader outputResult = null;
        BufferedReader errorResult = null;
        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (process == null) {
            return output.toString();
        }

        try {
            result = process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (result == 0) {
            outputResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            try {
                while ((s = outputResult.readLine()) != null) {
                    output.append(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            try {
                while ((s = errorResult.readLine()) != null) {
                    error.append(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (errorResult != null) {
            try {
                errorResult.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (process != null) {
            process.destroy();
        }

        return output.toString();
    }
}
