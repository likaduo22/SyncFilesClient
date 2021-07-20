package com.ushine.versionupdate.server.udp;

import com.ushine.versionupdate.server.tcp.TcpClient;
import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author CHL
 * @Date 2021/6/24 16:01
 */

@Slf4j
public class UdpReceive extends Thread {

    private TcpClient tcpClient;


    private DatagramSocket socket;

    public UdpReceive(DatagramSocket socket, TcpClient tcpClient) {
        this.socket = socket;
        this.tcpClient = tcpClient;
    }

    @Override
    public void run() {

        /*
         * 接收服务器端响应的数据
         */
        // 1.创建数据报，用于接收服务器端响应的数据
        byte[] data2 = new byte[1024];
        DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
        // 2.接收服务器响应的数据
        try {

            while (true) {
                socket.receive(packet2);

                // 3.读取数据
                String reply = new String(data2, 0, packet2.getLength());
                log.info("我是客户端，服务器说：" + reply);

                if (reply.equals("需要")) {

                    log.info("启动tcp客户端!准备接收更新文件");

                     tcpClient.client();
                }

                Thread.sleep(51000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}