package com.ushine.versionupdate;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.ushine.versionupdate.server.constant.ProjectConstant;
import sun.java2d.pipe.TextRenderer;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author CHL
 * @Date 2021/6/23 14:06
 */
public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {
        //readFile();
       // writeFile(2);

       // File file = new File("E:\\360MoveData\\Users\\Administrator\\Desktop\\同步\\0.zip");

       /* String name = file.getName();

        int i = name.indexOf(".");

        String substring = name.substring(0, i);

        System.out.println(substring);*/
//
       // File file1 = new File("E:\\\\360MoveData\\\\Users\\\\Administrator\\\\Desktop\\\\同步\\\\version.txt");

       // File file2 = new File("E:\\360MoveData\\Users\\Administrator\\Desktop\\face-web");

        //Files.copy(  Paths.get("E:\\\\360MoveData\\\\Users\\\\Administrator\\\\Desktop\\\\同步\\\\version.txt"),Paths.get("E:\\360MoveData\\Users\\Administrator\\Desktop\\face-web\\version.txt"));




        //FileUtil.copy(file1,file2,true);

        //executionBat("E:\\360MoveData\\Users\\Administrator\\Desktop\\同步\\windows客户端bat\\sql.bat",
          //      "127.0.0.1","3306","root","root","xq-showroom1","E:\\360MoveData\\Users\\Administrator\\Desktop\\同步\\xq-showroom1.sql");

       /* String now = DateUtil.now();
        String trim = now.trim();
        String replace = now.replace(" ", "-");
        String replace1 = replace.replace(":", "-");

        Path path = Paths.get("E:\\360MoveData\\Users\\Administrator\\Desktop\\同步\\windows客户端bat", replace1 + "sql.sql");
        System.out.println(replace1);




        if(!Files.exists(path)){

            Files.createFile(path);
        }*/




        /*URL url = TextRenderer.class.getResource("a");
        String protocol = url.getProtocol();
        System.out.println(protocol);
*/

       // boolean localhost = isSocketAliveUitlitybyCrunchify("localhost", 9001);

        //System.out.println(localhost);

       // Integer integer = executionBat("E:\\360MoveData\\Users\\Administrator\\Desktop\\tongbu\\noSql\\start.bat", "xq-showroom", "D:\\project");

       // System.out.println(integer);

//        isSocketAliveUitlitybyCrunchify("localhost","9001");

    /*    String filePath = "E:\\360MoveData\\Users\\Administrator\\Desktop\\tongbu\\version.txt";

        if(!FileUtil.exist(filePath)){

            File tempFile = FileUtil.file(new File(filePath));

            FileUtil.writeBytes("0".getBytes(),filePath);
        }*/


     /*   InputStream inputStream = Runtime.getRuntime().exec("D:\\syncFiles\\4\\start.bat xq-showroom D:\\project\\").getInputStream();


        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();

        byte[] bytes = new byte[1024];

        int len;

        while ((len = inputStream.read(bytes)) != -1) {

            outSteam.write(bytes, 0, len);
        }


        String s = new String(outSteam.toByteArray(), StandardCharsets.UTF_8);

        System.out.println(s);*/

        Process exec =
                Runtime.getRuntime()
                        .exec("cmd.exe   /C   start D:\\syncFiles\\5\\start.bat xq-showroom D:\\project\\");


        exec.waitFor();

        int i = exec.exitValue();

        System.out.println(i);

        //InputStream in = exec.getInputStream();

       // BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

       /* byte[] bytes = new byte[1024];

        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int len;

        while ((len = in.read(buffer)) != -1) {

            outSteam.write(buffer, 0, len);
        }

        outSteam.close();

        String s = new String(outSteam.toByteArray(), StandardCharsets.UTF_8);

       // s = replaceBlank(s);

        System.out.println(s);*/

      /* String s ="1";

        while (bufferedReader.readLine() != null){

            s=bufferedReader.readLine();

           if(Integer.parseInt(s)== ProjectConstant.SUCCESS || Integer.parseInt(s) ==   ProjectConstant.FAIL){

                System.out.println("aaa:::"+Integer.parseInt(s));

            }
        }

        System.out.println(Integer.parseInt(s));*/
    }






    private static void log(String string) {
        System.out.println(string);
    }













    private static void writeFile(int version) {

        File file = new File("E:\\360MoveData\\Users\\Administrator\\Desktop\\同步\\version.txt");

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file));) {
            out.write(version + ""); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static  Integer executionBat(String path,String... args) throws IOException {

        StringBuilder arg = new StringBuilder(" ");

        for (String arg1 : args) {

            arg.append(arg1).append(" ");
        }

        // Runtime.getRuntime().exec("cmd.exe /c start  "+path+arg);

        System.out.println("cmd.exe /c start  "+path+arg);

        Process child = Runtime.getRuntime().exec("cmd.exe /c start /b " + path + arg);

        InputStream in = child.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String s = bufferedReader.readLine();

        while(s != null){

            s=bufferedReader.readLine();

            if(Integer.parseInt(s)== ProjectConstant.SUCCESS || Integer.parseInt(s) ==   ProjectConstant.FAIL){

                return Integer.parseInt(s);

            }
        }

        return null;
    }


    public static   boolean isSocketAliveUitlitybyCrunchify(String hostName, String port) {
        boolean isAlive = false;

       // log.info("开始进入判断是否启动成功服务方法!!!!!!!");
        // 创建一个套接字
        SocketAddress socketAddress = new InetSocketAddress(hostName, Integer.parseInt(port));
        Socket socket = new Socket();

        // 超时设置，单位毫秒
        int timeout = 2000;

        //log.info("hostName: " + hostName + ", port: " + port);
        try {
            socket.connect(socketAddress, timeout);
            socket.close();
            isAlive = true;

        } catch (SocketTimeoutException exception) {
            System.out.println("SocketTimeoutException " + hostName + ":" + port + ". " + exception.getMessage());
        } catch (IOException exception) {
            System.out.println(
                    "IOException - Unable to connect to " + hostName + ":" + port + ". " + exception.getMessage());
        }
       // log.info("返回判断服务是否启动成功状态："+isAlive);
        return isAlive;
    }

}
