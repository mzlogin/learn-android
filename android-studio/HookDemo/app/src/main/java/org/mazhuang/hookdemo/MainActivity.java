package org.mazhuang.hookdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.mazhuang.hookdemo.plugin.ContextHookActivity;
import org.mazhuang.hookdemo.plugin.ContextStartActivityHook;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mContextHookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.context_hook).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.context_hook:
                try {
                    if (!ContextStartActivityHook.processed) {
                        ContextStartActivityHook.process();
                    }
                    Context context = this.getApplicationContext();
                    Intent intent = new Intent(context, ContextHookActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
