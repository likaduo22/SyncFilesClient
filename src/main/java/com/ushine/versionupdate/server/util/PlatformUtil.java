package com.ushine.versionupdate.server.util;

import org.springframework.stereotype.Component;

/**
 * @author CHL
 * @Date 2021/7/22 14:01
 * 分别平台工具
 */
@Component
public class PlatformUtil {

    /**
     * 根据平台获取对应的执行脚本全名
     * @param sql 脚本名称
     * @return 对应的平台脚本执行全名
     */
    public String isWindows(String sql){

        String system = System.getProperty("os.name");

        String s;

        if(system.contains("Windows")){

             s = sql + ".bat";
        }else {

             s = sql + ".sh";
        }

        return s;
    }
}
