package org.mazhuang.boottimemeasure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mBootTimeTextView;
    private SwitchCompat mShowOnBoot;
    private static String mBootTimeText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBootTimeTextView = (TextView) findViewById(R.id.boot_time);
        mShowOnBoot = (SwitchCompat) findViewById(R.id.show_on_boot);
        mShowOnBoot.setChecked(PrefsManager.getShowOnBoot(this, true));
        mShowOnBoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefsManager.setShowOnBoot(MainActivity.this, isChecked);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        showBootTime();
    }

    private void showBootTime() {
        long defValue = 0L;
        long bootTime = PrefsManager.getBootTime(this, defValue);
        if (bootTime == defValue) {
            mBootTimeText = "get boot time failed!";
        } else {
            mBootTimeText = "boot time: " + (1.0f * bootTime / 1000) + "s";
        }
        mBootTimeTextView.setText(mBootTimeText);
    }
}
