package org.mazhuang.boottimemeasure;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mBootTimeTextView;
    private static boolean mIsMeasured = false;
    private static String mBootTimeText = "";
    private static boolean mIsCallingFrom = false;

    public static String EXTRA_FROM = "from";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBootTimeTextView = (TextView) findViewById(R.id.boot_time);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_FROM)) {
            mIsCallingFrom = true;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (mIsCallingFrom) {
            showBootTime();
        }
    }

    private void showBootTime() {
        if (!mIsMeasured) {
            long bootTime = SystemClock.elapsedRealtime();
            mBootTimeText = "boot time: " + (1.0f * bootTime / 1000) + "s";
            mIsMeasured = true;
        }
        mBootTimeTextView.setText(mBootTimeText);
    }
}
