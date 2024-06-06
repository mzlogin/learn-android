package org.mazhuang.logbackdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mazhuang
 */
@Slf4j
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log.info("hello world");
        log.info("number {}, boolean {}, string {}, object {}", 1, true, "string", new Object());
        log.debug("debug log test");
    }
}