package com.ushine.versionupdate.initialization;

import cn.hutool.core.io.FileUtil;
import com.ushine.versionupdate.server.tcp.TcpClient;
import com.ushine.versionupdate.server.udp.UdpReceive;
import com.ushine.versionupdate.server.udp.UdpSend;

import java.io.File;
import java.net.DatagramSocket;

/**
 * @author CHL
 * @Date 2021/7/19 16:18
 */
public class ApplicationInit extends Thread{

    private String filePath;

    private TcpClient tcpClient;

    private DatagramSocket socket;

    private int port;

    private String ip;

    public ApplicationInit(String filePath,DatagramSocket socket,TcpClient tcpClient,String ip,int port) {

        this.filePath = filePath;
        this.socket = socket;
        this.tcpClient = tcpClient;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {

        if(!FileUtil.exist(filePath)){

            FileUtil.file(new File(filePath));

            FileUtil.writeBytes("0".getBytes(),filePath);
        }

        new Thread(new UdpSend(socket,filePath,port,ip)).start();

        new Thread(new UdpReceive(socket,tcpClient)).start();
    }
}
