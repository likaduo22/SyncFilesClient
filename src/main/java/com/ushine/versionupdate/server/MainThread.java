package com.ushine.versionupdate.server;

import com.ushine.versionupdate.server.tcp.TcpClient;
import com.ushine.versionupdate.server.udp.UdpSend;
import com.ushine.versionupdate.server.udp.UdpReceive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.DatagramSocket;

/**
 * @author CHL
 * @Date 2021/6/24 15:57
 */
@Component
@Slf4j
public class MainThread {

    @Resource
    private TcpClient tcpClient;
    @PostConstruct
    void initUdp() {

            try {
                DatagramSocket server = new DatagramSocket();

                new Thread(new UdpSend(server)).start();

                new Thread(new UdpReceive(server,tcpClient)).start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
