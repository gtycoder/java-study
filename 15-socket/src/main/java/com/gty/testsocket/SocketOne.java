package com.gty.testsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 实现了单向的通信,客户端给服务端发消息
 */
public class SocketOne {

    /*=============================服务端========================*/
    static class Service {
        public static void main(String[] args) throws IOException {
            int port = 8888;
            ServerSocket serverSocket = new ServerSocket(port);
            //该服务会一直阻塞等待连接,当有client连接才会真正返回操作连接的操作对象
            System.out.println("该服务会一直阻塞等待连接");
            Socket socket = serverSocket.accept();
            //当有了连接就取出连接中的数据
            InputStream inputStream = socket.getInputStream();
            byte[] b = new byte[1024];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            //当等于-1就说明结束了
            while ((len = inputStream.read(b))!= -1) {
                //这里要注意:最好指定编码统一,并且注意String导入的包
                sb.append(new String(b, 0, len, "utf-8"));
            }
            //输出客户端发过来的消息
            System.out.println(sb.toString());
            //关闭众多的流
            inputStream.close();
            socket.close();
            serverSocket.close();
        }
    }
    /*========================服务端结束==================================*/


    /*=======================客户端开始===================================*/
    static class Client {
        public static void main(String[] args) throws IOException {
            String hostname = "127.0.0.1";
            int port = 8888;
            //创建一个客户端的socket
            Socket socket = new Socket(hostname, port);
            //输出内容
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("第一个socket程序".getBytes("utf-8"));
            //关闭连接
            outputStream.close();
            socket.close();
        }
    }
    /*=======================客户端结束==========================*/

}
