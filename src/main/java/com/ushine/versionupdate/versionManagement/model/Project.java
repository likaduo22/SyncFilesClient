package com.ushine.versionupdate.versionManagement.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author CHL
 * @Date 2021/6/17 10:12
 */
@Data
public class Project implements Serializable {

    private int id;
    /**
     * 版本号
     */
    private int version;

    /**
     * 客户端文件落盘地址
     */
   // private String path;

    /**
     * 压缩文件字节
     */
    private byte[] projectFile;

    /**
     * 服务存放地址
     */
    private String serverPath;

    /**
     * 是否有sql操作
     */
    private int noSql;

    /**
     * 服务端口号
     */
    private int port;

    /**
     *  数据库地址
     */
    private String dbHost;

    /**
     * 数据库端口
     */
    private int dbPort;

    /**
     * 数据库用户名
     */
    private String dbUser;

    /**
     * 数据库密码
     */
    private String dbPass;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 数据库bin目录路径
     */
    private String dbBinPath;

    /**
     * sql文件名称
     */
    private String sqlName;

}
