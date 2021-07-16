package com.ushine.versionupdate.server.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;

/**
 * @author CHL
 * @Date 2021/6/23 9:41
 */
@Component
@Slf4j
public class BatExecution {

    /**
     * 执行bat脚本
     * @param path 脚本路径
     * @param args 脚本所需参数
     */
    public Integer executionBat(Path path, String... args) throws IOException {

        StringBuilder arg = new StringBuilder(" ");

        for (String arg1 : args) {

            arg.append(arg1).append(" ");
        }

        log.info("执行脚本："+path+arg);
        Process exec = Runtime.getRuntime().exec("cmd.exe /c start /b  " + path.toString() + arg);
        InputStream in = exec.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//        String output = bufferedReader.readLine();
        return Integer.parseInt(bufferedReader.readLine());

       /* while(output!= null){

            System.out.println(output);

            output=bufferedReader.readLine();
        }*/
    }
}
