package com.ushine.versionupdate;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import sun.java2d.pipe.TextRenderer;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author CHL
 * @Date 2021/6/23 14:06
 */
public class Test {

    public static void main(String[] args) throws IOException {
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

        boolean localhost = isSocketAliveUitlitybyCrunchify("localhost", 9001);

        System.out.println(localhost);
    }




    public static boolean isSocketAliveUitlitybyCrunchify(String hostName, int port) {
        boolean isAlive = false;

        // 创建一个套接字
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        Socket socket = new Socket();

        // 超时设置，单位毫秒
        int timeout = 2000;

        log("hostName: " + hostName + ", port: " + port);
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
        return isAlive;
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


    public static  void executionBat(String path,String... args) throws IOException {

        StringBuilder arg = new StringBuilder(" ");

        for (String arg1 : args) {

            arg.append(arg1).append(" ");
        }

        // Runtime.getRuntime().exec("cmd.exe /c start  "+path+arg);

        System.out.println("cmd.exe /c start  "+path+arg);

        Process child = Runtime.getRuntime().exec("cmd.exe /c start /b " + path + arg);

        InputStream in = child.getInputStream();
        String output = null;
        int isSuccessful = 0;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        output=bufferedReader.readLine();

        while(output!= null){

            System.out.println(output);

            output=bufferedReader.readLine();

        }

    }
}
