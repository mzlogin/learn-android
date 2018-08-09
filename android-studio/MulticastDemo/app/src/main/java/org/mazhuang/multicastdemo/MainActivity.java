package org.mazhuang.multicastdemo;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lenovo
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.receive_list)
    ListView mReceiveList;
    private SimpleAdapter mAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();

    private List<Map<String, String>> mData = new ArrayList<>();

    private static final String KEY_STR = "str";

    private static int sBroadcastValue = 0;

    private static final String MULTICAST_IP = "224.0.0.251";
    private static final int MULTICAST_PORT = 12345;
    private static final int TTL_TIME = 5;
    private static final int RECEIVE_LENGTH = 1024;

    private WifiManager.MulticastLock mMulticastLock = null;
    private PowerManager.WakeLock mWakeLock = null;

    private Handler mHandler;
    private static final int MSG_SEND = 100;
    private CountDownLatch mHandlerLatch = new CountDownLatch(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initLocks();

        initViews();

        startMulticastThread();

        startReceiveThread();
    }

    private void initLocks() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mMulticastLock = wifiManager.createMulticastLock("multicast.test");

        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
    }

    private void startMulticastThread() {
        new Thread() {
            private MulticastSocket multicastSocket;

            @Override
            public void run() {

                Looper.prepare();

                try {
                    while (mMulticastLock == null || !mMulticastLock.isHeld()) {
                        Thread.sleep(1000);
                        continue;
                    }

                    final InetAddress destAddress = InetAddress.getByName(MULTICAST_IP);
                    if (!destAddress.isMulticastAddress()) {
                        throw new Exception("不是多播地址！");
                    }

                    multicastSocket = new MulticastSocket();
                    multicastSocket.setTimeToLive(TTL_TIME);
                    multicastSocket.setReuseAddress(true);
                    multicastSocket.setLoopbackMode(true);
                    // multicastSocket.joinGroup(destAddress);

                    mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case MSG_SEND: {
                                    String content = DateUtils.getFormattedDatetime() + " " + sBroadcastValue;
                                    byte[] sendMsg = content.getBytes();
                                    DatagramPacket packet = new DatagramPacket(sendMsg, sendMsg.length, destAddress, MULTICAST_PORT);
                                    Log.v(TAG, "multicast " + content);
                                    try {
                                        multicastSocket.send(packet);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                    break;

                                default:
                                    super.handleMessage(msg);
                            }
                        }
                    };

                    mHandlerLatch.countDown();

                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (multicastSocket != null) {
                        multicastSocket.close();
                    }
                }
            }
        }.start();
    }

    private void startReceiveThread() {
        new Thread() {
            @Override
            public void run() {
                MulticastSocket receiveMulticast = null;

                try {

                    while (mMulticastLock == null || !mMulticastLock.isHeld()) {
                        Thread.sleep(1000);
                        continue;
                    }

                    InetAddress receiveAddress = InetAddress.getByName(MULTICAST_IP);
                    if (!receiveAddress.isMulticastAddress()) {
                        throw new Exception("不是多播地址！");
                    }

                    receiveMulticast = new MulticastSocket(MULTICAST_PORT);
                    receiveMulticast.joinGroup(receiveAddress);
                    receiveMulticast.setReuseAddress(true);
                    receiveMulticast.setLoopbackMode(true);

                    DatagramPacket packet = new DatagramPacket(new byte[RECEIVE_LENGTH], RECEIVE_LENGTH);

                    String selfIp = NetworkUtils.getIpAddress();
                    while (true) {
//                        if (!mMulticastLock.isHeld()) {
//                            mMulticastLock.acquire();
//                        }

                        receiveMulticast.receive(packet);
                        if (packet.getAddress().getHostAddress().equals(selfIp)) {
                            Log.v(TAG, "packet from self, ignore");
                            continue;
                        }
                        if (packet.getData() != null) {
                            Log.v(TAG, "receive " + new String(packet.getData()).trim());
                            final Map<String, String> data = new HashMap<>();
                            String content = new String(packet.getData()).trim();
                            String[] splits = content.split(" ");
                            sBroadcastValue = Integer.valueOf(splits[splits.length - 1]) + 1;

                            mHandlerLatch.await();

                            mHandler.sendEmptyMessage(MSG_SEND);

                            data.put(KEY_STR, DateUtils.getFormattedDatetime() + " 收到数据：" + new String(packet.getData()).trim());
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mData.add(data);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (receiveMulticast != null) {
                        receiveMulticast.close();
                    }
                }
            }
        }.start();
    }

    private void initViews() {
        mAdapter = new SimpleAdapter(this,
                mData,
                android.R.layout.simple_list_item_1,
                new String[]{KEY_STR},
                new int[]{android.R.id.text1});
        mReceiveList.setAdapter(mAdapter);
    }

    @OnClick({R.id.broadcast, R.id.clear})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.broadcast:
                sBroadcastValue = 0;
                try {
                    mHandlerLatch.await();
                    mHandler.sendEmptyMessage(MSG_SEND);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.clear:
                mData.clear();
                mAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMulticastLock.acquire();
        mWakeLock.acquire(1000000);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    Log.v(TAG, "multicast lock is held: " + mMulticastLock.isHeld());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMulticastLock.release();
        System.exit(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
    }
}
