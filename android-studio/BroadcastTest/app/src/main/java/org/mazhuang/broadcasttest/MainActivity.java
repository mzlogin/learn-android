package org.mazhuang.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver mBroadcastReceiver;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        registerMyReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterMyReceiver();
    }

    private void initViews() {
        ListView list = (ListView) findViewById(R.id.list);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        assert list != null;
        list.setAdapter(mAdapter);
    }

    private void registerMyReceiver() {
        if (mBroadcastReceiver == null) {
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    String msg = context.getString(R.string.item_format, dateFormat.format(calendar.getTime()), intent.getAction());

                    mAdapter.add(msg);
                    mAdapter.notifyDataSetChanged();
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            filter.addAction("android.intent.action.SCREEN_ON");
            filter.addAction("android.intent.action.SCREEN_OFF");
            filter.addAction("android.intent.action.BATTERY_LOW");
            filter.addAction("android.intent.action.BATTERY_OKAY");
            filter.addAction("android.intent.action.BOOT_COMPLETED");
            filter.addAction("android.intent.action.DEVICE_STORAGE_LOW");
            filter.addAction("android.intent.action.DEVICE_STORAGE_OK");
            filter.addAction("android.net.wifi.STATE_CHANGE");
            filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            filter.addAction("android.intent.action.BATTERY_CHANGED");
            filter.addAction("android.intent.action.INPUT_METHOD_CHANGED");
            filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
            filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
            filter.addAction("android.intent.action.DREAMING_STARTED");
            filter.addAction("android.intent.action.DREAMING_STOPPED");
            filter.addAction("android.intent.action.WALLPAPER_CHANGED");
            filter.addAction("android.intent.action.HEADSET_PLUG");
            filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
            filter.addAction("android.intent.action.MEDIA_MOUNTED");
            filter.addAction("android.intent.action.POWER_SAVE_MODE_CHANGED");
            registerReceiver(mBroadcastReceiver, filter);

            filter = new IntentFilter();
            filter.addAction("android.intent.action.PACKAGE_ADDED");
            filter.addAction("android.intent.action.PACKAGE_CHANGED");
            filter.addAction("android.intent.action.PACKAGE_REMOVED");
            filter.addDataScheme("package");
            registerReceiver(mBroadcastReceiver, filter);
        }
    }

    private void unregisterMyReceiver() {
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }
}
