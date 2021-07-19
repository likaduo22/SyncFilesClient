package com.ushine.versionupdate.server.util;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PortFindUtil {

    public  boolean isSocketAliveUitlitybyCrunchify(String hostName, String port) {

        boolean isAlive = false;

        try {
            Thread.sleep(10000);

            log.info("开始进入判断是否启动成功服务方法!!!!!!!");
            // 创建一个套接字
            SocketAddress socketAddress = new InetSocketAddress(hostName, Integer.parseInt(port));
            Socket socket = new Socket();

            // 超时设置，单位毫秒
            int timeout = 2000;

            log.info("hostName: " + hostName + ", port: " + port);
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
            log.info("返回判断服务是否启动成功状态："+isAlive);
            return isAlive;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }


}
