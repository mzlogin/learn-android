package org.mazhuang.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

public class RunAsClientActivity extends AppCompatActivity {

    private BluetoothAdapter mBtAdapter;

    private BluetoothSocket mBtSocket;

    private ArrayAdapter<String> mArrayAdapter;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_as_client);

        initViews();

        initData();

        initDeviceReceiver();
    }

    private void initDeviceReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mArrayAdapter.add(device.getName() + " | " + device.getAddress());
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    private void initData() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + " | " + device.getAddress());
            }
        }
    }

    private void initViews() {
        final Button refreshDevicesButton = (Button) findViewById(R.id.refresh_devices);
        refreshDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtAdapter.startDiscovery();
                mArrayAdapter.clear();
                refreshDevicesButton.setEnabled(false);
            }
        });

        final ListView devicesList = (ListView) findViewById(R.id.devices_list);
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        devicesList.setAdapter(mArrayAdapter);
        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBtAdapter.cancelDiscovery();
                refreshDevicesButton.setEnabled(true);

                String str = mArrayAdapter.getItem(position);
                String[] values = str.split("\\|");
                String address = values[1];

                BluetoothDevice btDevice = mBtAdapter.getRemoteDevice(address);

                if (mBtSocket == null) {
                    try {
                        mBtSocket = btDevice.createRfcommSocketToServiceRecord(RunAsServerActivity.MY_UUID);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RunAsClientActivity.this, getString(R.string.create_socket_failed), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (!mBtSocket.isConnected()) {
                    try {
                        mBtSocket.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RunAsClientActivity.this, getString(R.string.connect_failed), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                try {
                    OutputStream os = mBtSocket.getOutputStream();
                    os.write("hello from client".getBytes("utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
