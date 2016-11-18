package org.mazhuang.hookdemo.plugin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.mazhuang.hookdemo.R;

public class ContextHookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_hook);
    }
}
