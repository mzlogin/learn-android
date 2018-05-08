package org.mazhuang.multicastdemo;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 网络工具方法合集。
 *
 * @author mazhuang
 * @date 2017 /8/3
 */
public abstract class NetworkUtils {
    /**
     * 获取本机 IP 地址。
     *
     * @return IP 地址，获取失败返回 null
     */
    public static String getIpAddress() {
        String result = null;
        try {
            for (Enumeration<NetworkInterface> enumNetInterfaces = NetworkInterface.getNetworkInterfaces(); enumNetInterfaces.hasMoreElements(); ) {
                NetworkInterface netInterface = enumNetInterfaces.nextElement();
                for (Enumeration<InetAddress> enumIpAddress = netInterface.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddress.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        result = inetAddress.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return result;
    }
}
