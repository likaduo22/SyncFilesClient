package com.ushine.versionupdate.server.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    public Integer executionBat(Path path, String... args) {

        String s= "1";

        StringBuilder arg = new StringBuilder(" ");

        for (String arg1 : args) {

            arg.append(arg1).append(" ");
        }

        log.info("执行脚本："+path+arg);

        try {
            Process exec = Runtime.getRuntime().exec("cmd.exe /c start /b  " + path.toString() + arg);

            exec.waitFor();

            s = String.valueOf(exec.exitValue());

        } catch (Exception e) {

            log.info(e.getMessage());
        }

        return Integer.parseInt(s);
    }



   /* private static byte[] readStream(InputStream inStream) throws Exception {

        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int len;

        while ((len = inStream.read(buffer)) != -1) {

            outSteam.write(buffer, 0, len);
        }

        outSteam.close();

        return outSteam.toByteArray();

    }


    private static String replaceBlank(String str) {

        String dest = "";

        if (str != null) {

            Pattern p = Pattern.compile("\\s*|\t|\r|\n");

            Matcher m = p.matcher(str);

            dest = m.replaceAll("");

        }

        return dest;

    }*/
}
