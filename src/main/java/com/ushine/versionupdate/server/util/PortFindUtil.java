package com.ushine.versionupdate.server.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

/**
 * @author CHL
 * @Date 2021/7/8 17:22
 */
@Component
public class PortFindUtil {

    public  boolean isSocketAliveUitlitybyCrunchify(String hostName, int port) {
        boolean isAlive = false;

        // 创建一个套接字
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        Socket socket = new Socket();

        // 超时设置，单位毫秒
        int timeout = 2000;

        log("hostName: " + hostName + ", port: " + port);
        try {
            socket.connect(socketAddress, timeout);
            socket.close();
            isAlive = true;

        } catch (SocketTimeoutException exception) {
            System.out.println("SocketTimeoutException " + hostName + ":" + port + ". " + exception.getMessage());
        } catch (IOException exception) {
            System.out.println(
                    "IOException - Unable to connect to " + hostName + ":" + port + ". " + exception.getMessage());
        }
        return isAlive;
    }

    private static void log(String string) {
        System.out.println(string);
    }
}
