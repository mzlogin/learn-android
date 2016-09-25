package org.mazhuang.mibuspass;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    private final String PACKAGE_NAME = "com.miui.tsmclient";
    private final String ACTIVITY_NAME = PACKAGE_NAME + ".ui.quick.DoubleClickActivity";
    private final String ACTION = "com.miui.intent.DOUBLE_CLICK";
    private final int FLAG = 0x10800000;
    private final String EXTRA_KEY_EVENT_SOURCE = "event_source";
    private final String EXTRA_VALUE_EVENT_SOURCE = "key_volume_down";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startMiBuspass();

        finish();
    }

    private void startMiBuspass() {
        ComponentName componentName = new ComponentName(PACKAGE_NAME, ACTIVITY_NAME);

        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.setFlags(FLAG);
        intent.setComponent(componentName);
        intent.putExtra(EXTRA_KEY_EVENT_SOURCE, EXTRA_VALUE_EVENT_SOURCE);

        startActivity(intent);

        finish();
    }
}
