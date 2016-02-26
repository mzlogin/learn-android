package org.mazhuang.boottimemeasure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private TextView mBootTimeTextView;
    private TextView mEventsTextView;
    private SwitchCompat mShowOnBoot;
    private static String mBootTimeText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBootTimeTextView = (TextView) findViewById(R.id.boot_time);
        mEventsTextView = (TextView) findViewById(R.id.events);
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

        BootCompletedReceiver.saveBootEvents(this);

        showBootTime();
        showBootEvents();
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

    private void showBootEvents() {
        File events = new File(this.getFilesDir(), "events.log");
        if (events.exists()) {
            try {
                FileInputStream fis = new FileInputStream(events);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                try {
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    mEventsTextView.setText(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
