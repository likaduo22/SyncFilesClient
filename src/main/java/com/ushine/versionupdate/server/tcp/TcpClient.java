package com.ushine.versionupdate.server.tcp;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.ushine.versionupdate.server.constant.ProjectConstant;
import com.ushine.versionupdate.server.util.BatExecution;
import com.ushine.versionupdate.server.util.PlatformUtil;
import com.ushine.versionupdate.server.util.PortFindUtil;
import com.ushine.versionupdate.versionManagement.model.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author CHL
 * @Date 2021/6/17 15:15
 */
@Component
@Slf4j
public class TcpClient {

    //tcp服务端端口
    @Value(value = "${tcp.port}")
    private int port;
    //tcp服务端ip
    @Value(value = "${tcp.ip}")
    private String ip;
    //客户端同步的文件存放地址
    @Value(value = "${version.address}")
    private String address;
    //停止服务bat名称
    @Value(value = "${bat.stop}")
    private String stop;
    //启动服务bat文件名称
    @Value(value = "${bat.start}")
    private String start;
    //启动服务bat文件名称
    @Value(value = "${bat.sql}")
    //执行备份sql bat文件名称
    private String sql;
    //服务名称
    @Value(value = "${version.serverName}")
    private String serverName;
    //执行sql文件 bat文件名
    @Value(value = "${bat.executeSql}")
    private String executeSql;
    //服务名称
    @Value(value = "${version.versionFile}")
    private String versionFile;

    @Resource
    private PlatformUtil platformUtil;
    @Resource
    private BatExecution batExecution;
    @Resource
    private PortFindUtil portFindUtil;


    public void client() { // 连接套接字方法

        // 连接到服务器
        try (Socket socket = new Socket(ip, port)


        ) {
                // 网络上面的流
                InputStream is = socket.getInputStream();
                log.info("开始接收服务端下发更新文件!");

                ObjectInputStream  ObjectInputStream  = new ObjectInputStream(is);
                    //接收同步文件
                    Project o =  (Project)ObjectInputStream.readObject();

                    String path = address+o.getVersion()+".zip";
                    //写出文件
                    writeBytesToFile(o.getProjectFile(),path);
                    //解压文件
                    File unzip = ZipUtil.unzip(path);
                    //对于同步文件的处理
                    dealWithUpdateVersion(unzip.toString(),o);

                    //log.info("解压返回路径:"+unzip.toPath().toString());

        } catch (Exception e) {
            //捕获异常
            e.printStackTrace();
        }
    }


    /**
     * 将字节写入文件
     * @param bs 文件字节
     * @param address 写入地址
     */
    private void writeBytesToFile(byte[] bs, String address) {
        try (
                OutputStream out = new FileOutputStream(address);
                InputStream is = new ByteArrayInputStream(bs)
        ) {

            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) != -1) {

                out.write(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更改文件版本号
     * @param version 版本号
     */
    private  void writeFile(int version) {

        log.info("更改的版本号："+version);

        File file = new File(versionFile);

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            out.write(version + ""); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 同步文件解压后的处理
     * @param unzipPath 文件解压后的路径
     * @param project 同步对象
     */
    private void dealWithUpdateVersion(String unzipPath,Project project){

        log.info("开始处理接收的新版文件!");
        //停止服务脚本路径
        Path path = Paths.get(unzipPath, platformUtil.isWindows(stop));
        //执行备份sql脚本路径
        Path backSqlPath = Paths.get(unzipPath, platformUtil.isWindows(sql));
        //版本号处理
        int ver = project.getVersion() == 0 ? 0 : project.getVersion()-1;
        //备份sql文件存放路径包含文件名称
        Path pathFile = Paths.get(project.getServerPath(), ver + "", getSqlName());
        //执行sql bat文件路径
        Path executeSqlPath = Paths.get(unzipPath, platformUtil.isWindows(executeSql));
        try {
            //执行停止服务脚本
            batExecution.executionBat(path, String.valueOf(project.getPort()));
            //执行备份sql脚本
            Integer backStatus = batExecution.executionBat(backSqlPath,
                    project.getDbHost(), project.getDbPort() + "", project.getDbUser(), project.getDbPass(), project.getDbName(),
                    pathFile.toString(), project.getDbBinPath());

            Thread.sleep(5000);
            log.info("执行停止后开始休眠!");
            //判断是否有sql处理
            if (project.getNoSql() == ProjectConstant.HAVE) {
                /**
                 * 需要操作sql时的执行
                 */
                sqlOperating(executeSqlPath, unzipPath, project, ver,backStatus, pathFile);
            } else {
                /**
                 * 不需要操作sql时的执行
                 */
                noSqlOperating(executeSqlPath, unzipPath, project,pathFile,ver);
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }


    /**
     * 执行有sql操作逻辑
     * @param unzipPath 解压路径
     * @param project 同步对
     * @param ver 版本号
     * @throws IOException IO异常
     */
    private void sqlOperating(Path executeSqlPath,String unzipPath,Project project,int ver,int backStatus,Path pathFile) throws IOException {


        //备份sql文件存放路径
        Path dbPathDirector = Paths.get(project.getServerPath(), ver+"");

        //判断备份sql文件目录是否存在
        if(!Files.exists(dbPathDirector)){

            Files.createDirectories(dbPathDirector);
        }

        //执行更改数据结构sql
        if(!StringUtils.isEmpty(project.getSqlName())){

            //执行修改表sql路径
            Path sqlPath = Paths.get(unzipPath, project.getSqlName());
            //执行修改表脚本
            Integer  integer1 = batExecution.executionBat(executeSqlPath, project.getDbUser(), project.getDbPass(), sqlPath.toString(), project.getDbBinPath(), project.getDbName());
            //在需要执行修改表sql时 必须满足修改表和备份sql都成功 才能替换服务包
            if(backStatus == ProjectConstant.SUCCESS && integer1 == ProjectConstant.SUCCESS){

                //对应包复制进服务路径
                String serverPath = getServerPath(unzipPath);
                //把更新服务包复制到服务路径下
                fileCopyUtil(serverPath,project.getServerPath());
            }

        }else{
            //在只需要备份sql时 备份sql成功则可替换服务包
            if(backStatus == ProjectConstant.SUCCESS){
                //对应包复制进服务路径
                String serverPath = getServerPath(unzipPath);
                //把更新服务包复制到服务路径下
                fileCopyUtil(serverPath,project.getServerPath());
            }
        }
        //同步过后检测服务是否正常启动操作
        synchronizationJudgmentProcessing(unzipPath,executeSqlPath,project,pathFile.toString(),ver);
    }


    /**
     * 执行无sql操作逻辑
     * @param unzipPath 解压路径
     * @param project 同步对象
     */
    private void noSqlOperating(Path executeSqlPath,String unzipPath,Project project,Path pathFile,int ver){

        //对应包复制进服务路径
        String serverPath = getServerPath(unzipPath);
        //把更新服务包复制到服务路径下
        fileCopyUtil(serverPath,project.getServerPath());
        //同步过后检测服务是否正常启动操作
        synchronizationJudgmentProcessing(unzipPath,executeSqlPath,project,pathFile.toString(),ver);
    }




    /**
     * 根据解压目录获取同步服务的路径
     * @param unzipPath 解压目录
     * @return 同步文件服务的完成路径
     */
    private String getServerPath(String unzipPath){

        File file = new File(unzipPath);

        File[] files = file.listFiles();

            for (File sFile : files) {
                String name = sFile.getName();

                int i = name.indexOf(".");

                String fileName = name.substring(0, i);

                if(fileName.equals(serverName)){

                    return Paths.get(unzipPath,name).toString();
                }

            }
        return  null;
    }

    /**
     * 文件复制工具
     * @param versionPath 源文件路径
     * @param projectPath 目标路径
     */
    private void fileCopyUtil(String versionPath,String projectPath){

        if(versionPath != null) {

            FileUtil.copy(new File(versionPath), new File(projectPath), true);
        }

    }


    /**
     * 同步操作后 检测服务是否启动操作
     * @param unzipPath 解压路径
     * @param executeSqlPath 执行sql bat文件路径
     * @param project 同步对象
     * @param pathFile 备份sql存放路径
     */
    private void synchronizationJudgmentProcessing(String unzipPath,Path executeSqlPath,Project project,String pathFile,int ver){

         //启动服务脚本路径
         Path startPath = Paths.get(unzipPath, platformUtil.isWindows(start));
         //执行启动服务脚本
         batExecution.executionBat(startPath, serverName, project.getServerPath());
        //检测port是否启动占用 未启动成功则恢复操作
        if (portFindUtil.isSocketAliveUitlitybyCrunchify("localhost", project.getPort())) {
            //服务启动成功 更改文件版本号
            writeFile(project.getVersion());
        }else{
            /**
             * 服务启动失败 替换服务包 和 恢复sql数据
             */
            //上一版本服务包存放路径
            String upVersionPath = getServerPath(address + (ver));
            //同步失败时的处理
            //恢复数据库
            batExecution.executionBat(executeSqlPath, project.getDbUser(), project.getDbPass(), pathFile, project.getDbBinPath(), project.getDbName());
            if(upVersionPath != null) {
                //恢复服务包操作
                fileCopyUtil(upVersionPath, project.getServerPath());
            }
            //启动服务操作
            batExecution.executionBat(startPath, serverName, project.getServerPath());
        }
    }


    /**
     * 获取备份sql文件名称
     * @return 备份sql文件名称
     */
    private String getSqlName(){
        //当时时间
        String now = DateUtil.now();
        //处理时间成为备份sql的文件名称
        String replace = now.replace(" ", "-");
        String replace1 = replace.replace(":", "-");
        //备份sql名称
        return serverName + "-" + replace1 + ".sql";
    }
}

