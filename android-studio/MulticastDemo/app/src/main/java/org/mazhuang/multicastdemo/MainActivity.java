package org.mazhuang.multicastdemo;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static String sContent1111 = "1111";
    private static String sContent2222 = "2222";
    private static String sContent3333 = "3333";

    private static String sContent = sContent1111;

    private static final String MULTICAST_IP = "224.0.0.251";
    private static final int MULTICAST_PORT = 12345;
    private static final int TTL_TIME = 5;
    private static final int RECEIVE_LENGTH = 1024;

    private static String sBroadcastIp = null;
    private static final int BROADCAST_PORT = 12345;

    private WifiManager.MulticastLock mMulticastLock = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        try {
            sBroadcastIp = getBroadcastAddress(this).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        initViews();

        startMulticastThread();

        startReceiveThread();

//        startBroadcastThread();
//        startBroadcastReceiveThread();
    }

    private void startMulticastThread() {
        new Thread() {
            @Override
            public void run() {

                MulticastSocket multicastSocket = null;

                try {

                    while (mMulticastLock == null || !mMulticastLock.isHeld()) {
                        Thread.sleep(1000);
                        continue;
                    }

                    InetAddress destAddress = InetAddress.getByName(MULTICAST_IP);
                    if (!destAddress.isMulticastAddress()) {
                        throw new Exception("不是多播地址！");
                    }

                    multicastSocket = new MulticastSocket();
                    multicastSocket.setTimeToLive(TTL_TIME);
                    multicastSocket.setReuseAddress(true);
                    // multicastSocket.joinGroup(destAddress);

                    while (true) {
                        Thread.sleep(1000);
                        byte[] sendMsg = sContent.getBytes();
                        DatagramPacket packet = new DatagramPacket(sendMsg, sendMsg.length, destAddress, MULTICAST_PORT);
                        multicastSocket.send(packet);
                        Log.v(TAG, "multicast " + sContent);
                    }
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
                        receiveMulticast.receive(packet);
                        if (packet.getAddress().getHostAddress().equals(selfIp)) {
                            Log.v(TAG, "packet from self, ignore");
                            continue;
                        }
                        if (packet.getData() != null) {
                            Log.v(TAG, "receive " + new String(packet.getData()).trim());
                            Map<String, String> data = new HashMap<>();
                            data.put(KEY_STR, DateUtils.getFormattedDatetime() + " 收到数据：" + new String(packet.getData()).trim());
                            mData.add(data);
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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

    private void startBroadcastThread() {
        new Thread() {
            @Override
            public void run() {

                DatagramSocket datagramSocket = null;

                try {

                    while (mMulticastLock == null || !mMulticastLock.isHeld()) {
                        Thread.sleep(1000);
                        continue;
                    }

                    InetAddress destAddress = InetAddress.getByName(sBroadcastIp);

                    datagramSocket = new DatagramSocket();
                    datagramSocket.setBroadcast(true);

                    while (true) {
                        Thread.sleep(1000);
                        byte[] sendMsg = sContent.getBytes();
                        DatagramPacket packet = new DatagramPacket(sendMsg, sendMsg.length, destAddress, BROADCAST_PORT);
                        datagramSocket.send(packet);
                        Log.v(TAG, "broadcast " + sContent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (datagramSocket != null) {
                        datagramSocket.close();
                    }
                }
            }
        }.start();
    }

    private void startBroadcastReceiveThread() {
        new Thread() {
            @Override
            public void run() {
                DatagramSocket receiveSocket = null;

                try {

                    while (mMulticastLock == null || !mMulticastLock.isHeld()) {
                        Thread.sleep(1000);
                        continue;
                    }

                    receiveSocket = new DatagramSocket(null);
                    receiveSocket.setReuseAddress(true);
                    receiveSocket.bind(new InetSocketAddress(BROADCAST_PORT));

                    DatagramPacket packet = new DatagramPacket(new byte[RECEIVE_LENGTH], RECEIVE_LENGTH);

                    String selfIp = NetworkUtils.getIpAddress();
                    while (true) {
                        receiveSocket.receive(packet);
                        if (packet.getAddress().getHostAddress().equals(selfIp)) {
                            Log.v(TAG, "packet from self, ignore");
                            continue;
                        }
                        if (packet.getData() != null) {
                            Log.v(TAG, "receive " + new String(packet.getData()).trim());
                            Map<String, String> data = new HashMap<>();
                            data.put(KEY_STR, DateUtils.getFormattedDatetime() + " 收到数据：" + new String(packet.getData()).trim());
                            mData.add(data);
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (receiveSocket != null) {
                        receiveSocket.close();
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

    @OnClick({R.id.send1111, R.id.send2222, R.id.send3333})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send1111:
                sContent = sContent1111;
                break;

            case R.id.send2222:
                sContent = sContent2222;
                break;

            case R.id.send3333:
                sContent = sContent3333;
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mMulticastLock = wifiManager.createMulticastLock("multicast.test");
        mMulticastLock.acquire();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMulticastLock.release();
    }

    public static InetAddress getBroadcastAddress(Context context) throws UnknownHostException {
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if(dhcp==null) {
            return InetAddress.getByName("255.255.255.255");
        }
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++) {
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }
        return InetAddress.getByAddress(quads);
    }
}
