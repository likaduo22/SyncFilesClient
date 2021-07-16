package com.ushine.versionupdate.server.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author CHL
 * @Date 2021/6/18 14:54
 */
@Slf4j
public class UdpSend extends Thread {

    private DatagramSocket socket;

    public UdpSend(DatagramSocket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {
        try {

            while (true) {
                InetAddress address = InetAddress.getByName("localhost");
                int port = 8800;
                byte[] data = readFileVersion().getBytes();
                // 2.创建数据报，包含发送的数据信息
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

                // 3.向服务器端发送数据报
                log.info("向udp服务器发送报文!版本号：" + readFileVersion());

                socket.send(packet);

                Thread.sleep(10000);
            }
/*
            if( initialized.compareAndSet(false, true) ) {
                //第一个子线程执行
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    udpRevice.udpReviceInit(socket);
                    log.info("udp接收消息线程启动!");

                });
                executorService.shutdown();
            }*/

            /*
             * 接收服务器端响应的数据
             */
            // 1.创建数据报，用于接收服务器端响应的数据
           /* byte[] data2 = new byte[1024];
            DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
            // 2.接收服务器响应的数据
            socket.receive(packet2);
            // 3.读取数据
            String reply = new String(data2, 0, packet2.getLength());
            log.info("我是客户端，服务器说：" + reply);

            if (reply.equals("需要")) {

                log.info("启动tcp客户端!准备接收更新文件");

                tcpClient.client();
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    /**
     * 读取文件版本号
     *
     * @return 文件版本号
     */
    private static String readFileVersion() {

        File file = new File("E:\\360MoveData\\Users\\Administrator\\Desktop\\同步\\version.txt");
        String a = "";
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
             BufferedReader bufferedOutputStream = new BufferedReader(inputStreamReader)
        ) {
            String s ;
            while ((s = bufferedOutputStream.readLine()) != null) {

                a = s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return a;
    }
}
