package org.mazhuang.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class RunAsServerActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter mBtAdapter;

    private final String NAME = "MZBLUETOOTH";
    public static final UUID MY_UUID = UUID.fromString("A4736EA8-4DFE-44F7-BF4D-B0178DA5874F");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_as_server);

        checkBluetooth();

        findViewById(R.id.run).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                startServer();
            }
        });
    }

    private void startServer() {
        new AcceptThread().start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_ENABLE_BT:
                if (!mBtAdapter.isEnabled()) {
                    Toast.makeText(this, getString(R.string.cannot_work_without_bluetooth), Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkBluetooth() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(this, getString(R.string.device_unsupport), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!mBtAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }
    }

    private class AcceptThread extends Thread {
        private BluetoothServerSocket mServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBtAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;

            while (true) {
                if (mServerSocket != null) {
                    try {
                        socket = mServerSocket.accept();
                        mServerSocket.close();
                        mServerSocket = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (socket == null) {
                    Toast.makeText(RunAsServerActivity.this, getString(R.string.link_failed), Toast.LENGTH_SHORT).show();
                    break;
                }

                InputStream is;
                try {
                    is = socket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(RunAsServerActivity.this, getString(R.string.link_failed), Toast.LENGTH_SHORT).show();
                    break;
                }

                final byte[] buffer = new byte[128];
                while (true) {
                    try {
                        int count = is.read(buffer);
                        RunAsServerActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RunAsServerActivity.this, new String(buffer), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
