package com.gty.testsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 使用socket直接编写一个简单的redis客户端
 */
public class SocketRedis {

    private String host;
    private int port;
    private Socket socket;

    public SocketRedis(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void set(String key, String value) {
        //按照redis要求的规则构建命令,都要以\r\n结尾
        StringBuilder outputsb = new StringBuilder();
        // *3意思是当前的命令中包含3个内容,就应该有3个$num
        outputsb.append("*3").append("\r\n")
                //$3 表示下一个内容长度是3
                .append("$3").append("\r\n")
                //内容
                .append("set").append("\r\n")
                //$4  表示下一个有4个长度
                .append("$").append(key.length()).append("\r\n")
                .append(key).append("\r\n")
                //同理  写入value的长度和内容
                .append("$").append(value.length()).append("\r\n")
                .append(value).append("\r\n");
        //打印查看
        System.out.println(outputsb.toString());
        try {
            //创建客户端
            socket = new Socket(host, port);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(outputsb.toString().getBytes());
            socket.shutdownOutput();
            //接收回应
            InputStream inputStream = socket.getInputStream();
            StringBuilder inputsb = new StringBuilder();
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) != -1) {
                inputsb.append(new String(b, 0, len));
            }
            System.out.println(inputsb);
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String get(String key) {
        //按照redis要求的规则构建命令,都要以\r\n结尾
        StringBuilder outputsb = new StringBuilder();
        // *3意思是当前的命令中包含3个内容,就应该有3个$num
        outputsb.append("*2").append("\r\n")
                //$3 表示下一个内容长度是3
                .append("$3").append("\r\n")
                //内容
                .append("get").append("\r\n")
                //$4  表示下一个有4个长度
                .append("$").append(key.length()).append("\r\n")
                .append(key).append("\r\n");
        //打印查看
        System.out.println(outputsb.toString());
        //声明返回的字节数组
        byte[] b=null;
        try {
            //创建客户端
            socket = new Socket(host, port);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(outputsb.toString().getBytes());
            socket.shutdownOutput();
            //接收回应
            InputStream inputStream = socket.getInputStream();
            while (true) {
                //获取第一个字节,是$
                int first = inputStream.read();
                if (-1==first) {
                    break;
                }
                //获取第二个字节,是当前的返回值的长度
                int second = inputStream.read();

                b= new byte[second];
                inputStream.read(b);
            }
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(b);
    }


    public static void main(String[] args) {
        SocketRedis socketRedis = new SocketRedis("127.0.0.1", 6379);
        //socketRedis.set("num", "11 22 33 44");
        String name = socketRedis.get("name");
        System.out.println(name);
    }
}
