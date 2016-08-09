package org.mazhuang.notificationlistenerservicedemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mNotificationStatusTextView;
    private Button mNotificationSettingsButton;
    private ListView mNotificationList;
    private NotificationListAdapter mAdapter;
    private NotificationReceiver mReceiver;
    private boolean mListenerEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mListenerEnabled = false;

        mReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NotificationService.MSG_RECEIVER);
        filter.addAction(NotificationService.STATUS_RECEIVER);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mListenerEnabled) {
            restartNotificationService();
        }

        checkNotificationSettings();
    }

    private void restartNotificationService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void initViews() {
        mNotificationStatusTextView = (TextView) findViewById(R.id.notification_status);
        mNotificationSettingsButton = (Button) findViewById(R.id.notification_settings);
        mNotificationSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            }
        });
        mNotificationList = (ListView) findViewById(R.id.notification_list);
        mAdapter = new NotificationListAdapter(this.getApplicationContext());
        mNotificationList.setAdapter(mAdapter);
    }

    private void checkNotificationSettings() {
        boolean enabled = isNotificationEnabled();
        if (enabled) {
            mNotificationSettingsButton.setVisibility(View.INVISIBLE);
            if (!mListenerEnabled) {
                mNotificationStatusTextView.setText(getString(R.string.notification_permission_granted));
            } else {
                mNotificationStatusTextView.setText(getString(R.string.notification_status_well));
            }
        } else {
            mNotificationSettingsButton.setVisibility(View.VISIBLE);
            mNotificationStatusTextView.setText(getString(R.string.notification_permission_denied));
        }
    }

    private boolean isNotificationEnabled() {
        ContentResolver resolver = getContentResolver();
        String enabledListeners = Settings.Secure.getString(resolver, "enabled_notification_listeners");
        if (!TextUtils.isEmpty(enabledListeners)) {
            String pkgName = getPackageName();
            return enabledListeners.contains(pkgName + "/" + pkgName + ".NotificationService");
        } else {
            return false;
        }
    }

    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }

            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }

            String action = intent.getAction();
            if (action.equals(NotificationService.MSG_RECEIVER)) {
                String msg = bundle.getString(NotificationService.MSG_KEY);
                if (msg != null) {
                    mAdapter.addItem(msg);
                }
            } else if (action.equals(NotificationService.STATUS_RECEIVER)) {
                boolean listenerEnabled = bundle.getBoolean(NotificationService.STATUS_KEY);
                if (listenerEnabled) {
                    mListenerEnabled = true;
                    checkNotificationSettings();
                }
            }
        }
    }
}
