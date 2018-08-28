package com.mermaid.framework.util;

import org.apache.commons.lang.StringUtils;

import java.net.*;
import java.util.Enumeration;
import java.util.List;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/20 23:01
 * version 1.0
 */
public class IPUtil {
    private static String hostIP = StringUtils.EMPTY;

    static {

        String ip = null;
        Enumeration allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
                for (InterfaceAddress add : InterfaceAddress) {
                    InetAddress Ip = add.getAddress();
                    if (Ip != null && Ip instanceof Inet4Address) {
                        if (StringUtils.equals(Ip.getHostAddress(), "127.0.0.1")) {
                            continue;
                        }
                        ip = Ip.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        hostIP = ip;
    }

    /**
     * 获取本地IP
     * 通过获取系统所有的networkInterface网络接口，然后遍历
     * 每个网络下的InterfaceAddress组。
     * 获得符合<code>InetAddress instanceof Inet4Address</code>条件的一个IpV4地址
     * @return
     */
    public static String localIp() {
        return hostIP;
    }

    public static String getRealIp() {
        String localIp = null;
        String netIp = null;

        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;

            boolean finded = Boolean.FALSE;

            while (netInterfaces.hasMoreElements() && !finded) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if(!ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && !ip.getHostAddress().contains(":")) {
                        //外网
                        netIp = ip.getHostAddress();
                        finded = Boolean.TRUE;
                        break;
                    } else if(ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && !ip.getHostAddress().contains(":")){
                        //内网
                        localIp = ip.getHostAddress();
                    }
                }
            }

            if (org.springframework.util.StringUtils.hasText(netIp)) {
                return netIp;
            } else {
                return localIp;
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取主机第一个有效ip<br/>
     * 如果没有效ip，返回空串
     *
     * @return
     */
    public static String getHostFirstIp() {
        return hostIP;
    }


    public static void main(String[] args) throws Exception {
        //System.out.println(localIp());
        System.out.println(getRealIp());
        System.out.println(getHostFirstIp());
    }
}
