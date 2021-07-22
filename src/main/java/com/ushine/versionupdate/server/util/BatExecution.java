package com.ushine.versionupdate.server.util;

import com.ushine.versionupdate.server.constant.ProjectConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
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
    public Integer executionBat(Path path, String... args) {

        StringBuilder arg = new StringBuilder(" ");

        for (String arg1 : args) {

            arg.append(arg1).append(" ");
        }

        log.info("执行脚本："+path+arg);

        String s = null;
        try {

            Process exec;

            String system = System.getProperty("os.name");

            if(system.contains("Windows")){

                exec = Runtime.getRuntime().exec("cmd.exe /c start /b  " + path.toString() + arg);
            }else {

                Runtime.getRuntime().exec("chmod 777 -R " + path);

                exec= Runtime.getRuntime().exec(path.toString() + arg);
            }

            BufferedReader input = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            while ((s = input.readLine()) != null) {

                log.info("执行脚本"+path+"后返回值："+s);

                int i = Integer.parseInt(s);

                if(i == ProjectConstant.SUCCESS || i == ProjectConstant.FAIL){

                    return i;
                }
            }

            input.close();

        } catch (Exception e) {

            log.info(e.getMessage());
        }

        return s == null ? 1 : 0;
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
