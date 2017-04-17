package org.mazhuang.bluetoothdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SendFileByIntentActivity extends AppCompatActivity {

    private static final int FILE_SELECT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file_by_intent);
        
        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    sendFile(uri);
                } else {
                    Toast.makeText(this, getString(R.string.no_valid_file), Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {
        Button sendButton = (Button) findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        startActivityForResult(
                Intent.createChooser(intent, "选择文件"),
                FILE_SELECT_REQUEST
        );
    }

    private void sendFile(Uri uri) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("plain/text");
        sharingIntent.setComponent(new ComponentName("com.android.bluetooth", "com.android.bluetooth.opp.BluetoothOppLauncherActivity"));
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(sharingIntent);
    }
}
