package org.mazhuang.authoritydemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private Button mWithoutUserButton;
    private Button mWithUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWithoutUserButton = (Button) findViewById(R.id.without_user_parameter);
        mWithoutUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testAmStartCmd(false);
            }
        });
        mWithUserButton = (Button) findViewById(R.id.with_user_parameter);
        mWithUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testAmStartCmd(true);
            }
        });
    }

    private void testAmStartCmd(boolean withUserParam) {
        String cmd = "am start -n com.android.contacts/.activities.DialtactsActivity";
        if (withUserParam) {
            cmd += " --user " + android.os.Process.myUserHandle().hashCode();
        }

        Process process = null;
        int result = -1;
        BufferedReader errorResult = null;

        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (process == null) {
            return;
        }

        try {
            result = process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (result != 0) {
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            try {
                while ((s = errorResult.readLine()) != null) {
                    Log.d(TAG, s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "出错了，请查看 log", Toast.LENGTH_LONG).show();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
