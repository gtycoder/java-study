package com.gty.testsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 实现简单的双向通讯,一应一答
 */
public class SocketTwo {

    /*==================服务端开始=====================*/
    //与之前的不通之处在于,当读取完客户端的消息后，打开输出流，将指定消息发送回客户端
    static class Server {
        public static void main(String[] args) throws IOException {
            int port = 8888;
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("-----服务端一直等待连接");
            Socket socket = serverSocket.accept();
            //当收到请求后
            InputStream inputStream = socket.getInputStream();
            byte[] b = new byte[1024];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(b)) != -1) {
                sb.append(new String(b, 0, len, "utf-8"));
            }

            System.out.println("---服务端接收到客户端的信息---"+sb.toString());
            //客户端返回消息
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("服务端收到了,ok".getBytes("utf-8"));
            //关闭
            outputStream.close();
            inputStream.close();
            socket.close();
            serverSocket.close();
        }


    }
    /*==================服务端结束=====================*/


    /*====================客户端开始===================*/
    //与之前不通:  在发送完消息时，调用关闭输出通道方法，然后打开输出流，等候服务端的消息。(不用做确认,tcp还是很可靠的)
    static class Client {
        public static void main(String[] args) throws IOException {
            String host = "127.0.0.1";
            int port = 8888;
            Socket socket = new Socket(host, port);
            //先给服务端发消息
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("服务端收到我的消息了吗,请回复".getBytes("utf-8"));

            //关闭输出的通道,如果不关闭,服务端将不会知道什么时候停止接收消息,将会一直接收消息
            //会导致持续连接,而不输出消息.关闭之后就需要创建新的socket连接了.
            //对于频繁的联系是很不合适的,so,一般需要指定一个约定的关闭符号或者指定消息的长度.
            socket.shutdownOutput();

            //接收服务端的回复
            InputStream inputStream = socket.getInputStream();
            byte[] b = new byte[1024];
            int len;

            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(b)) != -1) {
                sb.append(new String(b, 0, len, "utf-8"));
            }
            System.out.println("--客户端收到服务端的回复---" + sb.toString());
            //关闭资源
            inputStream.close();
            outputStream.close();
            socket.close();
        }
    }
    /*==================客户端结束=====================*/

}

