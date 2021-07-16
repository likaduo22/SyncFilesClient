package com.ushine.versionupdate;

import com.ushine.versionupdate.server.udp.UdpSend;
import com.ushine.versionupdate.server.util.BatExecution;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class DemoApplicationTests {


    @Resource
    private UdpSend udp;
    @Resource
    private BatExecution batExecution;

    @Test
    void contextLoads() throws IOException, InterruptedException {

        batExecution.executionBat("E:\\sss\\startJar.bat","9001","55","66");
    }

}
