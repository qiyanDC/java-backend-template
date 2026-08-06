package com.example.jbt.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ly
 * @since 2023/6/26
 */
public class HostUtils {

    private static final Logger log = LoggerFactory.getLogger(HostUtils.class);

    public static String VALID_HOST_REGEX = "^(?=.{1,255}$)[0-9A-Za-z](?:(?:[0-9A-Za-z]|\\b-){0,61}[0-9A-Za-z])?(?:\\.[0-9A-Za-z](?:(?:[0-9A-Za-z]|\\b-){0,61}[0-9A-Za-z])?)*\\.?$";

    /**
     * 获取主机名
     * */
    public static String getHostName(){
        return getLocalHostAddress(null);
    }

    /**
     * 获取主机名
     * */
    public static String getHostName(String type){
        return getLocalHostAddress(type);
    }

    private static String getLocalHostAddress(String type) {
        // 定义网络接口枚举类
        Enumeration<NetworkInterface> allNetInterfaces;
        try {
            // 获取本机网络接口
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            // 遍历网络接口
            while (allNetInterfaces.hasMoreElements()) {
                // 获取网络接口
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if(!isValidInterface(netInterface)) {
                    continue;
                }
                // 获取网络接口的地址
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                // 遍历地址
                while (addresses.hasMoreElements()) {
                    // 获取地址
                    InetAddress address = addresses.nextElement();
                    if (ObjectUtils.isNotEmpty(address) && address instanceof Inet4Address && !address.isLoopbackAddress()) {
                        if("hostName".equals(type)){
                            return address.getHostName();
                        }
                        return address.toString().substring(1);
                    }
                }
            }
        } catch (SocketException e) {
            log.error("获取本地IP异常", e);
        }
        return "Unknown";
    }

    private static boolean isValidInterface(NetworkInterface netInterface) throws SocketException {
        return ObjectUtils.isNotEmpty(netInterface)
                && !netInterface.isLoopback()
                && !netInterface.isPointToPoint()
                && !netInterface.isVirtual()
                && netInterface.isUp();
    }
}